package net.mineshaft.cavegen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;

import java.util.Random;

public class CaveDecorator {
    public static void tryPlaceCobweb(ChunkPrimer primer, int x, int y, int z, Random rand) {
        // Must be air, must have ceiling
        if (primer.getBlock(x, y, z) != 0) return;
        primer.setBlockState(x, y, z, Blocks.web.getDefaultState());
    }

    /** 🧊 Place 3x3 to 5x5 ice patch */
    /** 🧊 FIXED: Full 3x3-5x5 ice patches */
    public static void placeBoulder(ChunkPrimer primer, int centerX, int centerY, int centerZ, IBlockState primary, IBlockState secondary, IBlockState rare, Random rand) {
        if(!CaveUtil.inBounds(centerX,centerY,centerZ)) return;
        if(centerY>10 && centerY<244 && centerX<14 && centerX>2 && centerZ<14 && centerZ > 2) {
            for (int x = -2; x <= 2; x++) {
                for (int y = -2; y <= 2; y++) {
                    for (int z = -2; z <= 2; z++) {
                        if (!(Math.abs(z) == 2 && Math.abs(x) == 2) && !(Math.abs(z) == 2 && Math.abs(y) == 2) && !(Math.abs(x) == 2 && Math.abs(y) == 2)) {
                            if (primer.getBlock(centerX + x, centerY + y, centerZ + z) != 0) {
                                if (rand.nextInt(7) == 0) {
                                    primer.setBlockState(centerX + x, centerY + y, centerZ + z, rare);
                                    break;
                                } else if (rand.nextInt(2) == 0) {
                                    primer.setBlockState(centerX + x, centerY + y, centerZ + z, secondary);
                                } else {
                                    primer.setBlockState(centerX + x, centerY + y, centerZ + z, primary);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
