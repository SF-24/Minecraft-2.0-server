package net.mineshaft.cavegen;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CaveHeight {
    public static final CaveHeight SHALLOW = new CaveHeight(24, -1);
    public static final CaveHeight MEDIUM = new CaveHeight(24, -1);
    public static final CaveHeight DEEP = new CaveHeight(1, 24);

    private final int minHeight;
    private final int maxHeightIsSurface;

    private CaveHeight(int minHeight, int maxHeightOrSurface) {
        this.minHeight = minHeight;
        this.maxHeightIsSurface = maxHeightOrSurface;
    }

    public int getMinHeight() { return minHeight; }
    public int getMaxHeight(World world, BlockPos pos) {
        if (maxHeightIsSurface == -1) {
            return world.getPrecipitationHeight(pos).getY();
        }
        return maxHeightIsSurface;
    }

    public int getRange(World world, BlockPos pos) {
        return Math.max(1, getMaxHeight(world, pos) - minHeight);
    }
}

