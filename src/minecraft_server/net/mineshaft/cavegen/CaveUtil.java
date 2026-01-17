package net.mineshaft.cavegen;

import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class CaveUtil {

    public static void decorateCave(CaveType caveType, ChunkPrimer chunkPrimer, int localChunkX, int localChunkY, int localChunkZ, Random rand) {
        // small chance to place cobwebs here
        switch (rand.nextInt(10)) {
            case 0:
                if(rand.nextInt((2))==0) {
                    CaveDecorator.tryPlaceCobweb(chunkPrimer, localChunkX, localChunkY, localChunkZ, rand);
                }
                break;
            case 1:
                if(caveType==null) return;
                caveType.decorateMapGenCaves(chunkPrimer,localChunkX,localChunkY,localChunkZ,rand);
                break;
            default:
                break;
        }
    }

    public static boolean isSolid(ChunkPrimer primer, int x, int y, int z) {
        if(!inBounds(x,y,z)) return false;
        return primer.getBlockState(x, y, z).getBlock().getMaterial().isSolid();
    }

    public static boolean isAir(ChunkPrimer primer, int x, int y, int z) {
        if (!inBounds(x, y, z)) return false;
        return primer.getBlock(x, y, z) == 0;
    }

    public static boolean inBounds(int x, int y, int z) {
        return x >= 0 && x < 16
                && z >= 0 && z < 16
                && y >= 0 && y < 256;
    }

}
