package com.vaerholt.echoes.client.renderer;

import com.vaerholt.echoes.EchoesMod;
import com.vaerholt.echoes.client.model.WhispererModel;
import com.vaerholt.echoes.entity.whisperer.WhispererEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WhispererRenderer extends MobRenderer<WhispererEntity, WhispererModel> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(EchoesMod.MODID, "textures/entity/sussurrante.png");

    public WhispererRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new WhispererModel(ctx.bakeLayer(com.vaerholt.echoes.client.model.ModModelLayers.SUSSURRANTE)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(WhispererEntity entity) {
        return TEXTURE;
    }
}
