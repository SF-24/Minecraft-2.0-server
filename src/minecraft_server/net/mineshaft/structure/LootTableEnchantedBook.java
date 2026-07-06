package net.mineshaft.structure;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;

import java.util.Random;

public class LootTableEnchantedBook {

    public static WeightedRandomChestContent getBasicEnchantedBook(Random rand) {
        return Items.enchanted_book.getRandom(rand);
    }

    public static WeightedRandomChestContent getEnchantedBook(Random rand, EnumLootSource enumLootSource) {
        if(enumLootSource.getNativeEnchantments().isEmpty() ||rand.nextInt(2)==0) {
            // Get random enchant
            return Items.enchanted_book.getRandom(rand);
        }
        // Get a native enchant
        ItemStack itemstack = new ItemStack(Items.book, 1, 0);

        Enchantment enchantment = enumLootSource.getNativeEnchantments().get(rand.nextInt(enumLootSource.getNativeEnchantments().size()));
        int level = rand.nextInt(enumLootSource.hasSuperEnchants()?enchantment.getMaxExtraLevel():enchantment.getMaxLevel())+1;
        itemstack.addEnchantment(enchantment,level);
        return new WeightedRandomChestContent(itemstack, 1, 1, 1);
    }
}
