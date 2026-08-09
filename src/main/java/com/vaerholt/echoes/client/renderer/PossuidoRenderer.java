package com.vaerholt.echoes.client.renderer;

import com.vaerholt.echoes.EchoesMod;
import com.vaerholt.echoes.client.model.ModModelLayers;
import com.vaerholt.echoes.client.model.PossuidoModel;
import com.vaerholt.echoes.entity.possuido.PossuidoAnimalEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PossuidoRenderer extends MobRenderer<PossuidoAnimalEntity, PossuidoModel> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(EchoesMod.MODID, "textures/entity/possuido_animal.png");

    public PossuidoRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new PossuidoModel(ctx.bakeLayer(ModModelLayers.POSSUIDO)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(PossuidoAnimalEntity entity) {
        return TEXTURE;
    }
}
