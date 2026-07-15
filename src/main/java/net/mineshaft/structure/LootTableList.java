package net.mineshaft.structure;

import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.WeightedRandomChestContent;

import java.util.List;
import java.util.Random;

public class LootTableList {

    public static class LootOverworld {
        // Rarely has notch apples
        public static final List<WeightedRandomChestContent> DUNGEON = Lists.newArrayList(new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 10), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 4, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 1, 10), new WeightedRandomChestContent(Items.wheat, 0, 1, 4, 10), new WeightedRandomChestContent(Items.gunpowder, 0, 1, 4, 10), new WeightedRandomChestContent(Items.string, 0, 1, 4, 10), new WeightedRandomChestContent(Items.bucket, 0, 1, 1, 10), new WeightedRandomChestContent(Items.golden_apple, 1, 1, 1, 1), new WeightedRandomChestContent(Items.redstone, 0, 1, 4, 10), new WeightedRandomChestContent(Items.record_13, 0, 1, 1, 4), new WeightedRandomChestContent(Items.record_cat, 0, 1, 1, 4), new WeightedRandomChestContent(Items.name_tag, 0, 1, 1, 10), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 2), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 5), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1));

        public static final List<WeightedRandomChestContent> IGLOO = Lists.newArrayList(
                new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 0),
                new WeightedRandomChestContent(Items.coal, 0, 1, 4, 5),
                new WeightedRandomChestContent(Items.apple, 0, 1, 3, 5),
                new WeightedRandomChestContent(Items.wheat_seeds, 0, 2, 3, 3),
                new WeightedRandomChestContent(Items.gold_nugget, 0, 1, 3, 3),
                new WeightedRandomChestContent(Items.stone_axe, 0, 1, 1, 1),
                new WeightedRandomChestContent(Items.emerald, 0, 1, 1, 1)
        );

        public static final List<WeightedRandomChestContent> JUNGLE_PYRAMID = Lists.newArrayList(
                new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 1), // Rarer, was 3
                new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 5), // Rarer was 10
                new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 12), // Rarer was 15
                new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2),
                new WeightedRandomChestContent(Items.bone, 0, 4, 6, 16), // Rarer
                new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 12), // Rarer
                new WeightedRandomChestContent(Items.string, 0, 3, 10, 5), // New
                new WeightedRandomChestContent(Items.leather, 0, 3, 10, 5), // New
                new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3),
                new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1),
                new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1),
                new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)
        );


        public static final List<WeightedRandomChestContent> DESERT_PYRAMID = Lists.newArrayList(
                new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 1), // Rarer, was 3
                new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 5), // Rarer, was 10
                new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 12), // Rarer, was 15
                new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 1),
                new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2),
                new WeightedRandomChestContent(Items.bone, 0, 4, 6, 16), // Slightly rarer, was 20
                new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 12), // Also rarer, was 16
                new WeightedRandomChestContent(Items.gunpowder, 0, 3, 7, 5), // New
                new WeightedRandomChestContent(Items.leather, 0, 3, 8, 5), // New
                new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3),
                new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1),
                new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1),
                new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)
        );
    }

    public static class LootNether {
        public static final List<WeightedRandomChestContent> NETHER_FORTRESS = Lists.newArrayList(
                new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 5),
                new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 5),
                /* new WeightedRandomChestContent(Items.nether_ash, 0, 1, 5, 5),*/
                new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 15),
                new WeightedRandomChestContent(Items.golden_sword, 0, 1, 1, 2),
                new WeightedRandomChestContent(Items.golden_helmet, 0, 1, 1, 2),
                new WeightedRandomChestContent(Items.golden_chestplate, 0, 1, 1, 2),
                new WeightedRandomChestContent(Items.golden_leggings, 0, 1, 1, 2),
                new WeightedRandomChestContent(Items.golden_boots, 0, 1, 1, 2),
                new WeightedRandomChestContent(Items.flint_and_steel, 0, 1, 1, 5),
                new WeightedRandomChestContent(Items.nether_wart, 0, 3, 7, 5),
                new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 10),
                new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 8),
                new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 5),
                new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 3),
                new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.obsidian), 0, 2, 4, 2));

        public static final List<WeightedRandomChestContent> NETHER_TOWER_GENERIC = Lists.newArrayList(
                // Loot
                new WeightedRandomChestContent(Items.nether_ash, 0, 1, 3, 3),
                new WeightedRandomChestContent(Items.steel_nugget, 0, 1, 1, 2),

                // Utilities used here
                new WeightedRandomChestContent(Items.arrow, 0, 2, 9, 3),
                new WeightedRandomChestContent(Items.bow, 0, 1, 1, 1),
                new WeightedRandomChestContent(Items.golden_pickaxe, 0, 1, 1, 2),
                new WeightedRandomChestContent(Items.golden_shovel, 0, 1, 1, 2),
                new WeightedRandomChestContent(Items.mushroom_stew, 0, 1, 3, 2),

                // Decorative and other useful items/
                new WeightedRandomChestContent(Items.flint_and_steel, 0, 1, 1, 2),
                new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.obsidian), 0, 2, 4, 2));

        public static final List<WeightedRandomChestContent> NETHER_TOWER_ROOF = Lists.newArrayList(
                // Loot - more ash on the roof.
                new WeightedRandomChestContent(Items.nether_ash, 0, 1, 3, 5),
                new WeightedRandomChestContent(Items.steel_nugget, 0, 1, 1, 2),

                // Utilities used here
                new WeightedRandomChestContent(Items.arrow, 0, 2, 9, 10),
                new WeightedRandomChestContent(Items.bow, 0, 1, 1, 3),

                // Decorative and other useful items/
                new WeightedRandomChestContent(Items.flint_and_steel, 0, 1, 1, 3),
                new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.obsidian), 0, 2, 4, 2));


        public static final List<WeightedRandomChestContent> NETHER_BASEMENT = Lists.newArrayList(
                // Loot
                new WeightedRandomChestContent(Items.nether_ash, 0, 2, 3, 3),
                new WeightedRandomChestContent(Items.steel_nugget, 0, 1, 2, 2),

                // Utilities used here
                new WeightedRandomChestContent(Items.golden_pickaxe, 0, 1, 1, 2),
                new WeightedRandomChestContent(Items.golden_shovel, 0, 1, 1, 2),

                // Decorative and other useful items/
                new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.netherrack), 0, 5, 12, 2),
                new WeightedRandomChestContent(Items.gold_nugget, 0, 5, 21, 3),
                new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 4, 1));


        public static List<WeightedRandomChestContent> getNetherFortressEnchantedBook(Random randomIn) {
            return WeightedRandomChestContent.addList(NETHER_FORTRESS, LootTableEnchantedBook.getEnchantedBooks(randomIn, EnumLootSource.NETHER_FORTRESS, 2,3, 4));
        }

        public static int getNetherFortressLootCount(Random randomIn) {
            return 3 + randomIn.nextInt(3); // was 2 and 4
        }

        public static int getNetherTowerLootCount(Random randomIn) {
            return 2 + randomIn.nextInt(2);
        }

        public static int getNetherBasementLootCount(Random randomIn) {
            return 2 + randomIn.nextInt(3);
        }
    }

    public static class LootAether {
        public static final List<WeightedRandomChestContent> AETHER_DUNGEON = Lists.newArrayList(
//            new WeightedRandomChestContent(Items.glowing_bread, 0, 1, 1, 3),
//            new WeightedRandomChestContent(Items.ruby, 0, 1, 1, 2),
                new WeightedRandomChestContent(Items.glowstone_dust, 0, 1, 4, 10),
                new WeightedRandomChestContent(Items.slime_ball, 0, 1, 4, 2),
                new WeightedRandomChestContent(Items.apple, 0, 1, 2, 3),
                new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 1),
                new WeightedRandomChestContent(Items.record_magnetic_circuit, 0, 1, 1, 2),
                new WeightedRandomChestContent(Items.bread, 0, 1, 4, 5),
                new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 4, 5),
                new WeightedRandomChestContent(Items.gold_nugget, 0, 3, 27, 5),
                new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 2));
    }

    public static class LootEnd {

    }
}
