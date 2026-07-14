package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityGrenade;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemGrenade extends Item
{
    int meta;

    public ItemGrenade(int meta)
    {
        this.meta = meta;
    }

    /**
     * Called whenever the player finishes usingthis item
     */
    public void onPlayerStoppedUsing(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, int timeLeft)
    {
        if (!playerIn.capabilities.isCreativeMode)
        {
            --itemStackIn.stackSize;
        }

        worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote)
        {
            worldIn.spawnEntityInWorld(new EntityGrenade(worldIn, playerIn, itemStackIn.getItem()));
        }
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    /**
     * When item is right-clicked, set in use
     * */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        return itemStackIn;
    }

    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        return stack;
    }

    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        if (stack.getMetadata() == 0)
        {
            tooltip.add("");
            tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("grenade.holy.description.1"));
            tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("grenade.holy.description.2"));
            tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("grenade.holy.description.3"));
        }
    }
}
