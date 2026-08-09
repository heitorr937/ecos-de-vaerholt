package com.vaerholt.echoes.entity.possuido;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * O que sobra quando a entidade "veste" um animal como pele. Visualmente reconhecível
 * como o animal original só na silhueta — a textura e o comportamento denunciam que algo
 * está errado. Spawna via RandomScareManager/PossessionManager quando substitui um animal
 * passivo próximo do jogador em Pavor alto (ver com detalhes em events/PossessionManager).
 * Fraco individualmente (não é para ser uma luta difícil), mas o choque de "a vaca virou
 * isso" é o dano real.
 */
public class PossuidoAnimalEntity extends Monster {

    public PossuidoAnimalEntity(EntityType<? extends PossuidoAnimalEntity> type, Level level) {
        super(type, level);
        this.xpReward = 3;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.15D, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
}
