package net.mineshaft;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.EnumDifficulty;

public class DifficultyManager {

    public static void applyDebuff(EnumDifficulty difficulty, int potionEffectId, EntityLivingBase target) {
        int i = 0;

        if (difficulty == EnumDifficulty.NORMAL)
        {
            i = 7;
        }
        else if (difficulty == EnumDifficulty.HARD)
        {
            i = 15;
        }

        if (i > 0)
        {
            ((EntityLivingBase)target).addPotionEffect(new PotionEffect(potionEffectId, i * 20, 0));
        }
    }

    public static void applyShortDebuff(EnumDifficulty difficulty, int potionEffectId, EntityLivingBase target) {
        // Don't get poisoned on easy
        int i = 0;

        if (difficulty == EnumDifficulty.NORMAL)
        {
            i = 3;
        }
        else if (difficulty == EnumDifficulty.HARD)
        {
            i = 5;
        }

        if (i > 0)
        {
            ((EntityLivingBase)target).addPotionEffect(new PotionEffect(potionEffectId, i * 20, 0));
        }
    }

    public static void applyShortDebuff(EnumDifficulty difficulty, int potionEffectId, EntityLivingBase target, int amplifier) {
        // Don't get poisoned on easy
        int i = 0;

        if (difficulty == EnumDifficulty.NORMAL)
        {
            i = 3;
        }
        else if (difficulty == EnumDifficulty.HARD)
        {
            i = 5;
        }

        if (i > 0)
        {
            ((EntityLivingBase)target).addPotionEffect(new PotionEffect(potionEffectId, i * 20, amplifier));
        }
    }

}
