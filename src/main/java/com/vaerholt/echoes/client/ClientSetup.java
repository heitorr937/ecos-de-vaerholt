package com.vaerholt.echoes.client;

import com.vaerholt.echoes.EchoesMod;
import com.vaerholt.echoes.client.model.CeifadoModel;
import com.vaerholt.echoes.client.model.EnlutadoModel;
import com.vaerholt.echoes.client.model.FlayedOneModel;
import com.vaerholt.echoes.client.model.ModModelLayers;
import com.vaerholt.echoes.client.model.PossuidoModel;
import com.vaerholt.echoes.client.model.WhispererModel;
import com.vaerholt.echoes.client.renderer.CeifadoRenderer;
import com.vaerholt.echoes.client.renderer.EnlutadoRenderer;
import com.vaerholt.echoes.client.renderer.FlayedOneRenderer;
import com.vaerholt.echoes.client.renderer.PossuidoRenderer;
import com.vaerholt.echoes.client.renderer.WhispererRenderer;
import com.vaerholt.echoes.registry.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EchoesMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.SUSSURRANTE, WhispererModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.DESCARNADO, FlayedOneModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.CEIFADO, CeifadoModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ENLUTADO, EnlutadoModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.POSSUIDO, PossuidoModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SUSSURRANTE.get(), WhispererRenderer::new);
        event.registerEntityRenderer(ModEntities.DESCARNADO.get(), FlayedOneRenderer::new);
        event.registerEntityRenderer(ModEntities.CEIFADO.get(), CeifadoRenderer::new);
        event.registerEntityRenderer(ModEntities.ENLUTADO.get(), EnlutadoRenderer::new);
        event.registerEntityRenderer(ModEntities.POSSUIDO.get(), PossuidoRenderer::new);
    }
}
