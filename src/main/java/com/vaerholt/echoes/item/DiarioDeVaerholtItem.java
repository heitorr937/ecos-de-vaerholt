package com.vaerholt.echoes.item;

import com.vaerholt.echoes.capability.DreadCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Random;

/**
 * Páginas de diário espalhadas pelas ruínas de Vaerholt. Ler reduz um pouco o Pavor
 * (a narrativa acalma por reconhecimento), mas tem uma chance de assustar logo em seguida
 * — ler demais sobre o que aconteceu ali tem um preço. Cada item guarda um índice de
 * página fixo (definido ao ser gerado no loot), para que a história seja lida em ordem
 * se o jogador for encontrando as páginas certas.
 */
public class DiarioDeVaerholtItem extends Item {

    public static final List<String> PAGINAS = List.of(
        "Página 1 — Chegamos há três semanas. O veio é mais rico do que qualquer relatório dizia. Prata que brilha sem tocha nenhuma por perto. O capataz chama de 'Prata-Viva'.",
        "Página 2 — Cavamos direto para dentro de uma câmara que não devia existir naquela profundidade. O ar lá dentro é mais frio do que devia. Ninguém quis entrar sozinho.",
        "Página 3 — À noite ouvimos vozes vindas da rocha. No começo pensamos ser eco. Depois reconhecemos as palavras: eram as últimas coisas que dissemos às nossas famílias antes de descer.",
        "Página 4 — Thomas não respondeu na contagem esta manhã. Encontraram as ferramentas dele arrumadas, perfeitamente, na Câmara. Ele nunca foi organizado assim.",
        "Página 5 — As vozes pararam de repetir o que dissemos. Agora perguntam coisas. Perguntam nomes. Perguntam onde moramos.",
        "Página 6 — Não vou mais descer. Os que descem voltam diferentes, ou não voltam. A vila inteira ouve isso à noite agora, não só os mineiros.",
        "Página 7 (última, letra trêmula) — Se alguém encontrar isto: não tragam luz forte demais. Ele sente o calor dela de longe. E se ouvirem sussurrando o que vocês disseram ontem, não respondam. Nunca respondam."
    );

    public DiarioDeVaerholtItem(Properties props) {
        super(props);
    }

    public static ItemStack ofPagina(int index) {
        ItemStack stack = new ItemStack(com.vaerholt.echoes.registry.ModItems.DIARIO_DE_VAERHOLT.get());
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("pagina", Math.max(0, Math.min(PAGINAS.size() - 1, index)));
        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        int pagina = stack.getOrCreateTag().getInt("pagina");
        String texto = PAGINAS.get(Math.min(pagina, PAGINAS.size() - 1));

        if (!level.isClientSide) {
            player.sendSystemMessage(Component.literal(texto));
            DreadCapability.get(player).ifPresent(cap -> {
                cap.addDread(-6f);
                if (new Random().nextFloat() < 0.15f) {
                    cap.addDread(18f); // o preço de ler: pico súbito de pavor
                }
            });
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
