package net.mineshaft.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

import java.util.Random;

public class ProjectileHelper {

    public static double maxProjectileDamageScalar = 3.5D;

    public static void shootProjectileArrowUsingItem(World worldIn, EntityPlayer playerIn, ItemStack stack, Random random, float chargeCapacity, float velocityMultiplier, float damageMultiplier, boolean canArrowBePickedUp) {
        float f = chargeCapacity;
        f = (f * f + f * 2.0F) / 3.0F;

        if ((double)f < 0.1D)
        {
            return;
        }

        if (f > 1.0F)
        {
            f = 1.0F;
        }

        EntityArrow entityarrow = new EntityArrow(worldIn, playerIn, f * 2.0F * velocityMultiplier);

        if (f == 1.0F)
        {
            entityarrow.setIsCritical(true);
        }

        int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);

        if (powerLevel > 0)
        {
            // Power level:
            // 1: +1
            // 2: +1.5
            // 3: +2
            // 4: +2.5
            // 5: +3.0
            entityarrow.setDamage(entityarrow.getDamage()*damageMultiplier + (double)powerLevel * 0.5D + 0.5D);
        } else {
            entityarrow.setDamage(entityarrow.getDamage()*damageMultiplier);
        }

        int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);

        if (k > 0)
        {
            entityarrow.setKnockbackStrength(k);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0)
        {
            entityarrow.setFire(100);
        }

        worldIn.playSoundAtEntity(playerIn, "random.bow", 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

        if (canArrowBePickedUp)
        {
            entityarrow.canBePickedUp = 2;
        }
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(stack.getItem())]);

        if (!worldIn.isRemote)
        {
            worldIn.spawnEntityInWorld(entityarrow);
        }
    }
}
