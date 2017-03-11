package cubex2.cs4.plugins.vanilla;

import cubex2.cs4.api.Content;
import cubex2.cs4.api.ContentHelper;
import cubex2.cs4.api.InitPhase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Optional;

public abstract class ContentBlockBase<T extends Block> implements Content
{
    String id;
    Material material = Material.GROUND;
    float slipperiness = 0.6f;

    protected transient T block;

    @Override
    public final void init(InitPhase phase, ContentHelper helper)
    {
        if (phase != InitPhase.INIT)
            return;

        block = createBlock();
        block.setUnlocalizedName(Loader.instance().activeModContainer().getModId() + "." + id);
        block.setRegistryName(id);
        block.slipperiness = slipperiness;

        initBlock();

        GameRegistry.register(block);

        createItem().ifPresent(this::initItem);
    }

    private void initItem(Item item)
    {
        item.setUnlocalizedName(Loader.instance().activeModContainer().getModId() + "." + id);
        item.setRegistryName(id);

        GameRegistry.register(item);
    }

    protected void initBlock()
    {

    }

    protected abstract T createBlock();

    protected Optional<Item> createItem()
    {
        return Optional.empty();
    }
}