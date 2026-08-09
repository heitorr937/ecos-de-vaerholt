package com.vaerholt.echoes.client.renderer;

import com.vaerholt.echoes.EchoesMod;
import com.vaerholt.echoes.client.model.FlayedOneModel;
import com.vaerholt.echoes.client.model.ModModelLayers;
import com.vaerholt.echoes.entity.flayed.FlayedOneEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FlayedOneRenderer extends MobRenderer<FlayedOneEntity, FlayedOneModel> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(EchoesMod.MODID, "textures/entity/descarnado.png");

    public FlayedOneRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new FlayedOneModel(ctx.bakeLayer(ModModelLayers.DESCARNADO)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(FlayedOneEntity entity) {
        return TEXTURE;
    }
}
