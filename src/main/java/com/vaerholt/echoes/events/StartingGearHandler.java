package com.vaerholt.echoes.events;

import com.vaerholt.echoes.EchoesMod;
import com.vaerholt.echoes.item.LanternaVaerholtItem;
import com.vaerholt.echoes.registry.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * O jogador começa com a lanterna direto no inventário (offhand se possível) — sem
 * precisar craftar nada primeiro. Usa uma flag persistente por jogador para garantir que
 * isso só acontece uma vez, mesmo se ele morrer/voltar depois.
 */
@Mod.EventBusSubscriber(modid = EchoesMod.MODID)
public class StartingGearHandler {

    private static final String FLAG = "echoes_recebeu_lanterna";

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        var player = event.getEntity();
        if (player.getPersistentData().getBoolean(FLAG)) return;
        player.getPersistentData().putBoolean(FLAG, true);

        ItemStack lanterna = new ItemStack(ModItems.LANTERNA_VAERHOLT.get());
        LanternaVaerholtItem.setBattery(lanterna, LanternaVaerholtItem.BATERIA_MAX);

        if (player.getOffhandItem().isEmpty()) {
            player.setItemInHand(net.minecraft.world.InteractionHand.OFF_HAND, lanterna);
        } else if (!player.addItem(lanterna)) {
            player.drop(lanterna, false);
        }

        // uma bateria reserva de brinde, já que ele não crafta a lanterna do zero
        ItemStack bateriaExtra = new ItemStack(ModItems.BATERIA_DE_PICHE.get(), 1);
        if (!player.addItem(bateriaExtra)) player.drop(bateriaExtra, false);
    }
}
