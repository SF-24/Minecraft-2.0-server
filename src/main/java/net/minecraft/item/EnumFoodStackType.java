package net.minecraft.item;

import net.minecraft.init.Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EnumFoodStackType
{
    SOUP(8, Arrays.asList(Items.mushroom_stew, Items.rabbit_stew)),
    NORMAL(64, new ArrayList<>()),
    LARGE(64, Arrays.asList(Items.cookie, Items.melon, Items.potato, Items.carrot, Items.poisonous_potato)),
    HEALING(16, Arrays.asList(Items.golden_apple, Items.glowing_bread));

    private final int defaultValue;
    private final List<Item> included;

    EnumFoodStackType(int defaultValue, List<Item> included)
    {
        this.defaultValue = defaultValue;
        this.included = included;
    }

    public int getDefaultValue()
    {
        return defaultValue;
    }
    public List<Item> getItemList()
    {
        return included;
    }
}
