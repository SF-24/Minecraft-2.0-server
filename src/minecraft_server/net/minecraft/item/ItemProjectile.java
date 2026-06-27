package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemProjectile extends Item
{
    byte variant;
    public ItemProjectile(byte variant)
    {
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.variant=variant;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        if (!playerIn.capabilities.isCreativeMode)
        {
            --itemStackIn.stackSize;
        }

        worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if(variant==1) playerIn.getCooldownTracker().setCooldown(this, 10);

        if (!worldIn.isRemote)
        {
            switch (variant) {
                case 0:
                    worldIn.spawnEntityInWorld(new EntitySnowball(worldIn, playerIn,this.variant));
                    break;
                case 1:
                    EntitySnowball entitySnowball = new EntitySnowball(worldIn,playerIn,this.variant);
                    double dirX = -Math.sin(Math.toRadians(playerIn.rotationYaw)) * Math.cos(Math.toRadians(playerIn.rotationPitch));
                    double dirY = -Math.sin(Math.toRadians(playerIn.rotationPitch));
                    double dirZ = Math.cos(Math.toRadians(playerIn.rotationYaw)) * Math.cos(Math.toRadians(playerIn.rotationPitch));
                    entitySnowball.setThrowableHeading(dirX,dirY,dirZ, 1.8f, 1.0f);
                    worldIn.spawnEntityInWorld(entitySnowball);
                    // TODO: ADD A COOLDOWN.
                    break;
            }
        }

        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }
}
