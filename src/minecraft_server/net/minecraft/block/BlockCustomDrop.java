package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockCustomDrop extends Block
{
    int dropItemId;
    public BlockCustomDrop(int dropItemId, Material material) {
        super(material);
        this.dropItemId = dropItemId;
    }

    /**
    * Spawns this Block's drops into the World as EntityItems.
    */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemById(dropItemId);
    }
}
