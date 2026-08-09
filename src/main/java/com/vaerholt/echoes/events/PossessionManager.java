package com.vaerholt.echoes.events;

import com.vaerholt.echoes.capability.DreadCapability;
import com.vaerholt.echoes.entity.possuido.PossuidoAnimalEntity;
import com.vaerholt.echoes.registry.ModEntities;
import com.vaerholt.echoes.registry.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.animal.Animal;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Random;

/**
 * MECÂNICA ÚNICA: possessão de animais.
 * Com Pavor alto, animais passivos (vaca, porco, galinha, ovelha) próximos do jogador têm
 * uma pequena chance, por segundo, de serem "possuídos": o animal para de se mover por um
 * instante (aviso visual: partículas escuras saindo dele + som de estática), e no lugar
 * dele nasce uma Presa Possuída hostil. A ideia é que NENHUM animal no mundo é seguro de
 * verdade perto de Vaerholt — reforça que o jogador nunca deveria se sentir "em segurança"
 * mesmo longe dos mobs do mod.
 */
public class PossessionManager {

    private final Random random = new Random();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer sp)) return;
        if (sp.level().getGameTime() % 100 != 0) return; // avalia a cada 5s
        if (!(sp.level() instanceof ServerLevel level)) return;

        DreadCapability.get(sp).ifPresent(cap -> {
            float dread = cap.getDread();
            if (dread < 45f) return;

            float chance = (dread - 45f) / 100f; // até ~0.55 no pavor máximo
            if (random.nextFloat() > chance * 0.3f) return;

            List<Animal> animais = level.getEntitiesOfClass(Animal.class, sp.getBoundingBox().inflate(20));
            if (animais.isEmpty()) return;

            Animal alvo = animais.get(random.nextInt(animais.size()));
            possuir(level, alvo);
        });
    }

    private void possuir(ServerLevel level, Animal alvo) {
        double x = alvo.getX(), y = alvo.getY(), z = alvo.getZ();

        level.sendParticles(ParticleTypes.SQUID_INK, x, y + 0.5, z, 15, 0.3, 0.4, 0.3, 0.02);
        level.playSound(null, alvo.blockPosition(), ModSounds.STATIC_GLITCH.get(), SoundSource.HOSTILE, 0.7f, 0.6f);

        alvo.discard();

        PossuidoAnimalEntity possuido = ModEntities.POSSUIDO.get().create(level);
        if (possuido == null) return;
        possuido.setPos(x, y, z);
        possuido.finalizeSpawn(level, level.getCurrentDifficultyAt(possuido.blockPosition()),
                net.minecraft.world.entity.MobSpawnType.TRIGGERED, null, null);
        level.addFreshEntity(possuido);
    }
}
