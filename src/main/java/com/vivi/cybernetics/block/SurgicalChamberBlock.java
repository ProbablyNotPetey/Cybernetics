package com.vivi.cybernetics.block;

import com.vivi.cybernetics.block.entity.SurgicalChamberBlockEntity;
import com.vivi.cybernetics.capability.PlayerCyberwareProvider;
import com.vivi.cybernetics.menu.SurgicalChamberCyberwareMenu;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class SurgicalChamberBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;


    public SurgicalChamberBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, BedPart.FOOT));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SurgicalChamberBlockEntity(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(level.isClientSide) return null;

        return (lvl, pos, blockState, be) -> {
            if(be instanceof SurgicalChamberBlockEntity blockEntity) {
                blockEntity.tick();
            }
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, PART);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.getHorizontalDirection();
        BlockPos blockpos = pContext.getClickedPos();
        BlockPos blockpos1 = blockpos.relative(direction);
        Level level = pContext.getLevel();
        return level.getBlockState(blockpos1).canBeReplaced(pContext) && level.getWorldBorder().isWithinBounds(blockpos1) ? this.defaultBlockState().setValue(FACING, direction) : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof SurgicalChamberBlockEntity be) {
                be.getCapability(PlayerCyberwareProvider.PLAYER_CYBERWARE).ifPresent(cyberware -> {
                    NetworkHooks.openScreen((ServerPlayer) player, new SimpleMenuProvider(((pContainerId, pPlayerInventory, pPlayer) -> new SurgicalChamberCyberwareMenu(pContainerId, pPlayerInventory, cyberware)), Component.literal("Cyberware")), buf -> {
                        buf.writeBlockPos(pos);
                    });
                });
            } else {
                throw new IllegalStateException("Container provider missing!");
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.IGNORE;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pos, pState, pPlacer, pStack);
        if (!pLevel.isClientSide) {
            BlockPos pos2 = pos.relative(pState.getValue(FACING));
            pLevel.setBlock(pos2, pState.setValue(PART, BedPart.HEAD), 3);
            pLevel.blockUpdated(pos, Blocks.AIR);
            pState.updateNeighbourShapes(pLevel, pos, 3);
        }

    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide) {
            BedPart bedpart = pState.getValue(PART);
            BlockPos blockpos = pPos.relative(getNeighbourDirection(bedpart, pState.getValue(FACING)));
            BlockState blockstate = pLevel.getBlockState(blockpos);
            if (blockstate.is(this) && blockstate.getValue(PART) != bedpart) {
                pLevel.levelEvent(pPlayer, 2001, blockpos, Block.getId(blockstate));
                pLevel.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
            }
        }

        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    private static Direction getNeighbourDirection(BedPart pPart, Direction pDirection) {
        return pPart == BedPart.FOOT ? pDirection : pDirection.getOpposite();
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

}
