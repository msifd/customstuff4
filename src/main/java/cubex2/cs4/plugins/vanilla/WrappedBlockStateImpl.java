package cubex2.cs4.plugins.vanilla;

import cubex2.cs4.api.WrappedBlockState;
import cubex2.cs4.plugins.vanilla.block.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class WrappedBlockStateImpl implements WrappedBlockState
{
    private final ResourceLocation block;
    private final List<Tuple<String, String>> properties;

    public WrappedBlockStateImpl(ResourceLocation block, List<Tuple<String, String>> properties)
    {
        this.block = block;
        this.properties = properties;
    }

    @Override
    @SuppressWarnings("unchecked")
    public IBlockState createState()
    {
        Block block = getBlock();
        if (block != null)
        {
            IBlockState state = block.getDefaultState();

            for (Tuple<String, String> tuple : properties)
            {
                String name = tuple.getFirst();

                Optional<IProperty> prop = getProperty(state, name);
                if (prop.isPresent())
                {
                    IProperty property = prop.get();
                    state = state.withProperty(property, (Comparable) property.parseValue(tuple.getSecond()).get());
                }
            }

            return state;
        } else
        {
            return null;
        }
    }

    @Nullable
    private Block getBlock()
    {
        if (Block.REGISTRY.containsKey(block))
        {
            return Block.REGISTRY.getObject(block);
        } else if (BlockRegistry.INSTANCE.contains(block))
        {
            return BlockRegistry.INSTANCE.get(block);
        } else
        {
            return null;
        }
    }

    private Optional<IProperty> getProperty(IBlockState state, String name)
    {
        for (IProperty<?> key : state.getPropertyKeys())
        {
            if (key.getName().equals(name))
            {
                return Optional.of(key);
            }
        }

        return Optional.empty();
    }
}
