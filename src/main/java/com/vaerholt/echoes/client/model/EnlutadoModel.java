package com.vaerholt.echoes.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vaerholt.echoes.entity.enlutado.EnlutadoEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

/**
 * Figura alta e imóvel, de cartola e terno. Não tem animação de andar (ele nunca anda —
 * só aparece/desaparece), então a única "animação" é um tremor quase imperceptível,
 * como estática, para nunca parecer 100% uma estátua morta.
 */
public class EnlutadoModel extends EntityModel<EnlutadoEntity> {

    private final ModelPart cabeca, cartola, torso, bracoEsq, bracoDir, pernaEsq, pernaDir;

    public EnlutadoModel(ModelPart root) {
        this.cabeca = root.getChild("cabeca");
        this.cartola = cabeca.getChild("cartola");
        this.torso = root.getChild("torso");
        this.bracoEsq = root.getChild("braco_esq");
        this.bracoDir = root.getChild("braco_dir");
        this.pernaEsq = root.getChild("perna_esq");
        this.pernaDir = root.getChild("perna_dir");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition cabeca = root.addOrReplaceChild("cabeca",
                CubeListBuilder.create().texOffs(16, 6).addBox(-4f, -8f, -4f, 8, 8, 8),
                PartPose.offset(0, -48f, 0)); // bem alto

        cabeca.addOrReplaceChild("cartola",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-5f, -3f, -5f, 10, 1, 10)   // aba
                        .texOffs(0, 0).addBox(-4f, -9f, -4f, 8, 6, 8),     // copa
                PartPose.offset(0, -8f, 0));

        root.addOrReplaceChild("torso",
                CubeListBuilder.create().texOffs(0, 20).addBox(-5f, 0f, -3f, 10, 24, 6),
                PartPose.offset(0, -48f, 0));

        root.addOrReplaceChild("braco_esq",
                CubeListBuilder.create().texOffs(28, 14).addBox(-1.5f, -2f, -1.5f, 3, 26, 3),
                PartPose.offset(6f, -46f, 0));
        root.addOrReplaceChild("braco_dir",
                CubeListBuilder.create().texOffs(28, 14).addBox(-1.5f, -2f, -1.5f, 3, 26, 3),
                PartPose.offset(-6f, -46f, 0));

        root.addOrReplaceChild("perna_esq",
                CubeListBuilder.create().texOffs(0, 46).addBox(-2f, 0f, -2f, 4, 24, 4),
                PartPose.offset(2.5f, -24f, 0));
        root.addOrReplaceChild("perna_dir",
                CubeListBuilder.create().texOffs(0, 46).addBox(-2f, 0f, -2f, 4, 24, 4),
                PartPose.offset(-2.5f, -24f, 0));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(EnlutadoEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        cabeca.yRot = netHeadYaw * ((float) Math.PI / 180F);
        cabeca.xRot = headPitch * ((float) Math.PI / 180F);
        // tremor de estática quase imperceptível — nunca completamente parado
        float jitter = (float) (Math.sin(ageInTicks * 3.1) * 0.01);
        torso.zRot = jitter;
        bracoEsq.zRot = jitter * 2;
        bracoDir.zRot = -jitter * 2;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        for (ModelPart part : new ModelPart[]{cabeca, torso, bracoEsq, bracoDir, pernaEsq, pernaDir}) {
            part.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
        }
    }
}
