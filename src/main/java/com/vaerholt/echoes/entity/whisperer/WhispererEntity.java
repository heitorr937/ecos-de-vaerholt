package com.vaerholt.echoes.entity.whisperer;

import com.vaerholt.echoes.registry.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class WhispererEntity extends PathfinderMob {

    private int stareTicks = 0;
    private int cooldownTeleport = 0;

    public WhispererEntity(EntityType<? extends WhispererEntity> type, Level level) {
        super(type, level);
        this.setNoAi(false);
        this.xpReward = 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.18D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;
        if (cooldownTeleport > 0) cooldownTeleport--;

        Player nearest = this.level().getNearestPlayer(this, 20.0D);
        if (nearest == null) {
            stareTicks = 0;
            return;
        }

        if (isBeingStaredAt(nearest)) {
            stareTicks++;
            this.getLookControl().setLookAt(nearest, 180f, 180f);
            if (stareTicks == 30) {
                sayFragment(nearest);
            }
            if (stareTicks > 45 && cooldownTeleport == 0) {
                teleportAwayFrom(nearest);
            }
        } else {
            stareTicks = Math.max(0, stareTicks - 2);
        }
    }

    private boolean isBeingStaredAt(Player player) {
        double dist = player.distanceTo(this);
        if (dist > 16) return false;
        var toEntity = this.position().subtract(player.getEyePosition()).normalize();
        var look = player.getLookAngle().normalize();
        return look.dot(toEntity) > 0.97;
    }

    private void sayFragment(Player player) {
        this.level().playSound(null, this.blockPosition(), ModSounds.WHISPER_PHRASE.get(),
                SoundSource.HOSTILE, 1.0f, 0.8f + this.random.nextFloat() * 0.3f);
    }

    private void teleportAwayFrom(Player player) {
        if (!(this.level() instanceof ServerLevel sl)) return;
        RandomSource r = this.random;
        for (int i = 0; i < 8; i++) {
            double ang = r.nextDouble() * Math.PI * 2;
            double dist = 10 + r.nextDouble() * 6;
            double x = player.getX() + Math.cos(ang) * dist;
            double z = player.getZ() + Math.sin(ang) * dist;
            var pos = this.level().getHeightmapPos(
                    net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,
                    net.minecraft.core.BlockPos.containing(x, player.getY(), z));
            this.teleportTo(x, pos.getY(), z);
            sl.sendParticles(ParticleTypes.PORTAL, this.getX(), this.getY() + 1, this.getZ(),
                    12, 0.3, 0.5, 0.3, 0.01);
            stareTicks = 0;
            cooldownTeleport = 60;
            return;
        }
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        return this.level().getMaxLocalRawBrightness(this.blockPosition()) < 8
                && super.checkSpawnRules(level, spawnType);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushEntities() {
    }
}
