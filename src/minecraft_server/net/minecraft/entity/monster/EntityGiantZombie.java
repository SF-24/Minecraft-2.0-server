package net.minecraft.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class EntityGiantZombie extends EntityZombie
{
    public EntityGiantZombie(World worldIn)
    {
        super(worldIn);
        this.multiplySize(6f);
        this.isImmuneToFire = true;
        this.applyEntityAI();
    }

    public float getEyeHeight()
    {
        return 10.440001F;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(reinforcementChance).setBaseValue(0.0D);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(30.0D);//from 50
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);//from 35
    }

    public void onUpdate()
    {
        super.onUpdate();
    }

    protected void applyEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityZombie.class}));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    /**
     * Drop 0-2 items of this living's type
     *
     * @param wasRecentlyHit true if this this entity was recently hit by appropriate entity (generally only if player
     * or tameable)
     * @param lootingModifier level of enchanment to be applied to this drop
     */
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        int i = this.rand.nextInt(2 + lootingModifier) + 2;

        for (int j = 0; j < i; ++j)
        {
            this.dropItem(Items.golden_apple, 1);
        }

        i = this.rand.nextInt(2) + 1;

        for (int k = 0; k < i; ++k)
        {
            this.dropItem(Items.ruby, 1);
        }
    }

    public float getBlockPathWeight(BlockPos pos)
    {
        return this.worldObj.getLightBrightness(pos) - 0.5F;
    }
}
