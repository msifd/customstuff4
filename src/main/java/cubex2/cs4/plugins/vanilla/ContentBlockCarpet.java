package cubex2.cs4.plugins.vanilla;

import cubex2.cs4.plugins.vanilla.block.BlockFactory;
import cubex2.cs4.plugins.vanilla.block.ItemBlockWithSubtypes;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.Optional;

public class ContentBlockCarpet extends ContentBlockBaseWithSubtypes
{
    public ContentBlockCarpet()
    {
        opacity = Attribute.constant(0);
        isFullCube = Attribute.constant(false);
        isOpaqueCube = Attribute.constant(false);
    }

    @Override
    protected Block createBlockWithSubtypes()
    {
        return BlockFactory.createCarpetSubtype(this);
    }

    @Override
    protected Block createBlockWithoutSubtypes()
    {
        return BlockFactory.createCarpet(this);
    }

    @Override
    protected Optional<Item> createItem(boolean hasSubtypes)
    {
        return Optional.of(new ItemBlockWithSubtypes(block, this));
    }
}
