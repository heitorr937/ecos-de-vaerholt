package com.vaerholt.echoes.registry;

import com.vaerholt.echoes.EchoesMod;
import com.vaerholt.echoes.item.BateriaDePicheItem;
import com.vaerholt.echoes.item.DiarioDeVaerholtItem;
import com.vaerholt.echoes.item.LanternaVaerholtItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, EchoesMod.MODID);

    public static final RegistryObject<Item> LANTERNA_VAERHOLT = ITEMS.register("lanterna_vaerholt",
            () -> new LanternaVaerholtItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> BATERIA_DE_PICHE = ITEMS.register("bateria_de_piche",
            () -> new BateriaDePicheItem(new Item.Properties()));

    public static final RegistryObject<Item> DIARIO_DE_VAERHOLT = ITEMS.register("diario_de_vaerholt",
            () -> new DiarioDeVaerholtItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> PRATA_VIVA = ITEMS.register("prata_viva",
            () -> new Item(new Item.Properties()));

    // NÃO chamar .get() aqui — passar o RegistryObject direto (ele é um Supplier)
    public static final RegistryObject<Item> SUSSURRANTE_SPAWN_EGG = ITEMS.register("sussurrante_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.SUSSURRANTE, 0x1a1a22, 0xd8d8e0, new Item.Properties()));

    public static final RegistryObject<Item> DESCARNADO_SPAWN_EGG = ITEMS.register("descarnado_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.DESCARNADO, 0x0d0d0d, 0x7a1414, new Item.Properties()));

    public static final RegistryObject<Item> CEIFADO_SPAWN_EGG = ITEMS.register("ceifado_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.CEIFADO, 0x2b2b2b, 0x556b2f, new Item.Properties()));

    public static final RegistryObject<Item> ENLUTADO_SPAWN_EGG = ITEMS.register("enlutado_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ENLUTADO, 0x1a1a1a, 0x4a4a4a, new Item.Properties()));
}
