package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemBackpack extends Item {

    // meta 0 = ender pouch
    int meta;

    public ItemBackpack(int meta)
    {
        this.meta=meta;
        this.setMaxDamage(64);
        this.setMaxStackSize(1);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        // if item is ender pouch
        if(meta==0) {
            InventoryEnderChest inventoryenderchest = playerIn.getInventoryEnderChest();
            worldIn.playSoundEffect(playerIn.posX, playerIn.posY + 0.5D, playerIn.posZ, "dig.cloth", 0.5F, worldIn.rand.nextFloat() * 0.15F + 0.9F);
            playerIn.displayGUIChest(inventoryenderchest);
            itemStackIn.damageItem(1,playerIn);
            playerIn.triggerAchievement(StatList.field_181738_V);
        }
        return itemStackIn;
    }

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return Items.blaze_powder == repair.getItem() || super.getIsRepairable(toRepair, repair);
    }
}
