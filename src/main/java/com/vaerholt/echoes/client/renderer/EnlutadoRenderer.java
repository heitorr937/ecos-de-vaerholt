package com.vaerholt.echoes.client.renderer;

import com.vaerholt.echoes.EchoesMod;
import com.vaerholt.echoes.client.model.EnlutadoModel;
import com.vaerholt.echoes.client.model.ModModelLayers;
import com.vaerholt.echoes.entity.enlutado.EnlutadoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EnlutadoRenderer extends MobRenderer<EnlutadoEntity, EnlutadoModel> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(EchoesMod.MODID, "textures/entity/enlutado.png");

    public EnlutadoRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new EnlutadoModel(ctx.bakeLayer(ModModelLayers.ENLUTADO)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(EnlutadoEntity entity) {
        return TEXTURE;
    }
}
