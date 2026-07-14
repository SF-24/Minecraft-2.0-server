package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemAnvilBlock extends ItemMultiTexture
{
    public ItemAnvilBlock(Block block)
    {
        super(block, block, new String[] {
                "intact",           // 0
                "unused",           // 1
                "slightlyDamaged",  // 2
                "unused",           // 3
                "veryDamaged",      // 4
                "unused",           // 5
                "unused",           // 6
                "unused",           // 7
                "diamond_intact",   // 8
                "unused",           // 9
                "diamond_slightlyDamaged", // 10
                "unused",           // 11
                "diamond_veryDamaged"      // 12
        });
//        super(block, block, new String[] {"intact", "slightlyDamaged", "veryDamaged"});
    }

    /**
     * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
     * placed as a Block (mostly used with ItemBlocks).
     */
    public int getMetadata(int damage)
    {
        return damage;
    }
//    public int getMetadata(int damage)
//    {
//        return damage << 2;
//    }
}
