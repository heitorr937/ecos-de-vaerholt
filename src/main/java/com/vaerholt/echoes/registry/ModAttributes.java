package com.vaerholt.echoes.registry;

import com.vaerholt.echoes.entity.ceifado.CeifadoEntity;
import com.vaerholt.echoes.entity.enlutado.EnlutadoEntity;
import com.vaerholt.echoes.entity.flayed.FlayedOneEntity;
import com.vaerholt.echoes.entity.possuido.PossuidoAnimalEntity;
import com.vaerholt.echoes.entity.whisperer.WhispererEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

public class ModAttributes {
    public static void register(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SUSSURRANTE.get(), WhispererEntity.createAttributes().build());
        event.put(ModEntities.DESCARNADO.get(), FlayedOneEntity.createAttributes().build());
        event.put(ModEntities.CEIFADO.get(), CeifadoEntity.createAttributes().build());
        event.put(ModEntities.ENLUTADO.get(), EnlutadoEntity.createAttributes().build());
        event.put(ModEntities.POSSUIDO.get(), PossuidoAnimalEntity.createAttributes().build());
    }
}
