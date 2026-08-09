package com.vaerholt.echoes.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vaerholt.echoes.entity.flayed.FlayedOneEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

/**
 * Corpo encurvado, braços desproporcionalmente longos (chegam quase ao chão), cabeça
 * pequena e sempre ligeiramente inclinada para trás — reforça visualmente que ele não
 * tem olhos e "ouve" o ambiente em vez de olhar para ele.
 */
public class FlayedOneModel extends EntityModel<FlayedOneEntity> {

    private final ModelPart cabeca, torso, bracoEsq, bracoDir, pernaEsq, pernaDir;

    public FlayedOneModel(ModelPart root) {
        this.cabeca = root.getChild("cabeca");
        this.torso = root.getChild("torso");
        this.bracoEsq = root.getChild("braco_esq");
        this.bracoDir = root.getChild("braco_dir");
        this.pernaEsq = root.getChild("perna_esq");
        this.pernaDir = root.getChild("perna_dir");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("cabeca",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3f, -4f, -3f, 6, 6, 6),
                PartPose.offsetAndRotation(0, -37f, -1f, 0.35f, 0, 0)); // inclinada pra trás

        root.addOrReplaceChild("torso",
                CubeListBuilder.create().texOffs(0, 14).addBox(-5f, 0f, -3f, 10, 22, 6),
                PartPose.offsetAndRotation(0, -37f, 0, 0.25f, 0, 0)); // levemente curvado

        root.addOrReplaceChild("braco_esq",
                CubeListBuilder.create().texOffs(28, 14).addBox(-1.5f, 0f, -1.5f, 3, 26, 3),
                PartPose.offset(5.5f, -35f, 0));

        root.addOrReplaceChild("braco_dir",
                CubeListBuilder.create().texOffs(40, 14).addBox(-1.5f, 0f, -1.5f, 3, 26, 3),
                PartPose.offset(-5.5f, -35f, 0));

        root.addOrReplaceChild("perna_esq",
                CubeListBuilder.create().texOffs(0, 42).addBox(-2f, 0f, -2f, 4, 15, 4),
                PartPose.offset(2.5f, -15f, 0));

        root.addOrReplaceChild("perna_dir",
                CubeListBuilder.create().texOffs(16, 42).addBox(-2f, 0f, -2f, 4, 15, 4),
                PartPose.offset(-2.5f, -15f, 0));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(FlayedOneEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Passo arrastado, deliberado, sem pressa — o oposto de um mob "rápido e OP".
        pernaEsq.xRot = Mth.cos(limbSwing * 0.4f) * 1.0f * limbSwingAmount;
        pernaDir.xRot = Mth.cos(limbSwing * 0.4f + (float) Math.PI) * 1.0f * limbSwingAmount;
        // Braços balançam largo e assíncrono - movimento "errado" propositalmente
        bracoEsq.xRot = Mth.cos(limbSwing * 0.4f + (float) Math.PI) * 0.6f * limbSwingAmount;
        bracoDir.xRot = Mth.cos(limbSwing * 0.4f) * 0.6f * limbSwingAmount;

        // A cabeça NÃO segue o alvo normalmente (ele é cego) — em vez disso, treme sutilmente,
        // como se estivesse "escutando" em várias direções.
        float t = ageInTicks / 20f;
        cabeca.yRot = (float) Math.sin(t * 1.3) * 0.25f;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        for (ModelPart part : new ModelPart[]{cabeca, torso, bracoEsq, bracoDir, pernaEsq, pernaDir}) {
            part.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
        }
    }
}
