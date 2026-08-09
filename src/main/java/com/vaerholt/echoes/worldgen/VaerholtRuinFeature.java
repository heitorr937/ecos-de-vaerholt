package com.vaerholt.echoes.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Uma pequena câmara de pedra em ruínas gerada em cavernas do overworld — um pedaço
 * literal de Vaerholt encontrado embaixo da terra. Contém sempre um baú com loot de lore
 * (páginas de diário, Prata-Viva). É deliberadamente pequena e simples (uma sala 5x4x5)
 * para não depender de arquivo .nbt externo — assim ela é 100% gerada em código e sempre
 * válida, sem risco de arquivo de estrutura corrompido/incompatível.
 */
public class VaerholtRuinFeature extends Feature<NoneFeatureConfiguration> {

    public static final ResourceLocation LOOT_TABLE =
            new ResourceLocation("echoes", "chests/vaerholt_ruin");

    public VaerholtRuinFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel level = ctx.level();
        BlockPos origin = ctx.origin();

        BlockState parede = Blocks.COBBLESTONE.defaultBlockState();
        BlockState paredeMusgo = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
        BlockState chao = Blocks.STONE_BRICKS.defaultBlockState();
        BlockState ar = Blocks.CAVE_AIR.defaultBlockState();

        int w = 5, h = 4, d = 5;

        // esvazia o interior e constrói uma caixa simples de pedra
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                for (int z = 0; z < d; z++) {
                    BlockPos p = origin.offset(x, y, z);
                    boolean borda = x == 0 || x == w - 1 || z == 0 || z == d - 1 || y == 0 || y == h - 1;
                    if (borda) {
                        level.setBlock(p, level.getRandom().nextFloat() < 0.25f ? paredeMusgo : parede, 3);
                    } else {
                        level.setBlock(p, y == 1 ? chao : ar, 3);
                    }
                }
            }
        }

        // entrada estreita numa das paredes
        level.setBlock(origin.offset(2, 1, 0), ar, 3);
        level.setBlock(origin.offset(2, 2, 0), ar, 3);

        // teia de aranha decorativa
        level.setBlock(origin.offset(1, h - 2, 1), Blocks.COBWEB.defaultBlockState(), 3);
        level.setBlock(origin.offset(w - 2, h - 2, d - 2), Blocks.COBWEB.defaultBlockState(), 3);

        // vela apagada num canto (atmosfera) - usa tocha comum como substituto simples
        level.setBlock(origin.offset(1, 1, 1), Blocks.SOUL_LANTERN.defaultBlockState(), 3);

        // baú com o loot de lore no centro
        BlockPos chestPos = origin.offset(w / 2, 1, d / 2);
        level.setBlock(chestPos, Blocks.CHEST.defaultBlockState(), 3);
        BlockEntity be = level.getBlockEntity(chestPos);
        if (be instanceof ChestBlockEntity chest) {
            chest.setLootTable(LOOT_TABLE, level.getRandom().nextLong());
        }

        return true;
    }
}
