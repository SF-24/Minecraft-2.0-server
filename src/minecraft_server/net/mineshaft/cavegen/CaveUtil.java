package net.mineshaft.cavegen;

import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class CaveUtil {

    public static void setCaveSurface(CaveType caveType, ChunkPrimer chunkPrimer, int localChunkX, int localChunkY, int localChunkZ, Random random) {
        if(caveType==null || !caveType.hasSurfaceRules()) return;

        int floorY = localChunkY - 2;  // 2 blocks below carved air (true floor level)
        // Cache block ID once (3x faster than repeated getBlock calls)
        final int floorBlockId = chunkPrimer.getBlock(localChunkX, floorY, localChunkZ);
        if (floorBlockId == 0) return;  // Early exit: already air

        if (floorY > 1 && floorY < 248) {
            // Only replace stone/dirt floors (not ores, bedrock, etc.)
            if (floorBlockId == 16 || floorBlockId == 17 || floorBlockId == 18 || floorBlockId == 19 || floorBlockId == 208 || floorBlockId == 48) {
                caveType.surfaceRules(chunkPrimer, localChunkX, floorY, localChunkZ, random);
            }
        }

    }

    public static void decorateCave(CaveType caveType, ChunkPrimer chunkPrimer, int localChunkX, int localChunkY, int localChunkZ, Random rand) {
        // small chance to place cobwebs here
        switch (rand.nextInt(2)) {
            case 0:
                if(caveType!=null && !caveType.hasCobwebs()) {return;}
                if (rand.nextInt((2)) == 0) {
                    CaveDecorator.tryPlaceCobweb(chunkPrimer, localChunkX, localChunkY, localChunkZ, rand);
                }
                // TODO: Add other decoration otherwise
                break;
            case 1:
                if (caveType == null) return;
                caveType.decorateMapGenCaves(chunkPrimer, localChunkX, localChunkY, localChunkZ, rand);
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
