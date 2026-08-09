package com.vaerholt.echoes.events;

import com.vaerholt.echoes.capability.DreadCapability;
import com.vaerholt.echoes.registry.ModEntities;
import com.vaerholt.echoes.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

/**
 * A cada poucos segundos, para cada jogador em área "assombrada" (Prata-Viva próxima ou
 * Pavor > 20), rola a chance de um evento ambiental assustador. A chance e a intensidade
 * escalam com o Pavor atual — quanto mais perturbado o jogador, mais frequentes e estranhos
 * os eventos ficam. Nenhum destes eventos causa dano: são puramente psicológicos.
 */
public class RandomScareManager {

    private final Random random = new Random();

    private static final String[] FRASES_FALSAS = {
            "algo respirou atrás de você.",
            "você não está sozinho.",
            "ele sabe onde você está.",
            "não olhe para trás.",
            "3... 2... 1..."
    };

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer sp)) return;
        if (sp.level().getGameTime() % 60 != 0) return; // avalia a cada 3s

        DreadCapability.get(sp).ifPresent(cap -> {
            float dread = cap.getDread();
            if (dread < 15f) return;

            float chance = 0.02f + (dread / 100f) * 0.10f; // 2% a 12% por avaliação
            if (random.nextFloat() <= chance) {
                fireRandomEvent(sp, dread);
            }

            // "O Enlutado" aparece sozinho, à distância, quando o Pavor está moderado/alto
            if (dread >= 40f && random.nextFloat() < 0.01f + (dread / 100f) * 0.02f) {
                spawnEnlutadoDistante(sp);
            }
        });
    }

    private void spawnEnlutadoDistante(ServerPlayer sp) {
        if (!(sp.level() instanceof ServerLevel level)) return;
        Vec3 look = sp.getLookAngle();
        double dist = 20 + random.nextDouble() * 12;
        double x = sp.getX() + look.x * dist + (random.nextDouble() - 0.5) * 6;
        double z = sp.getZ() + look.z * dist + (random.nextDouble() - 0.5) * 6;
        BlockPos pos = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,
                BlockPos.containing(x, sp.getY(), z));

        Mob enlutado = ModEntities.ENLUTADO.get().create(level);
        if (enlutado == null) return;
        enlutado.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        enlutado.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.TRIGGERED, null, null);
        level.addFreshEntity(enlutado);
    }

    private void fireRandomEvent(ServerPlayer sp, float dread) {
        int roll = random.nextInt(dread >= 60 ? 6 : 4);
        BlockPos p = sp.blockPosition();

        switch (roll) {
            case 0 -> // passos atrás do jogador
                sp.level().playSound(null, p.offset(offset(), 0, offset()),
                        ModSounds.FLAYED_STEP.get(), SoundSource.AMBIENT, 0.6f, 1.0f + random.nextFloat() * 0.3f);
            case 1 -> // batida distante em parede/porta
                sp.level().playSound(null, p.offset(offset() * 2, 0, offset() * 2),
                        ModSounds.KNOCK.get(), SoundSource.AMBIENT, 0.8f, 0.9f);
            case 2 -> // sussurro indistinto
                sp.level().playSound(null, p, ModSounds.WHISPER_LOOP.get(), SoundSource.AMBIENT, 0.4f, 1f);
            case 3 -> // grito distante
                sp.level().playSound(null, p.offset(offset() * 4, 0, offset() * 4),
                        ModSounds.DISTANT_SCREAM.get(), SoundSource.AMBIENT, 0.5f, 1f);
            case 4 -> { // mensagem "falsa" sutil (efeito visual apenas, não é chat real de outro jogador)
                sp.displayClientMessage(Component.literal(
                        FRASES_FALSAS[random.nextInt(FRASES_FALSAS.length)]).withStyle(s -> s.withItalic(true)), true);
                sp.level().playSound(null, p, ModSounds.STATIC_GLITCH.get(), SoundSource.MASTER, 0.3f, 1f);
            }
            case 5 -> { // item "some" do hotbar por instantes (puramente visual, sem perda real)
                sp.level().playSound(null, p, ModSounds.STATIC_GLITCH.get(), SoundSource.MASTER, 0.5f, 0.7f);
                // Implementação de fake-swap fica no lado cliente (HUD overlay), ver client/events.
            }
            default -> {}
        }
    }

    private int offset() {
        return random.nextBoolean() ? random.nextInt(6) + 2 : -(random.nextInt(6) + 2);
    }
}
