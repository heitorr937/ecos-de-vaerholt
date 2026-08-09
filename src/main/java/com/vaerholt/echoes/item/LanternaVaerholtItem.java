package com.vaerholt.echoes.item;

import com.vaerholt.echoes.registry.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

/**
 * Lanterna com estado próprio (ligada/desligada, nível de bateria, modo alto/baixo)
 * guardado em NBT do item. A luz de verdade é aplicada pelo FlashlightLightHandler no
 * client, que lê esse estado a cada tick. Aqui só cuidamos de input e consumo de bateria.
 */
public class LanternaVaerholtItem extends Item {

    public static final int BATERIA_MAX = 2400; // 2 minutos de uso contínuo no modo alto

    public LanternaVaerholtItem(Properties props) {
        super(props);
    }

    public static boolean isOn(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("on");
    }

    public static boolean isHighBeam(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("high");
    }

    public static int getBattery(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("battery")) tag.putInt("battery", BATERIA_MAX);
        return tag.getInt("battery");
    }

    public static void setBattery(ItemStack stack, int value) {
        stack.getOrCreateTag().putInt("battery", Math.max(0, Math.min(BATERIA_MAX, value)));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();

        if (player.isShiftKeyDown()) {
            // agachado + clique direito = alterna modo alto/baixo
            boolean high = !tag.getBoolean("high");
            tag.putBoolean("high", high);
            level.playSound(null, player.blockPosition(), ModSounds.FLASHLIGHT_CLICK.get(),
                    SoundSource.PLAYERS, 0.5f, high ? 1.3f : 0.9f);
        } else {
            boolean on = !tag.getBoolean("on");
            if (on && getBattery(stack) <= 0) {
                player.displayClientMessage(Component.literal("A bateria acabou."), true);
                return InteractionResultHolder.fail(stack);
            }
            tag.putBoolean("on", on);
            level.playSound(null, player.blockPosition(), ModSounds.FLASHLIGHT_CLICK.get(),
                    SoundSource.PLAYERS, 0.5f, on ? 1.0f : 0.8f);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    /** Chamado a cada tick pelo handler do lado servidor para consumir bateria enquanto ligada. */
    public static void tickBattery(ItemStack stack) {
        if (!isOn(stack)) return;
        int drain = isHighBeam(stack) ? 2 : 1;
        int battery = getBattery(stack) - drain;
        setBattery(stack, battery);
        if (battery <= 0) {
            stack.getOrCreateTag().putBoolean("on", false);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        int battery = getBattery(stack);
        int pct = (int) ((battery / (float) BATERIA_MAX) * 100);
        tooltip.add(Component.literal("Bateria: " + pct + "%"));
        tooltip.add(Component.literal(isHighBeam(stack) ? "Modo: Alto (atrai atenção)" : "Modo: Baixo (discreto)"));
        tooltip.add(Component.literal("Shift + clique direito: alterna modo").withStyle(s -> s.withItalic(true)));
    }
}
