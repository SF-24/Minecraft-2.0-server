package net.minecraft.item.crafting;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

public class RecipesIngots
{
    private final Object[][] recipeItems = new Object[][] {{Blocks.gold_block, new ItemStack(Items.gold_ingot, 9)}, {Blocks.iron_block, new ItemStack(Items.iron_ingot, 9)}, {Blocks.diamond_block, new ItemStack(Items.diamond, 9)}, {Blocks.emerald_block, new ItemStack(Items.emerald, 9)}, {Blocks.lapis_block, new ItemStack(Items.dye, 9, EnumDyeColor.BLUE.getDyeDamage())}, {Blocks.redstone_block, new ItemStack(Items.redstone, 9)}, {Blocks.coal_block, new ItemStack(Items.coal, 9, 0)}, {Blocks.hay_block, new ItemStack(Items.wheat, 9)}, {Blocks.slime_block, new ItemStack(Items.slime_ball, 9)}, {Blocks.amethyst_block, new ItemStack(Items.amethyst, 9)}, {Blocks.steel_block, new ItemStack(Items.steel_ingot, 9)}, {Blocks.ruby_block, new ItemStack(Items.ruby, 9)},
        {Blocks.rail_block, new ItemStack(Blocks.rail, 9)},
        {Blocks.activator_rail_block, new ItemStack(Blocks.activator_rail, 9)},
        {Blocks.detector_rail_block, new ItemStack(Blocks.detector_rail, 9)},
        {Blocks.gold_rail_block, new ItemStack(Blocks.golden_rail, 9)}
    };

    /**
     * Adds the ingot recipes to the CraftingManager.
     */
    public void addRecipes(CraftingManager p_77590_1_)
    {
        for (int i = 0; i < this.recipeItems.length; ++i)
        {
            Block block = (Block)this.recipeItems[i][0];
            ItemStack itemstack = (ItemStack)this.recipeItems[i][1];
            p_77590_1_.addRecipe(new ItemStack(block), "###", "###", "###", '#', itemstack);
            p_77590_1_.addRecipe(itemstack, "#", '#', block);
        }

        p_77590_1_.addRecipe(new ItemStack(Items.gold_ingot), "###", "###", "###", '#', Items.gold_nugget);
        p_77590_1_.addRecipe(new ItemStack(Items.gold_nugget, 9), "#", '#', Items.gold_ingot);
    }
}
