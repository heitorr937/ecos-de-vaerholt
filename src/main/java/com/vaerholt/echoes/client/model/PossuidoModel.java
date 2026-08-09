package com.vaerholt.echoes.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vaerholt.echoes.entity.possuido.PossuidoAnimalEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

/** Silhueta quadrúpede genérica e distorcida — corpo "errado", pernas em ângulos que não
 *  deveriam existir, para vender a ideia de "isso já foi um animal normal". */
public class PossuidoModel extends EntityModel<PossuidoAnimalEntity> {

    private final ModelPart corpo, cabeca, p1, p2, p3, p4;

    public PossuidoModel(ModelPart root) {
        this.corpo = root.getChild("corpo");
        this.cabeca = root.getChild("cabeca");
        this.p1 = root.getChild("perna1");
        this.p2 = root.getChild("perna2");
        this.p3 = root.getChild("perna3");
        this.p4 = root.getChild("perna4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("corpo",
                CubeListBuilder.create().texOffs(0, 0).addBox(-5f, -4f, -8f, 10, 8, 16),
                PartPose.offset(0, -10f, 0));
        root.addOrReplaceChild("cabeca",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3f, -3f, -5f, 6, 6, 5),
                PartPose.offsetAndRotation(0, -12f, -9f, 0.2f, 0, 0));

        for (int i = 0; i < 4; i++) {
            float x = (i % 2 == 0) ? 3f : -3f;
            float z = (i < 2) ? -5f : 5f;
            root.addOrReplaceChild("perna" + (i + 1),
                    CubeListBuilder.create().texOffs(20, 0).addBox(-1.5f, 0f, -1.5f, 3, 10, 3),
                    PartPose.offset(x, -8f, z));
        }
        return LayerDefinition.create(mesh, 48, 32);
    }

    @Override
    public void setupAnim(PossuidoAnimalEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        p1.xRot = Mth.cos(limbSwing * 0.9f) * 1.3f * limbSwingAmount;
        p2.xRot = Mth.cos(limbSwing * 0.9f + (float) Math.PI) * 1.3f * limbSwingAmount;
        p3.xRot = Mth.cos(limbSwing * 0.9f + (float) Math.PI) * 1.3f * limbSwingAmount;
        p4.xRot = Mth.cos(limbSwing * 0.9f) * 1.3f * limbSwingAmount;
        cabeca.yRot = netHeadYaw * ((float) Math.PI / 180F) * 1.4f; // vira a cabeça longe do natural
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        for (ModelPart part : new ModelPart[]{corpo, cabeca, p1, p2, p3, p4}) {
            part.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
        }
    }
}
