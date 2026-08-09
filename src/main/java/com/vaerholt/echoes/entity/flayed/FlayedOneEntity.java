package com.vaerholt.echoes.entity.flayed;

import com.vaerholt.echoes.capability.DreadCapability;
import com.vaerholt.echoes.item.LanternaVaerholtItem;
import com.vaerholt.echoes.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * O antagonista principal. Regras de design deliberadas para NÃO ser um mob "OP que mata
 * toda hora":
 *  - Sempre anda em velocidade "andando" (nunca corre), então é sempre possível se afastar
 *    a pé se você notar cedo.
 *  - Ataque tem alcance curto e dano moderado (não gib de um hit), e ele SEMPRE emite um
 *    aviso sonoro (respiração pesada) que aumenta de volume/pitch conforme se aproxima —
 *    o jogador tem informação suficiente pra reagir se prestar atenção.
 *  - Detecção: baseada em RUÍDO (correr, quebrar bloco, portas) e LUZ SUSTENTADA (lanterna
 *    no modo alto por vários segundos seguidos), não em "visão" pura — ficar parado, sem luz
 *    forte e em silêncio realmente funciona para escapar dele.
 *  - Esconderijo (ArmarioEsconderijoBlock) zera completamente sua capacidade de detecção
 *    daquele jogador enquanto durar.
 */
public class FlayedOneEntity extends PathfinderMob {

    private int breathTimer = 0;

    public FlayedOneEntity(EntityType<? extends FlayedOneEntity> type, Level level) {
        super(type, level);
        this.xpReward = 15;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.19D) // deliberadamente mais lento que o player andando não-sprint
                .add(Attributes.ATTACK_DAMAGE, 5.0D)   // dano moderado, não instakill
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.6D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.6D));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        Player target = findDetectablePlayer();
        if (target != null) {
            this.setTarget((net.minecraft.world.entity.LivingEntity) target);
            playBreathCue(target);
        } else if (this.getTarget() != null) {
            // perde o alvo se ele se escondeu ou saiu do alcance de detecção
            this.setTarget(null);
        }
    }

    /** Retorna o jogador mais próximo detectável por som/luz, ou null. Respeita o esconderijo. */
    private Player findDetectablePlayer() {
        List<ServerPlayer> nearby = this.level().getEntitiesOfClass(ServerPlayer.class,
                this.getBoundingBox().inflate(28));
        Player best = null;
        double bestScore = 0;

        for (ServerPlayer sp : nearby) {
            boolean hidden = DreadCapability.get(sp).map(c -> c.isHiding()).orElse(false);
            if (hidden) continue;

            double dist = this.distanceTo(sp);
            double score = 0;

            // ruído: sprint conta muito, andar pouco, parado quase nada
            if (sp.isSprinting()) score += 14;
            else if (sp.walkDist > 0 && !sp.isCrouching()) score += 4;

            // luz sustentada da lanterna no modo alto
            ItemStack flashlight = sp.getMainHandItem().getItem() instanceof LanternaVaerholtItem
                    ? sp.getMainHandItem() : sp.getOffhandItem();
            if (flashlight.getItem() instanceof LanternaVaerholtItem
                    && LanternaVaerholtItem.isOn(flashlight) && LanternaVaerholtItem.isHighBeam(flashlight)) {
                score += 10;
            }

            // pavor alto: a entidade "sente" quando o jogador está em ruptura, mesmo quieto
            float dread = DreadCapability.get(sp).map(c -> c.getDread()).orElse(0f);
            if (dread >= 85f) score += 6;

            score -= dist * 0.35; // distância reduz a chance de detecção

            if (score > 8 && score > bestScore) {
                bestScore = score;
                best = sp;
            }
        }
        return best;
    }

    private void playBreathCue(Player target) {
        breathTimer++;
        double dist = this.distanceTo(target);
        int interval = (int) Math.max(10, dist * 2); // mais perto = respiração mais frequente
        if (breathTimer >= interval) {
            breathTimer = 0;
            float volume = (float) Math.max(0.2, 1.5 - dist / 20.0);
            float pitch = (float) Math.max(0.7, 1.2 - dist / 30.0);
            this.level().playSound(null, this.blockPosition(), ModSounds.FLAYED_BREATH.get(),
                    SoundSource.HOSTILE, volume, pitch);
        }
    }

    @Override
    public boolean doHurtTarget(net.minecraft.world.entity.Entity entity) {
        boolean result = super.doHurtTarget(entity);
        if (result) {
            this.level().playSound(null, this.blockPosition(), ModSounds.FLAYED_ROAR.get(),
                    SoundSource.HOSTILE, 1.0f, 0.9f);
        }
        return result;
    }

    @Override
    protected float getStandingEyeHeight(net.minecraft.world.entity.Pose pose, net.minecraft.world.entity.EntityDimensions dim) {
        return dim.height * 0.85f;
    }
}
