package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityGiantZombie extends EntityMob
{
    protected void entityInit()
    {
        super.entityInit();
        this.getDataWatcher().addObject(12, Byte.valueOf((byte)0));
        this.getDataWatcher().addObject(13, Byte.valueOf((byte)0));
        this.getDataWatcher().addObject(14, Byte.valueOf((byte)0));
    }


    public EntityGiantZombie(World worldIn)
    {
        super(worldIn);
        this.multiplySize(2.5f); // was 6.0f
        this.setEntityBoundingBox(this.getEntityBoundingBox().expand(16.0D*1.5D,16.0D*3.0D,16.0D*1.5D));
        this.renderDistanceWeight=2;
        this.isImmuneToFire = true;
        this.applyEntityAI();
    }

    protected final void multiplySize(float size)
    {
        super.setSize(1 * size, 2 * size);
    }

    public float getEyeHeight() {return 4.35F;}

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D); // was 100
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier("Giant zombie bonus", this.rand.nextDouble() * 3.0D + 10.0D, 0));

        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.45D); // was 0.5
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0D);//from 50
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);//from 35
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
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
        int i = this.rand.nextInt(2 + lootingModifier)+1;

        for (int j = 0; j < i; ++j)
        {
            this.dropItem(Items.golden_apple, 1);
        }
    }

    public float getBlockPathWeight(BlockPos pos)
    {
        return this.worldObj.getLightBrightness(pos) - 0.5F;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.zombie.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.zombie.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.zombie.death";
    }

    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound("mob.zombie.step", 0.15F, 1.0F);
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    public int getTotalArmorValue()
    {
        int i = super.getTotalArmorValue() + 2;

        if (i > 20)
        {
            i = 20;
        }

        return i;
    }
}
