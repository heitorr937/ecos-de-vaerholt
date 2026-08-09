package com.vaerholt.echoes.registry;

import com.vaerholt.echoes.EchoesMod;
import com.vaerholt.echoes.entity.ceifado.CeifadoEntity;
import com.vaerholt.echoes.entity.enlutado.EnlutadoEntity;
import com.vaerholt.echoes.entity.flayed.FlayedOneEntity;
import com.vaerholt.echoes.entity.possuido.PossuidoAnimalEntity;
import com.vaerholt.echoes.entity.whisperer.WhispererEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, EchoesMod.MODID);

    public static final RegistryObject<EntityType<WhispererEntity>> SUSSURRANTE = ENTITY_TYPES.register("sussurrante",
            () -> EntityType.Builder.of(WhispererEntity::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.9f)
                    .clientTrackingRange(16)
                    .build(new net.minecraft.resources.ResourceLocation(EchoesMod.MODID, "sussurrante").toString()));

    public static final RegistryObject<EntityType<FlayedOneEntity>> DESCARNADO = ENTITY_TYPES.register("descarnado",
            () -> EntityType.Builder.of(FlayedOneEntity::new, MobCategory.MONSTER)
                    .sized(0.7f, 2.3f)
                    .clientTrackingRange(20)
                    .build(new net.minecraft.resources.ResourceLocation(EchoesMod.MODID, "descarnado").toString()));

    public static final RegistryObject<EntityType<CeifadoEntity>> CEIFADO = ENTITY_TYPES.register("ceifado",
            () -> EntityType.Builder.of(CeifadoEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.8f)
                    .clientTrackingRange(12)
                    .build(new net.minecraft.resources.ResourceLocation(EchoesMod.MODID, "ceifado").toString()));

    public static final RegistryObject<EntityType<EnlutadoEntity>> ENLUTADO = ENTITY_TYPES.register("enlutado",
            () -> EntityType.Builder.of(EnlutadoEntity::new, MobCategory.MISC)
                    .sized(0.7f, 2.6f)
                    .clientTrackingRange(48)
                    .build(new net.minecraft.resources.ResourceLocation(EchoesMod.MODID, "enlutado").toString()));

    public static final RegistryObject<EntityType<PossuidoAnimalEntity>> POSSUIDO = ENTITY_TYPES.register("possuido",
            () -> EntityType.Builder.of(PossuidoAnimalEntity::new, MobCategory.MONSTER)
                    .sized(0.9f, 0.9f)
                    .clientTrackingRange(16)
                    .build(new net.minecraft.resources.ResourceLocation(EchoesMod.MODID, "possuido").toString()));
}
