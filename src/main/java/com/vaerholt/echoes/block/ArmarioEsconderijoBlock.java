package com.vaerholt.echoes.block;

import com.vaerholt.echoes.capability.DreadCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.StateDefinition;

/**
 * Mecânica única: o jogador pode entrar neste bloco (clique direito) para "se esconder".
 * Enquanto escondido, o jogador não emite som/luz para a IA do Descarnado (ver
 * FlayedOneEntity#canDetect) e ganha uma pequena redução de Pavor por segundo — mas fica
 * cego (tela quase preta) e não pode ver o que se aproxima, criando tensão pura de espera.
 */
public class ArmarioEsconderijoBlock extends HorizontalDirectionalBlock {

    public ArmarioEsconderijoBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                  InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            DreadCapability.get(player).ifPresent(cap -> {
                boolean now = !cap.isHiding();
                cap.setHiding(now, now ? pos : null);
                player.displayClientMessage(
                        net.minecraft.network.chat.Component.literal(
                                now ? "Você prende a respiração..." : "Você sai do esconderijo."),
                        true);
            });
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
