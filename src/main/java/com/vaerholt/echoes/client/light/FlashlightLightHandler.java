package com.vaerholt.echoes.client.light;

import com.vaerholt.echoes.item.LanternaVaerholtItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

/**
 * IMPORTANTE — leia antes de "corrigir" isso achando que está errado:
 * O Forge 1.20.1 não expõe uma API nativa de luz pontual dinâmica (ao contrário de
 * shaders/Fabric API). A técnica padrão usada por mods de iluminação dinâmica no Forge
 * (ex.: "Dynamic Lights") é reaproveitar o bloco vanilla `minecraft:light` — que é
 * completamente invisível, não sólido e possui 16 níveis de emissão — colocando-o
 * temporariamente nas posições à frente do jogador e removendo/movendo a cada tick.
 * Isso dá luz de verdade (afeta o light engine, sombra em mobs, etc.), não uma tocha
 * disfarçada. É mais "caro" que uma tocha, então o raio é propositalmente curto (raio
 * efetivo de ~5-6 blocos no modo baixo, ~8 no alto) para parecer uma lanterna de mão real,
 * não um farol.
 *
 * A lista de posições iluminadas por jogador é rastreada para poder desfazer exatamente
 * o que foi colocado, sem tocar em blocos que já existiam no mundo.
 */
public class FlashlightLightHandler {

    private final Map<UUID, List<BlockPos>> litPositions = new HashMap<>();

    @SubscribeEvent
    public void onServerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer sp)) return;
        if (sp.level().getGameTime() % 2 != 0) return; // atualiza a cada 2 ticks (suficiente e mais leve)

        ServerLevel level = sp.serverLevel();
        UUID id = sp.getUUID();

        ItemStack held = findFlashlight(sp);
        clearOld(level, id);

        if (held == null) return;

        LanternaVaerholtItem.tickBattery(held);
        if (!LanternaVaerholtItem.isOn(held)) return;

        boolean high = LanternaVaerholtItem.isHighBeam(held);
        int range = high ? 8 : 5;
        int baseLevel = high ? 13 : 8;

        // tremulação leve e realista: nunca 100% estável, como uma lanterna de pilha de verdade
        int flicker = level.getRandom().nextInt(3) - 1;
        int lightLevel = Math.max(4, Math.min(15, baseLevel + flicker));

        List<BlockPos> positions = traceCone(sp, range);
        List<BlockPos> placed = new ArrayList<>();
        for (BlockPos pos : positions) {
            BlockState existing = level.getBlockState(pos);
            if (existing.canBeReplaced() || existing.is(Blocks.LIGHT)) {
                level.setBlock(pos, Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, lightLevel), 3);
                placed.add(pos);
            }
        }
        litPositions.put(id, placed);

        // poeira flutuando no feixe — dá a sensação de luz "física" atravessando o ar,
        // reforçando a leitura de que é uma lanterna de verdade e não luz de bloco
        if (!placed.isEmpty() && level.getRandom().nextFloat() < 0.35f) {
            BlockPos p = placed.get(level.getRandom().nextInt(placed.size()));
            level.sendParticles(net.minecraft.core.particles.ParticleTypes.MYCELIUM,
                    p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5, 1, 0.15, 0.15, 0.15, 0.0);
        }
    }

    private void clearOld(ServerLevel level, UUID id) {
        List<BlockPos> old = litPositions.remove(id);
        if (old == null) return;
        for (BlockPos pos : old) {
            if (level.getBlockState(pos).is(Blocks.LIGHT)) {
                level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    private ItemStack findFlashlight(ServerPlayer sp) {
        ItemStack main = sp.getMainHandItem();
        if (main.getItem() instanceof LanternaVaerholtItem) return main;
        ItemStack off = sp.getOffhandItem();
        if (off.getItem() instanceof LanternaVaerholtItem) return off;
        return null;
    }

    /** Traça um cone simples de blocos à frente do jogador, poucos por linha para performance. */
    private List<BlockPos> traceCone(ServerPlayer sp, int range) {
        List<BlockPos> out = new ArrayList<>();
        Vec3i look = new Vec3i(
                (int) Math.round(sp.getLookAngle().x),
                (int) Math.round(sp.getLookAngle().y),
                (int) Math.round(sp.getLookAngle().z));
        BlockPos eye = BlockPos.containing(sp.getEyePosition());

        BlockPos cursor = eye;
        for (int i = 1; i <= range; i++) {
            cursor = cursor.offset(
                    (int) Math.signum(sp.getLookAngle().x),
                    Math.abs(look.getY()) > 0 ? (int) Math.signum(sp.getLookAngle().y) : 0,
                    (int) Math.signum(sp.getLookAngle().z));
            out.add(cursor);
            if (i % 2 == 0) {
                out.add(cursor.above());
                out.add(cursor.below());
            }
            if (!sp.level().getBlockState(cursor).canBeReplaced() && !sp.level().getBlockState(cursor).is(Blocks.LIGHT)) {
                break; // luz é bloqueada por paredes sólidas, como uma lanterna de verdade
            }
        }
        return out;
    }
}
