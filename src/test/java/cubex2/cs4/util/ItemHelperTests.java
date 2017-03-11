package cubex2.cs4.util;

import com.google.common.collect.Maps;
import cubex2.cs4.plugins.vanilla.Attribute;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.Assert.*;

public class ItemHelperTests
{
    @BeforeClass
    public static void setup()
    {
        Bootstrap.register();
    }

    @Test
    public void test_findTabLabel()
    {
        Optional<CreativeTabs> tab = ItemHelper.findCreativeTab("tools");

        assertTrue(tab.isPresent());
        assertSame(CreativeTabs.TOOLS, tab.get());
    }

    @Test
    public void test_findTabLabel_invalid()
    {
        Optional<CreativeTabs> tab = ItemHelper.findCreativeTab("invalidtabname");

        assertFalse(tab.isPresent());
    }

    @Test
    public void test_createCreativeTabs()
    {
        HashMap<Integer, String> map = Maps.newHashMap();
        map.put(0, "tools");
        map.put(1, "invalidtab");
        map.put(2, "redstone");

        CreativeTabs[] tabs = ItemHelper.createCreativeTabs(Attribute.map(map), new int[] {0, 1, 2});

        assertEquals(2, tabs.length);
        assertSame(CreativeTabs.TOOLS, tabs[0]);
        assertSame(CreativeTabs.REDSTONE, tabs[1]);
    }

    @Test
    public void test_createCreativeTabs_removesDuplicates()
    {
        HashMap<Integer, String> map = Maps.newHashMap();
        map.put(0, "tools");
        map.put(1, "tools");

        CreativeTabs[] tabs = ItemHelper.createCreativeTabs(Attribute.map(map), new int[] {0, 1});

        assertEquals(1, tabs.length);
        assertSame(CreativeTabs.TOOLS, tabs[0]);
    }

    @Test
    public void test_createSubItems_differentTabs()
    {
        HashMap<Integer, String> map = Maps.newHashMap();
        map.put(0, "tools");
        map.put(1, "redstone");

        Attribute<String> tabLabels = Attribute.map(map);
        int[] subtypes = new int[] {0, 1};

        Item item = new Item();
        item.setHasSubtypes(true);

        NonNullList<ItemStack> subItems = ItemHelper.createSubItems(item, CreativeTabs.TOOLS, tabLabels, subtypes);

        assertSame(1, subItems.size());
        assertSame(0, subItems.get(0).getItemDamage());
    }

    @Test
    public void test_createSubItems_sameTabs()
    {
        HashMap<Integer, String> map = Maps.newHashMap();
        map.put(0, "tools");
        map.put(1, "tools");

        Attribute<String> tabLabels = Attribute.map(map);
        int[] subtypes = new int[] {0, 1};

        Item item = new Item();
        item.setHasSubtypes(true);

        NonNullList<ItemStack> subItems = ItemHelper.createSubItems(item, CreativeTabs.TOOLS, tabLabels, subtypes);

        assertSame(2, subItems.size());
        assertSame(0, subItems.get(0).getItemDamage());
        assertSame(1, subItems.get(1).getItemDamage());
    }

    @Test
    public void test_createSubItems_wrongTab()
    {
        HashMap<Integer, String> map = Maps.newHashMap();
        map.put(0, "tools");
        map.put(1, "tools");

        Attribute<String> tabLabels = Attribute.map(map);
        int[] subtypes = new int[] {0, 1};

        Item item = new Item();
        item.setHasSubtypes(true);

        NonNullList<ItemStack> subItems = ItemHelper.createSubItems(item, CreativeTabs.DECORATIONS, tabLabels, subtypes);

        assertSame(0, subItems.size());
    }

}