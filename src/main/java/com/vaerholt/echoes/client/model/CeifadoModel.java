package com.vaerholt.echoes.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vaerholt.echoes.entity.ceifado.CeifadoEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

/** Proporções humanoides normais (era gente), mas com uma picareta presa nas costas
 *  esculpida no próprio modelo — silhueta reconhecível à distância como "mineiro". */
public class CeifadoModel extends EntityModel<CeifadoEntity> {

    private final ModelPart cabeca, torso, bracoEsq, bracoDir, pernaEsq, pernaDir, picareta;

    public CeifadoModel(ModelPart root) {
        this.cabeca = root.getChild("cabeca");
        this.torso = root.getChild("torso");
        this.bracoEsq = root.getChild("braco_esq");
        this.bracoDir = root.getChild("braco_dir");
        this.pernaEsq = root.getChild("perna_esq");
        this.pernaDir = root.getChild("perna_dir");
        this.picareta = torso.getChild("picareta");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("cabeca",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4f, -8f, -4f, 8, 8, 8),
                PartPose.offset(0, -24f, 0));

        PartDefinition torso = root.addOrReplaceChild("torso",
                CubeListBuilder.create().texOffs(16, 16).addBox(-4f, 0f, -2f, 8, 12, 4),
                PartPose.offset(0, -24f, 0));

        torso.addOrReplaceChild("picareta",
                CubeListBuilder.create().texOffs(40, 0).addBox(-0.5f, -10f, -1f, 1, 14, 1),
                PartPose.offsetAndRotation(0, 2f, 2f, 0.6f, 0, 0.7f));

        root.addOrReplaceChild("braco_esq",
                CubeListBuilder.create().texOffs(40, 16).addBox(-1.5f, -2f, -1.5f, 3, 12, 3),
                PartPose.offset(5f, -22f, 0));
        root.addOrReplaceChild("braco_dir",
                CubeListBuilder.create().texOffs(48, 16).addBox(-1.5f, -2f, -1.5f, 3, 12, 3),
                PartPose.offset(-5f, -22f, 0));
        root.addOrReplaceChild("perna_esq",
                CubeListBuilder.create().texOffs(0, 16).addBox(-2f, 0f, -2f, 4, 12, 4),
                PartPose.offset(2f, -12f, 0));
        root.addOrReplaceChild("perna_dir",
                CubeListBuilder.create().texOffs(8, 16).addBox(-2f, 0f, -2f, 4, 12, 4),
                PartPose.offset(-2f, -12f, 0));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(CeifadoEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        cabeca.yRot = netHeadYaw * ((float) Math.PI / 180F);
        cabeca.xRot = headPitch * ((float) Math.PI / 180F);
        pernaEsq.xRot = Mth.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
        pernaDir.xRot = Mth.cos(limbSwing * 0.6662f + (float) Math.PI) * 1.4f * limbSwingAmount;
        bracoEsq.xRot = Mth.cos(limbSwing * 0.6662f + (float) Math.PI) * 1.2f * limbSwingAmount;
        bracoDir.xRot = Mth.cos(limbSwing * 0.6662f) * 1.2f * limbSwingAmount;
        if (entity.swinging) {
            bracoDir.xRot = -1.5f + Mth.cos(ageInTicks * 1.2f) * 0.4f; // golpe de picareta
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        for (ModelPart part : new ModelPart[]{cabeca, torso, bracoEsq, bracoDir, pernaEsq, pernaDir}) {
            part.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
        }
    }
}
