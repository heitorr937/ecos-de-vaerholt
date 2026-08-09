package com.vaerholt.echoes.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

/**
 * Ovo de geração com textura própria pintada à mão. O ForgeSpawnEggItem vanilla ignora
 * qualquer PNG e sempre desenha um círculo de duas cores via código — para ter a arte
 * customizada de verdade (pedida nas referências), este item usa um modelo
 * item/generated normal e implementa a lógica de spawn manualmente.
 */
public class CustomSpawnEggItem extends Item {
    private final EntityType<? extends Mob> entityType;

    public CustomSpawnEggItem(EntityType<? extends Mob> entityType, Properties props) {
        super(props);
        this.entityType = entityType;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!(context.getLevel() instanceof ServerLevel level)) return InteractionResult.SUCCESS;
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        Mob mob = entityType.create(level);
        if (mob == null) return InteractionResult.FAIL;
        mob.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        mob.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), net.minecraft.world.entity.MobSpawnType.SPAWN_EGG, null, null);
        level.addFreshEntity(mob);
        context.getItemInHand().shrink(context.getPlayer() != null && context.getPlayer().getAbilities().instabuild ? 0 : 1);
        return InteractionResult.CONSUME;
    }
}
