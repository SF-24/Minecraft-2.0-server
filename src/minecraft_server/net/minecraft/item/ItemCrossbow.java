package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.mineshaft.item.ProjectileHelper;

public class ItemCrossbow extends Item {

    private final int drawTime;
    private final float damageMultiplier;
    private final int enchantability;
    private final int textureVariant;

    public ItemCrossbow(int textureVariant, int drawTime, float damageMultiplier, int enchantability, int durability) {
        this.drawTime = drawTime;
        this.damageMultiplier = damageMultiplier;
        this.enchantability = enchantability;
        this.textureVariant=textureVariant;
        this.setMaxStackSize(1);
        this.setMaxBundleStackSize(4);
        this.setMaxDamage(durability);
        this.setCreativeTab(CreativeTabs.tabCombat);
    }

    @Override
    public int getItemEnchantability()
    {
        return this.enchantability;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        setLoaded(worldIn,stack,true);
        return stack;
    }

    public int getMaxItemUseDuration(ItemStack stack)
    {
        return drawTime;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        ItemStack stack = playerIn.getHeldItem();
        if (!isLoaded(stack) && (playerIn.capabilities.isCreativeMode || playerIn.inventory.hasItem(Items.arrow)))
        {
            playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        } else if (canFire(worldIn,stack)) {
            // Shoot
            ProjectileHelper.shootProjectileArrowUsingItem(worldIn,playerIn,stack,itemRand,1.0f, 1.5f, this.damageMultiplier,true);
            setLoaded(worldIn,stack, false);
            return stack;
        }
        return stack;
    }

    public static boolean isLoaded(ItemStack stack) {
        NBTTagCompound nbtTagCompound = stack.getTagCompound();
        if(nbtTagCompound==null) nbtTagCompound=new NBTTagCompound();
        return nbtTagCompound.getBoolean("Loaded");
    }
    public static void setLoaded(World worldIn, ItemStack stack, boolean isLoaded) {
        NBTTagCompound nbtTagCompound = stack.getTagCompound();
        if (nbtTagCompound == null) nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setBoolean("Loaded", isLoaded);
        if(isLoaded) {
            nbtTagCompound.setLong("CanUseTime", worldIn.getTotalWorldTime() + 10);
        }
        stack.setTagCompound(nbtTagCompound);
    }
    public static boolean canFire(World world, ItemStack stack) {
        NBTTagCompound nbtTagCompound = stack.getTagCompound();
        if(nbtTagCompound==null) nbtTagCompound=new NBTTagCompound();
        long time = nbtTagCompound.getLong("CanUseTime");
//        System.out.println("Time: " + time + " World time: " + world.getTotalWorldTime());
        return isLoaded(stack) && world.getTotalWorldTime()>=time;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.CROSSBOW;
    }

}
