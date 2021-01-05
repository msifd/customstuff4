package cubex2.cs4.plugins.vanilla.block;

import cubex2.cs4.plugins.vanilla.ContentBlockDoor;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDoor extends ItemBlockContent {
    private final Block block;
    private final ContentBlockDoor content;

    public ItemDoor(Block block, ContentBlockDoor content) {
        super(content);

        this.block = block;
        this.content = content;
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.UP)
            return EnumActionResult.FAIL;

        if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(facing);
        }

        ItemStack itemstack = player.getHeldItem(hand);

        if (player.canPlayerEdit(pos, facing, itemstack) && block.canPlaceBlockAt(worldIn, pos)) {
            EnumFacing enumfacing = EnumFacing.fromAngle(player.rotationYaw);
            int i = enumfacing.getFrontOffsetX();
            int j = enumfacing.getFrontOffsetZ();
            boolean flag = i < 0 && hitZ < 0.5F || i > 0 && hitZ > 0.5F || j < 0 && hitX > 0.5F || j > 0 && hitX < 0.5F;
            net.minecraft.item.ItemDoor.placeDoor(worldIn, pos, enumfacing, block, flag);

            SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, player);
            worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }
}
