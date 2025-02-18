package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemFood extends Item
{
    /** Number of ticks to run while 'EnumAction'ing until result. */
    public int itemUseDuration;

    // food type
    public EnumFoodType foodType;

    /** The amount this food item heals the player. */
    private final int healAmount;
    private final float saturationModifier;

    /** Whether wolves like this food (true for raw and cooked porkchop). */
    private final boolean isWolfsFavoriteMeat;

    /**
     * If this field is true, the food can be consumed even if the player don't need to eat.
     */
    private boolean alwaysEdible;
    private boolean alwaysEdibleBase = false;

    /**
     * represents the potion effect that will occurr upon eating this food. Set by setPotionEffect
     */
    private int potionId;

    /** set by setPotionEffect */
    private int potionDuration;

    /** set by setPotionEffect */
    private int potionAmplifier;

    /** probably of the set potion effect occurring */
    private float potionEffectProbability;

    public ItemFood(int amount, float saturation, boolean isWolfFood)
    {
        this.itemUseDuration = 32;
        this.healAmount = amount;
        this.isWolfsFavoriteMeat = isWolfFood;
        this.saturationModifier = saturation;
        this.setCreativeTab(CreativeTabs.tabFood);
        this.maxStackSize = 64;
    }
    public ItemFood(int amount, boolean isWolfFood, EnumFoodType foodType)
    {
        this(amount, 0.6F, isWolfFood);
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        --stack.stackSize;
        // custom code
        // ADDED CLASSIC MODE SETTING
        EnumTweakMode mode = worldIn.getGameRules().getEnumTweakMode("currentMode");

        if (mode.equals(EnumTweakMode.DEFAULT) || mode.equals(EnumTweakMode.MODERN))
        {
            playerIn.getFoodStats().addStats(this, stack);
            worldIn.playSoundAtEntity(playerIn, "random.burp", 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
        }
        else if (mode.equals(EnumTweakMode.CLASSIC_MODE))
        {
            int healLeftOver = (int)(playerIn.getHealth() + this.getHealAmount(stack) - playerIn.getMaxHealth());

            if (healLeftOver < 0)
            {
                healLeftOver = 0;
            }

            playerIn.heal(this.getHealAmount(stack) - healLeftOver);
            playerIn.getFoodStats().addStats(healLeftOver, this.getSaturationModifier(stack));
        }

        /*        else if (mode.equals(EnumTweakMode.VIKING_MODE))
                {
                }
        */
        this.onFoodEaten(stack, worldIn, playerIn);
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return stack;
    }

    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
        if (!worldIn.isRemote && this.potionId > 0 && worldIn.rand.nextFloat() < this.potionEffectProbability)
        {
            player.addPotionEffect(new PotionEffect(this.potionId, this.potionDuration * 20, this.potionAmplifier));
        }
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.EAT;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        if (playerIn.canEat(this.alwaysEdible))
        {
            playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        }

        return itemStackIn;
    }

    public int getHealAmount(ItemStack stack)
    {
        return this.healAmount;
    }

    public float getSaturationModifier(ItemStack stack)
    {
        return this.saturationModifier;
    }

    /**
     * Whether wolves like this food (true for raw and cooked porkchop).
     */
    public boolean isWolfsFavoriteMeat()
    {
        return this.isWolfsFavoriteMeat;
    }

    /**
     * sets a potion effect on the item. Args: int potionId, int duration (will be multiplied by 20), int amplifier,
     * float probability of effect happening
     */
    public ItemFood setPotionEffect(int id, int duration, int amplifier, float probability)
    {
        this.potionId = id;
        this.potionDuration = duration;
        this.potionAmplifier = amplifier;
        this.potionEffectProbability = probability;
        return this;
    }

    /**
     * Set the field 'alwaysEdible' to true, and make the food edible even if the player don't need to eat.
     */
    public ItemFood setAlwaysEdibleBase(boolean value)
    {
        alwaysEdibleBase = value;
        return this;
    }

    public ItemFood setAlwaysEdible(boolean value)
    {
        if (value)
        {
            alwaysEdible = true;
        }
        else
        {
            alwaysEdible = alwaysEdibleBase;
        }

        return this;
    }

    protected void setFoodStackCount(int value, EnumFoodStackType foodStackType)
    {
        System.out.println(this + " | " + value + " | " + foodStackType);

        if (!foodStackType.equals(EnumFoodStackType.NORMAL))
        {
            if (foodStackType.getItemList().contains(this))
            {
                this.setMaxStackSize(value);
            }
        }
        else
        {
            if (!EnumFoodStackType.SOUP.getItemList().contains(this) && !EnumFoodStackType.LARGE.getItemList().contains(this))
            {
                this.setMaxStackSize(value);
            }
        }
    }

    protected void setTweakMode(EnumTweakMode tweakMode)
    {
        if (tweakMode.equals(EnumTweakMode.DEFAULT))
        {
            this.itemUseDuration = 32;
            this.setAlwaysEdible(false);
        }

        if (tweakMode.equals(EnumTweakMode.CLASSIC_MODE))
        {
            this.itemUseDuration = 48;
            this.setAlwaysEdible(true);
        }

        /*if (tweakMode.equals(EnumTweakMode.VIKING_MODE))
        {
            this.itemUseDuration = 48;
            this.setAlwaysEdible(false);
        }*/
    }
}
