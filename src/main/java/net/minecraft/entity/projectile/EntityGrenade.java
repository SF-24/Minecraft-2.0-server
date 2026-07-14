package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityGrenade extends EntityThrowable
{
    private final Item item;

    public EntityGrenade(World worldIn, Item item)
    {
        super(worldIn);
        this.item = item;
    }

    public EntityGrenade(World worldIn, EntityLivingBase throwerIn, Item item)
    {
        super(worldIn, throwerIn);
        this.item = item;
    }

    public EntityGrenade(World worldIn, double x, double y, double z, Item item)
    {
        super(worldIn, x, y, z);
        this.item = item;
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition p_70184_1_)
    {
        if (!this.worldObj.isRemote)
        {
            this.setDead();
        }

        worldObj.newExplosion(null, this.posX, this.posY, this.posZ, 6.5F, false, false);
    }
}
