package com.vaerholt.echoes.registry;

import com.vaerholt.echoes.EchoesMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EchoesMod.MODID);

    private static RegistryObject<SoundEvent> reg(String name) {
        ResourceLocation id = new ResourceLocation(EchoesMod.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    // Ambiência geral / drone de fundo do Ventre
    public static final RegistryObject<SoundEvent> AMBIENT_DRONE = reg("ambient.drone");
    // Sussurros indistintos (Sussurrante por perto / Pavor médio)
    public static final RegistryObject<SoundEvent> WHISPER_LOOP = reg("whisper.loop");
    // Frase fragmentada dita pelo Sussurrante ao ser encarado
    public static final RegistryObject<SoundEvent> WHISPER_PHRASE = reg("whisper.phrase");
    // Batimento cardíaco (escala com Pavor / proximidade do Descarnado)
    public static final RegistryObject<SoundEvent> HEARTBEAT = reg("heartbeat");
    // Respiração pesada do Descarnado
    public static final RegistryObject<SoundEvent> FLAYED_BREATH = reg("flayed.breath");
    // Rugido de ataque do Descarnado
    public static final RegistryObject<SoundEvent> FLAYED_ROAR = reg("flayed.roar");
    // Passos arrastados
    public static final RegistryObject<SoundEvent> FLAYED_STEP = reg("flayed.step");
    // Grito distante (evento aleatório)
    public static final RegistryObject<SoundEvent> DISTANT_SCREAM = reg("event.distant_scream");
    // Estática/glitch (evento de Pavor alto)
    public static final RegistryObject<SoundEvent> STATIC_GLITCH = reg("event.static_glitch");
    // Batida em porta/parede (evento aleatório)
    public static final RegistryObject<SoundEvent> KNOCK = reg("event.knock");
    // Lanterna: clique de ligar/desligar
    public static final RegistryObject<SoundEvent> FLASHLIGHT_CLICK = reg("item.flashlight_click");
    // Lanterna: tremulação com bateria fraca
    public static final RegistryObject<SoundEvent> FLASHLIGHT_FLICKER = reg("item.flashlight_flicker");
}
