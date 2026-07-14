package net.minecraft.item;

public enum EnumFoodType
{
    FRUIT(900, 1.0f),
    RAW_VEGETABLE(900, 1.0f),
    COOKED_VEGETABLE(1200, 1.0f),
    BAKING(1200, 1.0f),
    RAW_MEAT(600, 1.2f),
    COOKED_MEAT(1200, 1.2f),
    RAW_FISH(900, 1.0f),
    COOKED_FISH(1200, 1.0f),
    SOUP(1800, 1.5f),
    POISON(0, 1.0f),
    MAGIC(2400, 1.0f),
    EXCLUDED(0, 0.0f);

    int foodLastTime;
    float regenAmount;

    EnumFoodType(int foodLastTime, float regenAmount)
    {
        this.foodLastTime = foodLastTime;
        this.regenAmount = regenAmount;
    }

    public float getRegenAmount()
    {
        return regenAmount;
    }
    public int getFoodLastTime()
    {
        return foodLastTime;
    }
    public int getFoodLastTimeTicks()
    {
        return foodLastTime * 20;
    }
}
