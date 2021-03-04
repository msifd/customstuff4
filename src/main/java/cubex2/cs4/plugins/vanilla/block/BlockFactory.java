package cubex2.cs4.plugins.vanilla.block;

import cubex2.cs4.mixin.Mixin;
import cubex2.cs4.plugins.vanilla.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Field;
import java.util.stream.Stream;


public class BlockFactory
{
    private static Class<? extends Block> simpleClass;
    private static Class<? extends Block> simpleSubtypeClass;
    private static Class<? extends Block> directionalClass;
    private static Class<? extends Block> directionalSubtypeClass;
    private static Class<? extends Block> verticalClass;
    private static Class<? extends Block> verticalSubtypeClass;
    private static Class<? extends Block> horizontalClass;
    private static Class<? extends Block> horizontalSubtypeClass;
    private static Class<? extends Block> fenceClass;
    private static Class<? extends Block> fenceSubtypeClass;
    private static Class<? extends Block> stairsClass;
    private static Class<? extends Block> slabClass;
    private static Class<? extends Block> slabSubtypeClass;
    private static Class<? extends Block> fluidClass;
    private static Class<? extends Block> carpetClass;
    private static Class<? extends Block> carpetSubtypeClass;
    private static Class<? extends Block> snowClass;
    private static Class<? extends Block> cropsClass;
    private static Class<? extends Block> fenceGateClass;
    private static Class<? extends Block> wallClass;
    private static Class<? extends Block> wallSubtypeClass;
    private static Class<? extends Block> doorClass;
    private static Class<? extends Block> trapDoorClass;
    private static Class<? extends Block> torchClass;
    private static Class<? extends Block> buttonClass;
    private static Class<? extends Block> paneClass;
    private static Class<? extends Block> paneSubtypeClass;
    private static Class<? extends Block> pressurePlateClass;

    public static Block createSimple(ContentBlockSimple content)
    {
        return newInstance(simpleClass, content);
    }

    public static Block createSimpleSubtype(ContentBlockSimple content)
    {
        return newInstance(simpleSubtypeClass, content);
    }

    public static Block createDirectional(ContentBlockOrientable content)
    {
        return newInstance(directionalClass, content);
    }

    public static Block createDirectionalSubtype(ContentBlockOrientable content)
    {
        return newInstance(directionalSubtypeClass, content);
    }

    public static Block createVertical(ContentBlockOrientable content)
    {
        return newInstance(verticalClass, content);
    }

    public static Block createVerticalSubtype(ContentBlockOrientable content)
    {
        return newInstance(verticalSubtypeClass, content);
    }

    public static Block createHorizontal(ContentBlockOrientable content)
    {
        return newInstance(horizontalClass, content);
    }

    public static Block createHorizontalSubtype(ContentBlockOrientable content)
    {
        return newInstance(horizontalSubtypeClass, content);
    }

    public static Block createFence(ContentBlockFence content)
    {
        return newInstance(fenceClass, content);
    }

    public static Block createFenceSubtype(ContentBlockFence content)
    {
        return newInstance(fenceSubtypeClass, content);
    }

    public static Block createStairs(ContentBlockStairs content)
    {
        return newInstance(stairsClass, content);
    }

    public static Block createSlab(ContentBlockSlab content)
    {
        return newInstance(slabClass, content);
    }

    public static Block createSlabSubtype(ContentBlockSlab content)
    {
        return newInstance(slabSubtypeClass, content);
    }

    public static Block createFluid(ContentBlockFluid content)
    {
        return newInstance(fluidClass, content);
    }

    public static Block createCarpet(ContentBlockCarpet content)
    {
        return newInstance(carpetClass, content);
    }

    public static Block createCarpetSubtype(ContentBlockCarpet content)
    {
        return newInstance(carpetSubtypeClass, content);
    }

    public static Block createCrops(ContentBlockCrops content)
    {
        return newInstance(cropsClass, content);
    }

    public static Block createSnow(ContentBlockSnow content)
    {
        final Block block = newInstance(snowClass, content);
        setMaterial(block, content.material);
        return block;
    }

    public static Block createFenceGate(ContentBlockFenceGate content)
    {
        return newInstance(fenceGateClass, content);
    }

    public static Block createWall(ContentBlockWall content)
    {
        return newInstance(wallClass, content);
    }

    public static Block createWallSubtype(ContentBlockWall content)
    {
        return newInstance(wallSubtypeClass, content);
    }

    public static Block createDoor(ContentBlockDoor content)
    {
        return newInstance(doorClass, content);
    }

    public static Block createTrapDoor(ContentBlockTrapDoor content)
    {
        return newInstance(trapDoorClass, content);
    }

    public static Block createTorch(ContentBlockTorch content)
    {
        return newInstance(torchClass, content);
    }

    public static Block createButton(ContentBlockButton content)
    {
        return newInstance(buttonClass, content);
    }

    public static Block createPane(ContentBlockPane content)
    {
        return newInstance(paneClass, content);
    }

    public static Block createPaneSubtype(ContentBlockPane content)
    {
        return newInstance(paneSubtypeClass, content);
    }

    public static Block createPressurePlate(ContentBlockPressurePlate content)
    {
        return newInstance(pressurePlateClass, content);
    }

    private static <T extends ContentBlockBase> Block newInstance(Class<? extends Block> blockClass, T content)
    {
        try
        {
            return blockClass.getConstructor(Material.class, content.getClass()).newInstance(content.material, content);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private static void setMaterial(Block block, Material material) {
        final Field materialField = Stream.of(Block.class.getDeclaredFields())
                .filter(f -> f.getType() == Material.class)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("cant find Block material field"));
        try {
            materialField.setAccessible(true);
            materialField.set(block, material);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static
    {
        simpleClass = createClass(BlockSimple.class, BlockMixin.class);
        simpleSubtypeClass = createClass(BlockSimpleWithSubtypes.class, BlockMixin.class);
        directionalClass = createClass(BlockOrientableDirectional.class, BlockMixin.class);
        directionalSubtypeClass = createClass(BlockOrientableDirectionalWithSubtypes.class, BlockMixin.class);
        verticalClass = createClass(BlockOrientableVertical.class, BlockMixin.class);
        verticalSubtypeClass = createClass(BlockOrientableVerticalWithSubtypes.class, BlockMixin.class);
        horizontalClass = createClass(BlockOrientableHorizontal.class, BlockMixin.class);
        horizontalSubtypeClass = createClass(BlockOrientableHorizontalWithSubtypes.class, BlockMixin.class);
        fenceClass = createClass(BlockFence.class, BlockMixin.class);
        fenceSubtypeClass = createClass(BlockFenceWithSubtypes.class, BlockMixin.class);
        stairsClass = createClass(BlockStairs.class, BlockMixin.class);
        slabClass = createClass(BlockSlab.class, BlockMixin.class);
        slabSubtypeClass = createClass(BlockSlabWithSubtypes.class, BlockMixin.class);
        fluidClass = createClass(BlockFluid.class, BlockMixin.class);
        carpetClass = createClass(BlockCarpet.class, BlockMixin.class);
        carpetSubtypeClass = createClass(BlockCarpetWithSubtypes.class, BlockMixin.class);
        snowClass = createClass(BlockSnow.class, BlockMixin.class);
        cropsClass = createClass(BlockCrops.class, BlockMixin.class);
        fenceGateClass = createClass(BlockFenceGate.class, BlockMixin.class);
        wallClass = createClass(BlockWall.class, BlockMixin.class);
        wallSubtypeClass = createClass(BlockWallWithSubtypes.class, BlockMixin.class);
        doorClass = createClass(BlockDoor.class, BlockMixin.class);
        trapDoorClass = createClass(BlockTrapDoor.class, BlockMixin.class);
        torchClass = createClass(BlockTorch.class, BlockMixin.class);
        buttonClass = createClass(BlockButton.class, BlockMixin.class);
        paneClass = createClass(BlockPane.class, BlockMixin.class);
        paneSubtypeClass = createClass(BlockPaneWithSubtypes.class, BlockMixin.class);
        pressurePlateClass = createClass(BlockPressurePlate.class, BlockMixin.class);
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Block> createClass(Class<?> baseClass, Class<?>... mixins)
    {
        return (Class<? extends Block>) Mixin.create(baseClass.getName().replace('.', '/') + "_created", BlockFactory::checkForPublicConstructor, baseClass, mixins);
    }

    private static void checkForPublicConstructor(ClassNode node)
    {
        for (MethodNode method : node.methods)
        {
            if (method.name.equals("<init>") && (method.access & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC)
                return;
        }

        throw new RuntimeException(" Block Class " + node.name + " has no public constructor");
    }
}
