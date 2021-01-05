package cubex2.cs4.plugins.vanilla.block;

import cubex2.cs4.plugins.vanilla.ContentBlockDoor;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDoor extends net.minecraft.block.BlockDoor implements CSBlock<ContentBlockDoor> {
    private final ContentBlockDoor content;

    public BlockDoor(Material materialIn, ContentBlockDoor content) {
        super(materialIn);
        this.content = content;
    }

    @Override
    public String getLocalizedName() {
        return I18n.translateToLocal(content.getItemInstance().getUnlocalizedName() + ".name");
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return blockMaterial.getMaterialMapColor();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!content.opensWithHands)
            return false;

        BlockPos blockpos = state.getValue(HALF) == net.minecraft.block.BlockDoor.EnumDoorHalf.LOWER ? pos : pos.down();
        IBlockState iblockstate = pos.equals(blockpos) ? state : worldIn.getBlockState(blockpos);

        if (iblockstate.getBlock() != this) {
            return false;
        } else {
            state = iblockstate.cycleProperty(OPEN);
            worldIn.setBlockState(blockpos, state, 10);
            worldIn.markBlockRangeForRenderUpdate(blockpos, pos);
            worldIn.playEvent(playerIn, state.getValue(OPEN) ? getOpenSound() : getCloseSound(), pos, 0);
            return true;
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (content.opensWithRedstone) {
            super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return content.getItemInstance();
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(content.getItemInstance());
    }

    @Override
    public int getSubtype(IBlockState state) {
        return 0;
    }

    @Override
    public ContentBlockDoor getContent() {
        return content;
    }

    private int getCloseSound() {
        return content.opensWithHands ? 1011 : 1012;
    }

    private int getOpenSound() {
        return content.opensWithHands ? 1005 : 1006;
    }
}
