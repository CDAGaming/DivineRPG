package divinerpg.blocks.base;

import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockModGlass extends AbstractGlassBlock {
    public BlockModGlass() {
        super(BlockBehaviour.Properties.of().strength(0.3F).sound(SoundType.GLASS).noOcclusion());
    }

}
