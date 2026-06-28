package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.Random;

public class ItemStaff extends Item {

    // Variants:
    // 0 water
    // 1 fire
    // 2 wind
    // 3 ice
    // 4 health

    int variant;

    public ItemStaff(int variant, int durability) {
        this.setMaxDamage(durability);
        this.maxBundleStackSize=4;
        this.maxStackSize=1;
        this.setCreativeTab(CreativeTabs.tabCombat);
        this.variant = variant;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (!playerIn.capabilities.isCreativeMode) {
            itemStackIn.damageItem(1, playerIn);
        }
        if(this.variant==0 && !worldIn.isRemote) {
            EntitySnowball entitySnowball = new EntitySnowball(worldIn,playerIn,10);
            worldIn.spawnEntityInWorld(entitySnowball);
        } else if(this.variant == 1) {
            if(!worldIn.isRemote) {
                for (Entity targetEntity : worldIn.loadedEntityList) {
                    if (targetEntity.getDistanceToEntity(playerIn) <= 6.0F /*distance*/ && targetEntity != playerIn) {
                        targetEntity.setFire(4);
                        Vec3 vec = playerIn.getLookVec();
                        // Applies a small force to the target
                        targetEntity.addVelocity(vec.xCoord / (double) 30.0F, 0.2, vec.zCoord / (double) 30.0F);
                        // Damage the entity using magic.
                        targetEntity.attackEntityFrom(DamageSource.magic, 1.0F); // TODO: Tweak damage
                    }
                }
            }

            worldIn.playSound(playerIn.posX,playerIn.posY,playerIn.posZ, "entity.ghast.shoot",5.0F,1.0F,false);

            Random rand = new Random();
            float yaw = playerIn.rotationYaw;
            float pitch = playerIn.rotationPitch;

            // Calculate direction vector based on yaw
            float x = (float)(-Math.sin(Math.toRadians(yaw)));
            float z = (float)Math.cos(Math.toRadians(yaw));
            double y = -Math.sin(Math.toRadians(pitch));

            // Spawn 8 flame particles around the player
            for(int i = 0; i < 8; ++i) {
                worldIn.spawnParticle(EnumParticleTypes.FLAME, playerIn.posX, playerIn.posY + 1.0d, playerIn.posZ, ((double)(x + rand.nextFloat()) - 0.5d) / 2.0d, (y + (double)rand.nextFloat() - 0.5d) / 8.0d, ((double)(z + rand.nextFloat()) - 0.5d) / 2.0d, 0);
            }

            // Swing the player's arm
            playerIn.swingItem();

        }

        playerIn.getCooldownTracker().setCooldown(this, 10);
        return itemStackIn;
    }
}
