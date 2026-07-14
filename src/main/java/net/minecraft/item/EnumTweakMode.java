package net.minecraft.item;

public enum EnumTweakMode
{
    DEFAULT(true, true, false, false),
    MODERN(true, false, false, true),
    CLASSIC_MODE(false, false, false, false);
    //VIKING_MODE(false, false, true);

    private final boolean naturalRegen;
    private final boolean starvationDamage;
    private final boolean regainStamina;
    private final boolean sprintDrain;

    EnumTweakMode(boolean naturalRegenaration, boolean starvationDamage, boolean regainStamina, boolean sprintDrain)
    {
        this.naturalRegen = naturalRegenaration;
        this.starvationDamage = starvationDamage;
        this.regainStamina = regainStamina;
        this.sprintDrain = sprintDrain;
    }

    public boolean enableNaturalRegen()
    {
        return naturalRegen;
    }

    public boolean enableStarvationDamage()
    {
        return starvationDamage;
    }

    public boolean enableRegainStamina()
    {
        return regainStamina;
    }

    public boolean enableSprintDrain()
    {
        return sprintDrain;
    }
}
