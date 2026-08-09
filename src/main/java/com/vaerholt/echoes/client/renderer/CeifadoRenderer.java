package com.vaerholt.echoes.client.renderer;

import com.vaerholt.echoes.EchoesMod;
import com.vaerholt.echoes.client.model.CeifadoModel;
import com.vaerholt.echoes.client.model.ModModelLayers;
import com.vaerholt.echoes.entity.ceifado.CeifadoEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CeifadoRenderer extends MobRenderer<CeifadoEntity, CeifadoModel> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(EchoesMod.MODID, "textures/entity/ceifado.png");

    public CeifadoRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new CeifadoModel(ctx.bakeLayer(ModModelLayers.CEIFADO)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(CeifadoEntity entity) {
        return TEXTURE;
    }
}
