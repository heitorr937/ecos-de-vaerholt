package com.vaerholt.echoes.entity.enlutado;

import com.vaerholt.echoes.capability.DreadCapability;
import com.vaerholt.echoes.registry.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * "O Enlutado" — figura de cartola e terno que NUNCA persegue de perto. Ele aparece
 * parado, longe, sempre olhando na direção do jogador, e desaparece (com um efeito de
 * partículas/som) assim que o jogador se aproxima demais ou olha para outro lugar e volta
 * a olhar para ele. É puramente um "stalker" de tensão ambiental — nunca ataca — mas cada
 * aparição empurra bastante o Pavor, e aparições ficam mais frequentes e mais próximas
 * conforme o Pavor sobe. Lore: é o que restou do capataz de Vaerholt, o primeiro a
 * "responder" às vozes do Ventre.
 */
public class EnlutadoEntity extends Mob {

    private int vidaTicks = 200; // desaparece sozinho depois de um tempo se não for visto

    public EnlutadoEntity(EntityType<? extends EnlutadoEntity> type, Level level) {
        super(type, level);
        this.xpReward = 0;
        this.setNoAi(true); // não se move sozinho - a "movimentação" dele é só aparecer/sumir
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        Player nearest = getNearestPlayerRaw();
        if (nearest != null) {
            this.getLookControl().setLookAt(nearest, 180f, 180f);
        }

        vidaTicks--;
        if (nearest != null) {
            double dist = this.distanceTo(nearest);
            boolean lookingAtMe = isBeingWatched(nearest);

            if (dist < 8.0 || (lookingAtMe && vidaTicks < 140)) {
                vanish();
                return;
            }
            if (vidaTicks % 60 == 0 && dist < 40) {
                this.level().playSound(null, this.blockPosition(), ModSounds.WHISPER_LOOP.get(),
                        SoundSource.HOSTILE, 0.3f, 0.6f);
            }
        }

        if (vidaTicks <= 0) vanish();
    }

    private boolean isBeingWatched(Player player) {
        var toEntity = this.position().subtract(player.getEyePosition()).normalize();
        var look = player.getLookAngle().normalize();
        return look.dot(toEntity) > 0.98 && player.distanceTo(this) < 30 && player.hasLineOfSight(this);
    }

    private Player getNearestPlayerRaw() {
        return this.level().getNearestPlayer(this, 48.0D);
    }

    private void vanish() {
        if (!(this.level() instanceof ServerLevel sl)) return;
        sl.sendParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 1, this.getZ(), 20, 0.4, 0.8, 0.4, 0.02);
        this.level().playSound(null, this.blockPosition(), ModSounds.STATIC_GLITCH.get(), SoundSource.HOSTILE, 0.6f, 0.5f);
        Player nearest = getNearestPlayerRaw();
        if (nearest instanceof ServerPlayer sp) {
            DreadCapability.get(sp).ifPresent(cap -> cap.addDread(10f));
        }
        this.discard();
    }

    @Override
    public boolean isPersistenceRequired() { return true; }

    @Override
    public boolean removeWhenFarAway(double dist) { return false; }
}
