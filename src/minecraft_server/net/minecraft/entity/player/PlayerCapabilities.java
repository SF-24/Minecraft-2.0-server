package net.minecraft.entity.player;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerCapabilities
{
    /** Disables player damage. */
    public boolean disableDamage;

    /** Sets/indicates whether the player is flying. */
    public boolean isFlying;

    /** whether or not to allow the player to fly when they double jump. */
    public boolean allowFlying;

    /**
     * Used to determine if creative mode is enabled, and therefore if items should be depleted on usage
     */
    public boolean isCreativeMode;

    /** Indicates whether the player is allowed to modify the surroundings */
    public boolean allowEdit = true;
    private float flySpeed = 0.05F;
    private float walkSpeed = 0.1F;

    public CapabilityWindChargeFall capabilityWindChargeFall = new CapabilityWindChargeFall();

    public void writeCapabilitiesToNBT(NBTTagCompound tagCompound)
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setBoolean("invulnerable", this.disableDamage);
        nbttagcompound.setBoolean("flying", this.isFlying);
        nbttagcompound.setBoolean("mayfly", this.allowFlying);
        nbttagcompound.setBoolean("instabuild", this.isCreativeMode);
        nbttagcompound.setBoolean("mayBuild", this.allowEdit);
        nbttagcompound.setFloat("flySpeed", this.flySpeed);
        nbttagcompound.setFloat("walkSpeed", this.walkSpeed);
        tagCompound.setTag("abilities", nbttagcompound);
        // Wind charge
        nbttagcompound.setInteger("wind_burst_height", this.capabilityWindChargeFall.getWindBurstTime());
        nbttagcompound.setInteger("wind_burst_time", this.capabilityWindChargeFall.getWindBurstHeight());
        nbttagcompound.setBoolean("wind_charge_used", this.capabilityWindChargeFall.getUsedWindCharge());
    }

    public void readCapabilitiesFromNBT(NBTTagCompound tagCompound)
    {
        if (tagCompound.hasKey("abilities", 10))
        {
            NBTTagCompound nbttagcompound = tagCompound.getCompoundTag("abilities");
            this.disableDamage = nbttagcompound.getBoolean("invulnerable");
            this.isFlying = nbttagcompound.getBoolean("flying");
            this.allowFlying = nbttagcompound.getBoolean("mayfly");
            this.isCreativeMode = nbttagcompound.getBoolean("instabuild");

            if (nbttagcompound.hasKey("flySpeed", 99))
            {
                this.flySpeed = nbttagcompound.getFloat("flySpeed");
                this.walkSpeed = nbttagcompound.getFloat("walkSpeed");
            }

            if (nbttagcompound.hasKey("mayBuild", 1))
            {
                this.allowEdit = nbttagcompound.getBoolean("mayBuild");
            }
            // Wind charge
            if(nbttagcompound.hasKey("wind_burst_height")) {
                this.capabilityWindChargeFall.setWindBurstHeight(nbttagcompound.getInteger("wind_burst_height"));
            }
            if(nbttagcompound.hasKey("wind_burst_time")) {
                this.capabilityWindChargeFall.setWindBurstTime(nbttagcompound.getInteger("wind_burst_time"));
            }
            if(nbttagcompound.hasKey("wind_charge_used")) {
                this.capabilityWindChargeFall.setUsedWindCharge(nbttagcompound.getBoolean("wind_charge_used"));
            }
        }
    }

    public float getFlySpeed()
    {
        return this.flySpeed;
    }

    public void setFlySpeed(float speed)
    {
        this.flySpeed = speed;
    }

    public float getWalkSpeed()
    {
        return this.walkSpeed;
    }

    public void setPlayerWalkSpeed(float speed)
    {
        this.walkSpeed = speed;
    }
}
