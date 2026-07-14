package net.minecraft.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumTweakMode;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;

import java.util.ArrayList;
import java.util.HashMap;

public class FoodStats
{
    private int foodDecayTime;

    private int healAmount = 2;

    public ArrayList<Integer> timers = new ArrayList<>();
    private HashMap<ItemFood, Integer> foodHashMap = new HashMap<>();
    private ArrayList<ItemFood> foods = new ArrayList<>();

    /** The player's food level. */
    private int foodLevel = 20;

    /** The player's food saturation. */
    private float foodSaturationLevel = 5.0F;

    /** The player's food exhaustion. */
    private float foodExhaustionLevel;

    /** The player's food timer value. */
    private int foodTimer;
    private int healTimer;
    private int sprintDrainTimer;
    private int prevFoodLevel = 20;

    int drainTime = 25;

    private float healthLevel = 20;
    private float maxHealthLevel = 20;

    EnumTweakMode currentMode= EnumTweakMode.DEFAULT;

    /**
     * Add food stats.
     */
    public void addStats(int foodLevelIn, float foodSaturationModifier)
    {
        this.foodLevel = Math.min(foodLevelIn + this.foodLevel, 20);
        this.foodSaturationLevel = Math.min(this.foodSaturationLevel + (float)foodLevelIn * foodSaturationModifier * 2.0F, (float)this.foodLevel);
    }

    public void addStats(ItemFood foodItem, ItemStack p_151686_2_)
    {
        this.addStats(foodItem.getHealAmount(p_151686_2_), foodItem.getSaturationModifier(p_151686_2_));
    }

    /**
     * Handles the food game logic.
     */
    public void onUpdate(EntityPlayer player)
    {
        EnumDifficulty enumdifficulty = player.worldObj.getDifficulty();
        this.prevFoodLevel = this.foodLevel;
        this.currentMode=player.getEntityWorld().getGameRules().getEnumTweakMode("currentMode");
        this.healthLevel=player.getHealth();
        this.maxHealthLevel=player.getMaxHealth();

        if (this.foodExhaustionLevel > 4.0F)
        {
            this.foodExhaustionLevel -= 4.0F;

            if (!currentMode.enableRegainStamina() && this.foodSaturationLevel > 0.0F)
            {
                this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0F, 0.0F);
            }
            else if (enumdifficulty != EnumDifficulty.PEACEFUL)
            {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }

        GameRules gameRules = player.worldObj.getGameRules();

        // player heals when at least 6 hp
        if (currentMode.enableNaturalRegen() && gameRules.getBoolean("naturalRegeneration") && this.foodLevel >= 6 /*was 18*/ && player.shouldHeal())
        {
            ++this.foodTimer;

            // heal every 3 seconds, not 4
            if (this.foodTimer >= 60) // was 80
            {
                player.heal(1.0F);
                this.addExhaustion(3.0F);
                this.foodTimer = 0;
            }
        }
        else if (currentMode.enableStarvationDamage() && this.foodLevel <= 0)
        {
            ++this.foodTimer;

            if ((currentMode.equals(EnumTweakMode.MODERN) && this.foodTimer>=40) || this.foodTimer >= 80)
            {
                if (currentMode.equals(EnumTweakMode.MODERN) || ((player.getHealth() > 10.0F || enumdifficulty == EnumDifficulty.HARD || player.getHealth() > 1.0F && enumdifficulty == EnumDifficulty.NORMAL)))
                {
                    player.attackEntityFrom(DamageSource.starve, 1.0F);
                }

                this.foodTimer = 0;
            }
        }
        else if(this.foodTimer>=80)
        {
            this.foodTimer = 0;
        }

        if(currentMode.enableSprintDrain()) {
            if (player.isSprinting() && this.foodLevel > 0) {
                sprintDrainTimer++;

                if (this.sprintDrainTimer >= drainTime) {
                    this.foodLevel=foodLevel-1;
                    this.prevFoodLevel=foodLevel;
                    this.sprintDrainTimer = 0;
                }
            }
        }

        if (currentMode.enableRegainStamina())
        {
            /**
             * VIKING TWEAKS HEALTH REGENERATION CODE:
             * VARIES DEPENDING ON FOOD
             * */

            if(player.getHealth()<player.getMaxHealth()) {
                ++healTimer;

                if(healTimer % 20 == 0) {
                    System.out.println("TICKING TIMER");
                    if(foodHashMap.keySet().size()>0) {
                        for (ItemFood element : foodHashMap.keySet()) {
                            foodHashMap.replace(element, foodHashMap.get(element) - 1);
                            System.out.println("TIME LEFT FOR FOOD " + element + ": " + (foodHashMap.get(element) - 1));
                        }
                    }
                }

                if (healTimer >= 200)
                {
                    player.heal(this.healAmount);
                    this.healTimer = 0;
                }

            } else if(healTimer>0) healTimer=0;


            /**
             * STAMINA REGENERATION CODE:
             * |||
             * TEST VALUES ARE SHOWN BELOW
             * */

            // val: 15/25
            // val: 10/20
            // val: 10/15
            // val: 20/40

            foodTimer++;
            if (this.foodTimer >= 15)
            {
                if(!player.isSprinting() && this.foodLevel<20) {
                    this.foodLevel++;
                    this.foodTimer = 0;
                }
            }
        }
    }

    /**
     * Reads the food data for the player.
     */
    public void readNBT(NBTTagCompound p_75112_1_)
    {
        if (p_75112_1_.hasKey("foodLevel", 99))
        {
            this.foodLevel = p_75112_1_.getInteger("foodLevel");
            this.foodTimer = p_75112_1_.getInteger("foodTickTimer");
            this.foodSaturationLevel = p_75112_1_.getFloat("foodSaturationLevel");
            this.foodExhaustionLevel = p_75112_1_.getFloat("foodExhaustionLevel");
        }
    }

    /**
     * Writes the food data for the player.
     */
    public void writeNBT(NBTTagCompound p_75117_1_)
    {
        p_75117_1_.setInteger("foodLevel", this.foodLevel);
        p_75117_1_.setInteger("foodTickTimer", this.foodTimer);
        p_75117_1_.setFloat("foodSaturationLevel", this.foodSaturationLevel);
        p_75117_1_.setFloat("foodExhaustionLevel", this.foodExhaustionLevel);
    }

    /**
     * Get the player's food level.
     */
    public int getFoodLevel()
    {
        return this.foodLevel;
    }

    public int getPrevFoodLevel()
    {
        return this.prevFoodLevel;
    }

    /**
     * Get whether the player must eat food.
     */
    public boolean needFood()
    {
        boolean value = this.foodLevel < 20;
        if(!value&&(currentMode.equals(EnumTweakMode.CLASSIC_MODE)) && healthLevel<maxHealthLevel) value=true;
        return value;
    }

    /**
     * adds input to foodExhaustionLevel to a max of 40
     */
    public void addExhaustion(float p_75113_1_)
    {
        this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + p_75113_1_, 40.0F);
    }

    /**
     * Get the player's food saturation level.
     */
    public float getSaturationLevel()
    {
        return this.foodSaturationLevel;
    }

    public void setFoodLevel(int foodLevelIn)
    {
        this.foodLevel = foodLevelIn;
    }

    public void setFoodSaturationLevel(float foodSaturationLevelIn)
    {
        this.foodSaturationLevel = foodSaturationLevelIn;
    }

    /**
     * VIKING MODE EATING FUNCTIONS
     *
     * ALL NICELY DISPLAYED BELOW
     */

    // update the time of a food, i.e. if player eats a food, that they have eaten previously and is in the food bar,
    // and have eaten it again, remove tha old one from the hashmap and add the new one
    public void updateFoodTime(ItemFood itemFood) {
        foodHashMap.replace(itemFood, itemFood.getFoodType().getFoodLastTime());
    }

    // run eating functions
    public void eatItem(ItemFood itemFood) {
        if(canEatItem(itemFood)) {
            if(getCurrentFoodCount()<3) {
                foodHashMap.put(itemFood, itemFood.getFoodType().getFoodLastTime());
                foods.add(itemFood);
                timers.add(itemFood.getFoodType().getFoodLastTime());
            } else {
                updateFoodTime(itemFood);
            }
        }
    }

    // check if item is replaceable, ie, is included and can have time extended
    public ItemFood getReplaceableItem() {
        for(ItemFood itemFood : foodHashMap.keySet()) {
            if(foodHashMap.get(itemFood)<=foodDecayTime) { return itemFood; }
        }
        return null;
    }

    // check if player can eat item
    public boolean canEatItem(ItemFood itemFood) {
        if(canEat()) {
            if(!foodHashMap.containsKey(itemFood)) {
                return true;
            }
            return foodHashMap.get(itemFood)<=foodDecayTime;
        }
        return false;
    }

    // check if player can eat in general
    public boolean canEat() {
        //if(foodHashMap.keySet().size()<3) return true;
        for(ItemFood itemFood : foodHashMap.keySet()) {
            if(itemFood==null || foodHashMap.get(itemFood)<=foodDecayTime) { return true; }
        }
        return false;
    }

    // get current player food count. Max = 3
    public int getCurrentFoodCount() {
        return foodHashMap.keySet().size();
    }

    // get food of number in hash map
    public ItemFood getFoodOfNumber(int number) {
        int i = 0;
        for(ItemFood itemFood : foodHashMap.keySet()) {
            if(i==number){
                return itemFood;
            }
            i++;
        }
        return null;
    }

    // get timer for food of number in hash map
    public int getFoodTimerOfNumber(int number) {
        if(getFoodOfNumber(number)==null) return 0;
        System.out.println(foodHashMap.get(getFoodOfNumber(number)));
        return foodHashMap.get(getFoodOfNumber(number));
    }

    // get the food hash map
    public HashMap<ItemFood, Integer> getFoodHashMap() { return foodHashMap; }

    // get time at which new food can be eaten and food tints transparent
    public int getFoodDecayTime() {return foodDecayTime;}

    public NBTTagList saveMapToNbt() {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < foodHashMap.size(); ++i) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();

            nbtTagCompound.setInteger("Timer", getFoodTimerOfNumber(i));
            ResourceLocation resourcelocation = Item.itemRegistry.getNameForObject(getFoodOfNumber(i));
            nbtTagCompound.setString("id", resourcelocation == null ? "minecraft:air" : resourcelocation.toString());
            nbttaglist.appendTag(nbtTagCompound);
        }
        return nbttaglist;
    }

    public void readMapFromNbt(NBTTagList tagList) {

        HashMap<ItemFood, Integer> foodMap = new HashMap<>();

        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound nbtTagCompound = tagList.getCompoundTagAt(i);

            int timer = nbtTagCompound.getInteger("Timer");
            ItemFood id = (ItemFood) Item.getByNameOrId(nbtTagCompound.getString("id"));
            foodMap.put(id,timer);
        }
        this.foodHashMap=foodMap;
    }

}
