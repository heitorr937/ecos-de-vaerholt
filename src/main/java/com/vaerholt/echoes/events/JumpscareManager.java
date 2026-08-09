package com.vaerholt.echoes.events;

import com.vaerholt.echoes.capability.DreadCapability;
import com.vaerholt.echoes.registry.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

/**
 * O susto "de verdade": raro, curto, alto impacto — bem diferente dos eventos ambientais
 * do RandomScareManager (que são sutis). Só dispara em Pavor muito alto (ruptura) e nunca
 * mais de uma vez a cada ~2 minutos por jogador, para não virar cansativo/previsível.
 * Efeito: um flash de cegueira de 2-4 ticks (curtíssimo, só o suficiente para simular um
 * "piscar" de sobressalto) sincronizado com o rugido tocado bem próximo/alto — sem mob
 * real aparecendo (evita abuso de hitbox), então é 100% seguro para o jogador mas ainda
 * assusta de verdade pelo áudio+flash sincronizados.
 */
public class JumpscareManager {

    private final Random random = new Random();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer sp)) return;
        if (sp.level().getGameTime() % 40 != 0) return; // avalia a cada 2s
        if (!(sp.level() instanceof ServerLevel level)) return;

        DreadCapability.get(sp).ifPresent(cap -> {
            if (cap.getDread() < 88f) return;
            if (sp.getPersistentData().getLong("echoes_last_jumpscare") > level.getGameTime() - 2400) return; // cooldown 2min

            if (random.nextFloat() > 0.05f) return; // ~5% a cada 2s quando em ruptura extrema

            sp.getPersistentData().putLong("echoes_last_jumpscare", level.getGameTime());
            sp.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 3, 0, false, false));
            level.playSound(null, sp.blockPosition(), ModSounds.FLAYED_ROAR.get(), SoundSource.HOSTILE, 1.6f, 0.7f);
            cap.addDread(-15f); // o susto "quebra a tensão" e reduz o pavor depois, como um pico de adrenalina
        });
    }
}
