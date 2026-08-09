package com.vaerholt.echoes.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Craftável com Prata-Viva + carvão. Usada sobre a lanterna para recarregar a bateria. */
public class BateriaDePicheItem extends Item {
    public BateriaDePicheItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack battery = player.getItemInHand(hand);
        ItemStack offhand = player.getOffhandItem();
        if (offhand.getItem() instanceof LanternaVaerholtItem) {
            LanternaVaerholtItem.setBattery(offhand, LanternaVaerholtItem.BATERIA_MAX);
            if (!player.getAbilities().instabuild) battery.shrink(1);
            return InteractionResultHolder.sidedSuccess(battery, level.isClientSide);
        }
        return InteractionResultHolder.pass(battery);
    }
}
