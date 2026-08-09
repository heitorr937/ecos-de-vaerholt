package com.vaerholt.echoes.registry;

import com.vaerholt.echoes.EchoesMod;
import com.vaerholt.echoes.block.ArmarioEsconderijoBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, EchoesMod.MODID);
    public static final DeferredRegister<Item> BLOCK_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, EchoesMod.MODID);

    // Bloco/mobília em que o jogador pode se agachar e entrar para "prender a respiração"
    // e sumir do radar de som/visão do Descarnado por um tempo limitado.
    public static final RegistryObject<Block> ARMARIO_ESCONDERIJO = registerWithItem(
            "armario_esconderijo",
            () -> new ArmarioEsconderijoBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .strength(2.5f)
                    .noOcclusion()
                    .pushReaction(PushReaction.BLOCK))
    );

    private static <T extends Block> RegistryObject<T> registerWithItem(String name, Supplier<T> block) {
        RegistryObject<T> b = BLOCKS.register(name, block);
        BLOCK_ITEMS.register(name, () -> new BlockItem(b.get(), new Item.Properties()));
        return b;
    }
}
