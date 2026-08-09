package com.vaerholt.echoes.registry;

import com.vaerholt.echoes.EchoesMod;
import com.vaerholt.echoes.item.BateriaDePicheItem;
import com.vaerholt.echoes.item.CustomSpawnEggItem;
import com.vaerholt.echoes.item.DiarioDeVaerholtItem;
import com.vaerholt.echoes.item.LanternaVaerholtItem;
import net.minecraft.world.item.Item;
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

    // Ovos de spawn com arte própria (não o padrão de duas cores do vanilla)
    public static final RegistryObject<Item> SUSSURRANTE_SPAWN_EGG = ITEMS.register("sussurrante_spawn_egg",
            () -> new CustomSpawnEggItem(ModEntities.SUSSURRANTE.get(), new Item.Properties()));

    public static final RegistryObject<Item> DESCARNADO_SPAWN_EGG = ITEMS.register("descarnado_spawn_egg",
            () -> new CustomSpawnEggItem(ModEntities.DESCARNADO.get(), new Item.Properties()));

    public static final RegistryObject<Item> CEIFADO_SPAWN_EGG = ITEMS.register("ceifado_spawn_egg",
            () -> new CustomSpawnEggItem(ModEntities.CEIFADO.get(), new Item.Properties()));

    public static final RegistryObject<Item> ENLUTADO_SPAWN_EGG = ITEMS.register("enlutado_spawn_egg",
            () -> new CustomSpawnEggItem(ModEntities.ENLUTADO.get(), new Item.Properties()));
}
