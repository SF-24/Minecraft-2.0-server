package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.world.World;

public class ItemStaff extends Item {

    // Variants:
    // 0 water
    // 1 fire
    // 2 wind
    // 3 ice
    // 4 health

    int variant;

    public ItemStaff(int variant) {
        this.setMaxDamage(50);
        this.setCreativeTab(CreativeTabs.tabCombat);
        this.variant = variant;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (!playerIn.capabilities.isCreativeMode) {
            itemStackIn.damageItem(1, playerIn);
        }
        if (!worldIn.isRemote) {
            EntitySnowball entitySnowball = new EntitySnowball(worldIn,playerIn,10);
            worldIn.spawnEntityInWorld(entitySnowball);
        }
        playerIn.getCooldownTracker().setCooldown(this, 10);
        return itemStackIn;
    }
}
