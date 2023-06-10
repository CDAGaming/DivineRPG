package divinerpg.blocks.arcana;

import divinerpg.blocks.base.BlockMod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.shapes.*;

public class BlockElevantium extends BlockMod {

    protected static final VoxelShape ELEVANTIUM = Shapes.or(Block.box(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D), Block.box(4.0D, 1.0D, 4.0D, 12.0D, 2.0D, 12.0D));

    public BlockElevantium() {
        super(Block.Properties.of().mapColor(MapColor.GRASS).requiresCorrectToolForDrops().strength(5F, 6F).pushReaction(PushReaction.DESTROY).randomTicks().jumpFactor(2).sound(SoundType.METAL));
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(world, pos, state, entity);
        entity.setDeltaMovement(entity.getDeltaMovement().x, 1, entity.getDeltaMovement().z);
    }

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return ELEVANTIUM;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_, CollisionContext p_220071_4_) {
        return Shapes.create(ELEVANTIUM.bounds().inflate(0.3D));
    }
}
