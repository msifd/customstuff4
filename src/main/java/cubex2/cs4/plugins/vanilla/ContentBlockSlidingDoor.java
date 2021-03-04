package cubex2.cs4.plugins.vanilla;

import cubex2.cs4.plugins.vanilla.block.BlockFactory;
import cubex2.cs4.plugins.vanilla.block.ItemSlidingDoor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;

import java.util.Optional;

public class ContentBlockSlidingDoor extends ContentBlockBaseNoSubtypes {
    public boolean opensWithRedstone = true;
    public boolean opensWithHands = true;
    public double sideOffset = 0.8125;
    public double frontOffset = 0.1875;

    public ContentBlockSlidingDoor() {
        opacity = Attribute.constant(0);
        isFullCube = Attribute.constant(false);
        isOpaqueCube = Attribute.constant(false);
    }

    @Override
    protected Optional<Item> createItem() {
        return Optional.of(new ItemSlidingDoor(block, this));
    }

    @Override
    public Block createBlock() {
        return BlockFactory.createSlidingDoor(this);
    }

    @Override
    protected Optional<IStateMapper> createStateMapper() {
        return Optional.of(new StateMap.Builder().ignore(BlockDoor.POWERED).build());
    }
}
