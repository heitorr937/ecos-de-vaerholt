package com.vaerholt.echoes.dread;

import com.vaerholt.echoes.capability.DreadCapability;
import com.vaerholt.echoes.capability.IDread;
import com.vaerholt.echoes.entity.flayed.FlayedOneEntity;
import com.vaerholt.echoes.entity.whisperer.WhispererEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

/**
 * Roda a cada tick do servidor por jogador: calcula ganho/perda de Pavor a partir do
 * ambiente (luz, entidades próximas, isolamento) e aplica efeitos sutis quando cruza
 * limiares. Não existe barra visível de Pavor de propósito — o jogador só "sente" os
 * efeitos, nunca vê o número. Isso é intencional para reforçar a incerteza.
 */
public class DreadEventHandler {

    private static final float RAIO_DETECCAO = 24.0f;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer sp)) return;
        if (sp.level().getGameTime() % 20 != 0) return; // avalia 1x por segundo

        DreadCapability.get(sp).ifPresent(cap -> tickDread(sp, cap));
    }

    private void tickDread(ServerPlayer sp, IDread cap) {
        float delta = 0f;

        int light = sp.level().getMaxLocalRawBrightness(sp.blockPosition());
        if (light <= 2) delta += 0.35f;          // escuro total sustentado assusta aos poucos
        else if (light >= 12) delta -= 0.25f;    // luz forte acalma

        List<WhispererEntity> whisperers = sp.level().getEntitiesOfClass(
                WhispererEntity.class, sp.getBoundingBox().inflate(RAIO_DETECCAO));
        delta += whisperers.size() * 0.4f;

        List<FlayedOneEntity> flayed = sp.level().getEntitiesOfClass(
                FlayedOneEntity.class, sp.getBoundingBox().inflate(RAIO_DETECCAO));
        for (FlayedOneEntity f : flayed) {
            double dist = f.distanceTo(sp);
            delta += (float) Math.max(0, (RAIO_DETECCAO - dist) / RAIO_DETECCAO) * 2.5f;
        }

        if (cap.isHiding()) delta -= 0.6f; // esconder-se acalma ativamente

        cap.addDread(delta);
        applyThresholdEffects(sp, cap.getDread());
    }

    private void applyThresholdEffects(ServerPlayer sp, float dread) {
        if (dread >= 85f) {
            // Ruptura: cegueira pulsante curta, ocasional
            if (sp.getRandom().nextFloat() < 0.05f) {
                sp.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 30, 0, false, false));
            }
        } else if (dread >= 60f) {
            // Perturbado: leve escurecimento visual via Nausea curta e rara (usado como "distorção")
            if (sp.getRandom().nextFloat() < 0.01f) {
                sp.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 40, 0, false, false));
            }
        }
        // 30-60 (Inquieto) e abaixo de 30 (Calmo) não aplicam efeitos de status:
        // são tratados só por áudio no RandomScareManager, para não incomodar demais o jogador.
    }

    public static boolean isRuptured(LivingEntity entity) {
        if (!(entity instanceof ServerPlayer sp)) return false;
        return DreadCapability.get(sp).map(c -> c.getDread() >= 85f).orElse(false);
    }
}
