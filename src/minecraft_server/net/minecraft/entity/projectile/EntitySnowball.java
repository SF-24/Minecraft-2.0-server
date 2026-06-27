package net.minecraft.entity.projectile;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.CapabilityWindChargeFall;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntitySnowball extends EntityThrowable {
    static final double BURST_POWER = 1.0D;
    static final double BURST_RADIUS = 2.5D;
    static final boolean PLAYER_FALL_REDUCTION = true;

    float renderOffset = 0.0f;
    float renderScale = 1.0f;
    int lifetime = 0;
    int mountTime = 0;

    protected EntityLivingBase knockbackImmune;

    public EntitySnowball(World worldIn) {
        super(worldIn);
    }

    public EntitySnowball(World worldIn, EntityLivingBase throwerIn, int type) {
        super(worldIn, throwerIn);
        setProjectileType(type);
//        if(type==10) this.hasNoGravity=true;
    }

    public EntitySnowball(World worldIn, EntityLivingBase throwerIn, int type, EntityLivingBase knockbackImmune) {
        super(worldIn, throwerIn);
        setProjectileType(type);
        this.knockbackImmune = knockbackImmune;
    }

    public EntitySnowball(World worldIn, double x, double y, double z, int type) {
        super(worldIn, x, y, z);
        this.setProjectileType(type);
//        if(type==10) this.hasNoGravity=true;
    }

    // Used to remove objects which exist for too long
    @Override
    public void onUpdate() {
        super.onUpdate();
        // Remove if it exists for too long
        if (getProjectileType() == 10) {
            if (!this.worldObj.isRemote) {
                ++this.lifetime;
                if (this.lifetime == 12 && this.riddenByEntity == null/*300*/) {
                    this.setDead();
                }
            }

            if (this.riddenByEntity != null) {
                // Slowly move upwards
                this.motionX = 0.0D;
                this.motionY = 0.1D;
                this.motionZ = 0.0D;
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX, this.posY, this.posZ, 0.0D, -1.0D, 0.0D);

                if (!this.worldObj.isRemote) {
                    ++this.mountTime;
                    if (this.mountTime >= 70) {
                        this.riddenByEntity.setPositionAndUpdate(this.riddenByEntity.posX, this.riddenByEntity.posY, this.riddenByEntity.posZ);
                        this.setDead();
                    }
                }
            } else if (!this.worldObj.isRemote && mountTime > 0) {
                this.setDead();
            }
        }
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition result) {
        switch (getProjectileType()) {
            case 0:
                if (result.entityHit != null) {
                    int i = 0;

                    if (result.entityHit instanceof EntityBlaze) {
                        i = 3;
                    }

                    result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float) i);
                }

                for (int j = 0; j < 8; ++j) {
                    this.worldObj.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
                }
                break;
            case 1:
                // TODO:
                if (!this.worldObj.isRemote) {
                    if (result.entityHit != null && !(result.entityHit instanceof EntityEnderCrystal) && !(result.entityHit == this.getThrower())) {
                        // Do 1 damage.
                        result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float) 1);
                        /*Bedrock specific behaviour */
                        if (this.isBurning()) result.entityHit.setFire(5);
                        worldObj.playSound(result.entityHit.getPosition().getX(), result.entityHit.getPosition().getY(), result.entityHit.getPosition().getZ(), "random.explode", 0.5F, 0.5F, false);

                    } else if (result.entityHit == null) {
                        worldObj.playSound(result.getBlockPos().getX(), result.getBlockPos().getY(), result.getBlockPos().getZ(), "random.explode", 0.5F, 0.5F, false);
                    }
                }

                performKnockbackEffects(0.0);
                checkBlockInteraction(this.getPosition());

                //Spawn the burst
                for (int i = 0; i < BURST_RADIUS * 1.5; i++) {
                    EnumParticleTypes type = i < BURST_RADIUS * 10 / 10 ? EnumParticleTypes.EXPLOSION_LARGE : EnumParticleTypes.CLOUD;
                    float range = (float) (BURST_RADIUS / 3);
                    double x = this.posX + worldObj.rand.nextFloat() * range - worldObj.rand.nextFloat() * range;
                    double y = this.posY + (worldObj.rand.nextFloat() * range - worldObj.rand.nextFloat() * range) / 2;
                    double z = this.posZ + worldObj.rand.nextFloat() * range - worldObj.rand.nextFloat() * range;

                    (this.worldObj).spawnParticle(type, x, y, z, 0, 0, 0, 1);
                    // Speed 0, number = 1
                }

                break;
            case 10:
                if (!this.worldObj.isRemote && result.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && result.entityHit != this.thrower) {
                    Entity entity = result.entityHit;
                    if (entity != null && !entity.isDead && entity.ridingEntity == null) {
                        // Cache rendering values:

                        if (this.riddenByEntity != null) {
                            this.riddenByEntity.setDead(); // TODO: CHECK FOR BUGS!
                        }
                        entity.mountEntity(this);
                    }
                }
                break;
        }

        if (!this.worldObj.isRemote && this.getProjectileType() != 10) {
            this.setDead();
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(13, (byte) 0);
    }

    @Override
    public boolean canBeCollidedWith() {
        return getProjectileType() != 10;
    }

    // Projectile types:

    /**
     * Return this skeleton's type.
     */
    public int getProjectileType() {
        return this.dataWatcher.getWatchableObjectByte(13);
    }

    public void setProjectileType(int type) {
        this.dataWatcher.updateObject(13, (byte) type);
    }

    /**
     * Wind charge functions:
     * */
    /**
     * Mostly from Minecraft's Explosion Code, as Wind Charges use the same code, but altered a ton for customizability.
     * <p>
     * resistModifier is how much Knockback Resistance is applied.
     *
     */
    public void performKnockbackEffects(double resistModifier) {
        double scale = BURST_RADIUS;
        double knockbackStrength = BURST_POWER + 0.1;
        double k = MathHelper.floor_double(this.posX - (double) scale - 1.0);
        double l = MathHelper.floor_double(this.posX + (double) scale + 1.0);
        double i2 = MathHelper.floor_double(this.posY - (double) scale - 1.0);
        double i1 = MathHelper.floor_double(this.posY + (double) scale + 1.0);
        double j2 = MathHelper.floor_double(this.posZ - (double) scale - 1.0);
        double j1 = MathHelper.floor_double(this.posZ + (double) scale + 1.0);
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(k, i2, j2, l, i1, j1));
        Vec3 vec3d = new Vec3(this.posX, this.posY, this.posZ);

        for (Entity entity : list) {
            /* Multiplied by the knockback to lower it, so low number means lower knockback! */
            double knockbackResist = 1D;
            if (entity instanceof EntityLivingBase)
                knockbackResist -= ((EntityLivingBase) entity).getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue() * resistModifier;

            if (!entity.isImmuneToExplosions() && entity != knockbackImmune) {
                double d12 = entity.getDistance(this.posX, this.posY, this.posZ) / (double) scale;
                if (d12 <= 1.0) {
                    /* The distance to power calculation breaks down if the Wind Charge is spawned within an entity... so we do a little trick called lying >B) */
                    if (d12 == 0) d12 = 1.85F;

                    double dx = entity.posX - this.posX;
                    double dy = entity.posY + (double) entity.getEyeHeight() - this.posY;
                    double dz = entity.posZ - this.posZ;
                    double horizontalDistance = MathHelper.sqrt_double(dx * dx + dz * dz);

                    double distance = MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
                    if (distance != 0.0) {
                        dx /= distance;
                        dy /= distance;
                        dz /= distance;
                        double blockStoppage = this.checkBlockBlocking(vec3d, entity.getEntityBoundingBox());
                        double kmult = (knockbackStrength - d12) * blockStoppage;

                        if (horizontalDistance > 0.25) {
                            entity.motionX += (dx * kmult) * knockbackResist;
                            entity.motionZ += (dz * kmult) * knockbackResist;
                        }
                        entity.motionY += (dy * kmult) * knockbackResist; // Replace dy with 0.4
                        entity.velocityChanged = true;


                        if (entity instanceof EntityPlayer) {
                            CapabilityWindChargeFall capWindCharge = ((EntityPlayer) entity).capabilities.capabilityWindChargeFall;

                            if (PLAYER_FALL_REDUCTION) {
                                capWindCharge.setUsedWindCharge(true);
                                capWindCharge.setWindBurstHeight((int) this.posY);
                                /** This is the Scheduled time, where the landing must occur BEFORE this, or it wil not be applied. */
                                int getTime = (int) (entity.ticksExisted + Math.max(entity.motionY * 60, 30));
                                capWindCharge.setWindBurstTime(getTime);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Mostly from getBlockDensity, but it properly ignores non-solid blocks. Checks how much of the entity is exposed to the explosion.
     */
    private float checkBlockBlocking(Vec3 vec, AxisAlignedBB bb) {
        double dx = 1.0 / ((bb.maxX - bb.minX) * 2.0 + 1.0);
        double dy = 1.0 / ((bb.maxY - bb.minY) * 2.0 + 1.0);
        double dz = 1.0 / ((bb.maxZ - bb.minZ) * 2.0 + 1.0);
        double d3 = (1.0 - Math.floor(1.0 / dx) * dx) / 2.0;
        double d4 = (1.0 - Math.floor(1.0 / dz) * dz) / 2.0;
        if (dx >= 0.0 && dy >= 0.0 && dz >= 0.0) {
            int j2 = 0;
            int k2 = 0;
            for (float fx = 0.0F; fx <= 1.0F; fx = (float) ((double) fx + dx)) {
                for (float fy = 0.0F; fy <= 1.0F; fy = (float) ((double) fy + dy)) {
                    for (float fz = 0.0F; fz <= 1.0F; fz = (float) ((double) fz + dz)) {
                        double d5 = bb.minX + (bb.maxX - bb.minX) * (double) fx;
                        double d6 = bb.minY + (bb.maxY - bb.minY) * (double) fy;
                        double d7 = bb.minZ + (bb.maxZ - bb.minZ) * (double) fz;
                        MovingObjectPosition result = this.worldObj.rayTraceBlocks(new Vec3(d5 + d3, d6, d7 + d4), vec, false, true, false);

                        if (result == null) {
                            ++j2;
                        }
                        ++k2;
                    }
                }
            }
            return (float) j2 / (float) k2;
        } else {
            return 0.0F;
        }
    }

    // Check surrounding blocks

    /**
     * Ray-trace checks surrounding blocks within range.
     */
    private void checkBlockInteraction(BlockPos pos) {
        double radius = BURST_RADIUS;
        /* Uses a list, so the same block isn't interacted with multiple times. */
        List<BlockPos> processedBlocks = new ArrayList<>();

        for (double h1 = -radius; h1 <= radius; h1++) {
            for (double i1 = -radius; i1 <= radius; i1++) {
                for (double j1 = -radius; j1 <= radius; j1++) {
                    BlockPos tPos = pos.add(h1, i1, j1);

                    /* Curves the interaction radius. */
                    if (pos.distanceSq(tPos) > radius * radius) {
                        continue;
                    }

                    /* Toggles between Raytraced interaction, or just anything within this radius. Raytraced has lots of issues currently, likely remove later. */
                    if (false) {
                        MovingObjectPosition result = this.worldObj.rayTraceBlocks(new Vec3(pos.getX(), pos.getY(), pos.getZ()), new Vec3(tPos.getX(), tPos.getY(), tPos.getZ()));
                        if (result != null && result.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                            if (processedBlocks.contains(result.getBlockPos())) {
                                continue;
                            }
                            activateBlocks(result.getBlockPos());
                            processedBlocks.add(result.getBlockPos());
                        }
                    } else {
                        if (processedBlocks.contains(tPos)) {
                            continue;
                        }
                        activateBlocks(tPos);
                        processedBlocks.add(tPos);
                    }
                }
            }
        }
    }

    /**
     * Simply activates blocks.
     */
    private void activateBlocks(BlockPos pos) {
        Block block = this.worldObj.getBlockState(pos).getBlock();
        EntityPlayer countedInteract = thrower instanceof EntityPlayer ? (EntityPlayer) thrower : null;

        if (block instanceof BlockButton || block instanceof BlockTrapDoor || block instanceof BlockDoor && this.worldObj.getBlockState(pos).getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER || block instanceof BlockLever) {
            block.onBlockActivated(this.worldObj, pos, this.worldObj.getBlockState(pos), countedInteract, EnumFacing.UP, 0.5F, 0.5F, 0.5F);
        }
        /* AAAAAAAHHHHH WHY DOES BlockFenceGate REQUIRE A PLAYER FOR `onBlockActivated`, IT COULD JUST USE THE HIT POS! */
        else if (block instanceof BlockFenceGate) {
            boolean isOpen = this.worldObj.getBlockState(pos).getValue(BlockFenceGate.OPEN);
            EnumFacing getFacing = this.worldObj.getBlockState(pos).getValue(BlockFenceGate.FACING);

            worldObj.setBlockState(pos, this.worldObj.getBlockState(pos).withProperty(BlockFenceGate.OPEN, !isOpen).withProperty(BlockFenceGate.FACING, this.rand.nextBoolean() ? getFacing.getOpposite() : getFacing));
//            worldObj.playEvent(null, isOpen ? 1014 : 1008, pos, 0);
        }
    }

    @Override
    protected boolean hasNoGravity() {
        return getProjectileType() == 10;
    }

    public float getRenderScale() {
        return this.renderScale;
    }

    public float getRenderOffset() {
        return this.renderOffset;
    }

    public void setRenderScale(float value) {
        this.renderScale=value;
    }

    public void setRenderOffset(float value) {
        this.renderOffset=value;
    }
}
