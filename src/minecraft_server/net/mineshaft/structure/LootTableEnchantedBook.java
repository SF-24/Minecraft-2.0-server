package net.mineshaft.structure;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootTableEnchantedBook {

    public static WeightedRandomChestContent getBasicEnchantedBook(Random rand) {
        return Items.enchanted_book.getRandom(rand);
    }

    public static WeightedRandomChestContent getEnchantedBook(Random rand, EnumLootSource enumLootSource) {
        return getEnchantedBook(rand,enumLootSource,1,1);
    }

    public static List<WeightedRandomChestContent> getEnchantedBooks(Random rand, EnumLootSource enumLootSource, int minLevel, int weight, int amount) {
        List<WeightedRandomChestContent> list = new ArrayList<>();
        for(int i = 0; i < amount; i++) {
            list.add(getEnchantedBook(rand,enumLootSource,minLevel,weight));
        }
        return list;
    }

    public static WeightedRandomChestContent getEnchantedBook(Random rand, EnumLootSource enumLootSource, int minLevel, int weight) {
        if (enumLootSource.getNativeEnchantments().isEmpty() || rand.nextInt(3) == 0) {
            // Get random enchant
            return Items.enchanted_book.getRandom(rand, 1, 1, weight);
        }
        // Get a native enchant
        ItemStack itemstack = new ItemStack(Items.book, 1, 0);

        Enchantment enchantment = enumLootSource.getRandomEnchantment(rand);
        System.out.println("Enchant: " + enchantment);
        int level = rand.nextInt(enumLootSource.hasSuperEnchants() ? enchantment.getMaxExtraLevel() : enchantment.getMaxLevel()) + 1;

        itemstack.setItem(Items.enchanted_book);
        itemstack.addEnchantment(enchantment, level);
        return new WeightedRandomChestContent(itemstack, 1, 1, weight);
    }
}
