package net.mineshaft.cavegen;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class CaveType {
    protected final boolean useSurfaceRules;
    protected String name;
    protected CaveHeight height;
    protected int decorationCount;
    protected boolean spawnCobwebs;

    public CaveType(CaveHeight height, String name, boolean useSurfaceRules)
    {
        this.decorationCount=1;
        this.name = name;
        this.spawnCobwebs=true;
        this.height = height;
        this.useSurfaceRules=useSurfaceRules;
    }

    public CaveType setDecorationCount(int decorationCount) {
        if(decorationCount>10) {
            System.out.println("Warning! High decoration count declared in " + this.getClass().getName() + ", aborting!");
            return this;
        }
        this.decorationCount=decorationCount;
        return this;
    }

    public CaveType setCobwebs(boolean spawnCobwebs) {
        this.spawnCobwebs=spawnCobwebs;
        return this;
    }

    public int getDecorationCount() {return this.decorationCount;}
    public String getName() {return this.name;}
    public boolean hasSurfaceRules() {return this.useSurfaceRules;}
    public boolean hasCobwebs() {return this.spawnCobwebs;}

    public void decorateMapGenCaves(ChunkPrimer chunkPrimer, int localChunkX, int localChunkY, int localChunkZ, Random rand) {

    }

    public void decorate(World worldIn, Random rand, BiomeGenBase biomeGenBase, BlockPos pos) {

    }

    public void surfaceRules(ChunkPrimer chunkPrimer, int localChunkX, int localChunkY, int localChunkZ, Random random) {

    }

//
//    /**
//     * Iterator for cave decoration positions within this cave type's height range.
//     * Handles all coordinate/height logic automatically.
//     */
//    protected void forEachCavePosition(World world, Random rand, BlockPos chunkPos, int attempts, PositionCallback callback) {
//        int yMin = this.height.getMinHeight();
//        int yRange = this.height.getRange(world, chunkPos);
//        int baseX = chunkPos.getX();
//        int baseZ = chunkPos.getZ();
//
//        for (int n = 0; n < attempts; n++) {
//            int localX = rand.nextInt(16);  // 0-15
//            int localZ = rand.nextInt(16);  // 0-15
//            int worldX = baseX + localX;
//            int worldZ = baseZ + localZ;
//            int y = yMin + rand.nextInt(yRange);
//
//            if (y < 0 || y >= 256) continue;
//
//            callback.onPosition(worldX, y, worldZ, rand);
//        }
//    }
//
//    /**
//     * Callback interface for cave position processing
//     */
//    @FunctionalInterface
//    protected interface PositionCallback {
//        void onPosition(int worldX, int y, int worldZ, Random rand);
//    }
//
//    /**
//     * Safe cave floor validation using isAirBlock() only
//     */
//    protected boolean isValidCaveFloor(World world, BlockPos.MutableBlockPos pos) {
//        if (pos.getY() < 0 || pos.getY() >= 256) return false;
//
//        // Target (y): air
//        if (!world.isAirBlock(pos)) return false;
//
//        // Floor (y-1): solid
//        if (world.isAirBlock(pos.down())) return false;
//
//        // Ceiling gap (y+1): air
//        if (!world.isAirBlock(pos.up())) return false;
//
//        return true;
//    }
}
