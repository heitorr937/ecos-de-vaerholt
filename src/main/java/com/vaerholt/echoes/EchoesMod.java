package com.vaerholt.echoes;

import com.vaerholt.echoes.registry.*;
import com.vaerholt.echoes.dread.DreadEventHandler;
import com.vaerholt.echoes.events.RandomScareManager;
import com.vaerholt.echoes.client.light.FlashlightLightHandler;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.api.distmarker.Dist;

/**
 * ECOS DE VAERHOLT
 * Mod de terror psicológico para Forge 1.20.1.
 * Ponto de entrada: registra tudo e liga os sistemas de gameplay
 * (Pavor, eventos aleatórios, lanterna dinâmica).
 */
@Mod(EchoesMod.MODID)
public class EchoesMod {

    public static final String MODID = "echoes";

    public EchoesMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.ENTITY_TYPES.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.BLOCK_ITEMS.register(modBus);
        ModSounds.SOUND_EVENTS.register(modBus);
        com.vaerholt.echoes.worldgen.ModFeatures.FEATURES.register(modBus);

        modBus.addListener(this::commonSetup);
        modBus.addListener(this::buildCreativeTab);
        modBus.addListener(com.vaerholt.echoes.registry.ModAttributes::register);

        // Sistemas globais de gameplay ligados ao barramento do Forge (mundo/jogador)
        MinecraftForge.EVENT_BUS.register(new DreadEventHandler());
        MinecraftForge.EVENT_BUS.register(new RandomScareManager());
        MinecraftForge.EVENT_BUS.register(new com.vaerholt.echoes.dread.AmbientDroneHandler());
        MinecraftForge.EVENT_BUS.register(new com.vaerholt.echoes.events.PossessionManager());
        MinecraftForge.EVENT_BUS.register(new com.vaerholt.echoes.events.JumpscareManager());
        // StartingGearHandler se auto-registra via @Mod.EventBusSubscriber
        // Roda no lado lógico do servidor (inclusive em singleplayer); ver comentário na classe.
        MinecraftForge.EVENT_BUS.register(new FlashlightLightHandler());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // nada de pesado aqui; registries já cuidam do essencial
    }

    private void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.LANTERNA_VAERHOLT);
            event.accept(ModItems.BATERIA_DE_PICHE);
        }
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModBlocks.ARMARIO_ESCONDERIJO);
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.DIARIO_DE_VAERHOLT);
            event.accept(ModItems.PRATA_VIVA);
        }
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.SUSSURRANTE_SPAWN_EGG);
            event.accept(ModItems.DESCARNADO_SPAWN_EGG);
            event.accept(ModItems.CEIFADO_SPAWN_EGG);
            event.accept(ModItems.ENLUTADO_SPAWN_EGG);
        }
    }
}
