package net.minecraft.entity.player;

public class CapabilityWindChargeFall {

    private int windBurstTime;
    private int windBurstHeight;
    private boolean usedWindCharge;

    public CapabilityWindChargeFall() {

    }

    public void setUsedWindCharge(boolean value) {
        this.usedWindCharge = value;
    }

    public void setWindBurstHeight(int value) {
        this.windBurstHeight=value;
    }

    public void setWindBurstTime(int value) {
        this.windBurstTime=value;
    }

    public int getWindBurstTime() {
        return windBurstTime;
    }

    public int getWindBurstHeight() {
        return windBurstHeight;
    }

    public boolean getUsedWindCharge() {
        return usedWindCharge;
    }

}
