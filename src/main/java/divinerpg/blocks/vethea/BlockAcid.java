package divinerpg.blocks.vethea;

import divinerpg.blocks.base.BlockMod;
import divinerpg.util.DamageSources;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.*;

public class BlockAcid extends BlockMod {

    protected static final VoxelShape acidBoundingBox = Shapes.create(new AABB(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F));
    protected boolean decays;
    protected boolean poison;

    public BlockAcid(boolean decays) {
        this(decays, false);
    }

    public BlockAcid(boolean decays, boolean poison) {
        super(Block.Properties.of().strength(0.1F).randomTicks().sound(SoundType.SNOW));
        this.decays = decays;
        this.poison = poison;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter source, BlockPos pos, CollisionContext context) {
        return acidBoundingBox;
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if(entity instanceof Player) {
            entity.hurt(DamageSources.source(level, DamageSources.ACID), 3);
        }
    }

}
