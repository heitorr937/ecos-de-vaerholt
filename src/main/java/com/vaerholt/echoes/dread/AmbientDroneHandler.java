package com.vaerholt.echoes.dread;

import com.vaerholt.echoes.capability.DreadCapability;
import com.vaerholt.echoes.registry.ModSounds;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Toca o drone ambiente (grave, contínuo) periodicamente quando o jogador está com
 * Pavor moderado ou alto, para reforçar a atmosfera sem depender só de eventos pontuais.
 * Reenviado a cada ~18s enquanto a condição se mantém (o som em si dura 20s, então
 * o loop fica quase contínuo sem precisar de lógica de "sound instance" no cliente).
 */
public class AmbientDroneHandler {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer sp)) return;
        if (sp.level().getGameTime() % 360 != 0) return; // a cada 18s

        DreadCapability.get(sp).ifPresent(cap -> {
            if (cap.getDread() < 25f) return;
            float volume = Math.min(1f, cap.getDread() / 100f + 0.2f);
            sp.level().playSound(null, sp.blockPosition(), ModSounds.AMBIENT_DRONE.get(),
                    SoundSource.AMBIENT, volume, 1.0f);
        });
    }
}
