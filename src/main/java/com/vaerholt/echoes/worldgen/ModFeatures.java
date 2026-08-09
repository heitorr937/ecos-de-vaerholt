package com.vaerholt.echoes.worldgen;

import com.vaerholt.echoes.EchoesMod;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, EchoesMod.MODID);

    public static final RegistryObject<VaerholtRuinFeature> VAERHOLT_RUIN = FEATURES.register(
            "vaerholt_ruin", () -> new VaerholtRuinFeature(NoneFeatureConfiguration.CODEC));
}
