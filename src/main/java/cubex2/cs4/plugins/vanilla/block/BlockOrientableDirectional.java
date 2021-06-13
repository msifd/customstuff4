package cubex2.cs4.plugins.vanilla.block;

import cubex2.cs4.plugins.vanilla.ContentBlockOrientableDirectional;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockOrientableDirectional extends BlockOrientable
{
    public static final PropertyDirection FACING = BlockDirectional.FACING;

    public BlockOrientableDirectional(Material material, ContentBlockOrientableDirectional content)
    {
        super(material, content);

        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public boolean hasSubtypes() {
        return false;
    }

    @Override
    public int getSubtype(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
    public PropertyDirection getFacingProperty()
    {
        return FACING;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, getDirectionalFacingForPlacement(pos, facing, meta, placer));
    }
}
