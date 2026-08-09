package com.vaerholt.echoes.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vaerholt.echoes.entity.whisperer.WhispererEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

/**
 * Modelo próprio, feito à mão (sem reaproveitar humanoid vanilla): figura alta, fina,
 * "envolta em panos", sem braços visíveis (movimento de balanço passivo lembra um manto
 * pendurado, não uma pessoa andando normalmente — propositalmente "errado").
 */
public class WhispererModel extends EntityModel<WhispererEntity> {

    private final ModelPart cabeca;
    private final ModelPart torsoManto;
    private final ModelPart saia;

    public WhispererModel(ModelPart root) {
        this.cabeca = root.getChild("cabeca");
        this.torsoManto = root.getChild("torso_manto");
        this.saia = root.getChild("saia");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("cabeca",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.5f, -6f, -3.5f, 7, 7, 7),
                PartPose.offset(0, -30f, 0));

        root.addOrReplaceChild("torso_manto",
                CubeListBuilder.create().texOffs(0, 20).addBox(-5f, 0f, -3f, 10, 18, 6),
                PartPose.offset(0, -30f, 0));

        root.addOrReplaceChild("saia",
                CubeListBuilder.create().texOffs(0, 46).addBox(-6f, 0f, -4f, 12, 14, 8),
                PartPose.offset(0, -12f, 0));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(WhispererEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Nada de balanço de perna/braço "normal": só um sway sutil e a cabeça, que fazem
        // dela algo que se move de um jeito quase-humano, mas não exatamente.
        float t = ageInTicks / 20f;
        torsoManto.zRot = (float) (Math.sin(t * 0.8) * 0.03);
        saia.zRot = (float) (Math.sin(t * 0.8 + 0.5) * 0.05);
        cabeca.yRot = netHeadYaw * ((float) Math.PI / 180F);
        cabeca.xRot = headPitch * ((float) Math.PI / 180F);
        cabeca.zRot = (float) (Math.sin(t * 0.4) * 0.02); // leve inclinação errática
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        cabeca.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
        torsoManto.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
        saia.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
    }
}
