package net.mineshaft.cavegen;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
                                if (!rare.getBlock().equals(Blocks.air) && rand.nextInt(7) == 0) {
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

    public static void placeCaveFoliage(ChunkPrimer primer, int centreX, int centreY, int centreZ, IBlockState wood, IBlockState leaves, Random rand) {
        Block block;

        if(centreX>14) centreX=14;
        if(centreZ>14) centreZ=14;
        if(centreX<1) centreX=1;
        if(centreZ<1) centreZ=1;

        while(primer.getBlock(centreX,centreY,centreZ)!=0) {
            centreY--;
            if(centreY<20) return;
        }

        while ((primer.getBlock(centreX,centreY,centreZ)==0 || primer.getBlockState(centreX,centreY,centreZ).getBlock().getMaterial() == Material.leaves) && centreY > 0)
        {
            centreY--;
            if(centreY<20) return;
        }

        Block block1 = primer.getBlockState(centreX,centreY,centreZ).getBlock();

        if (block1 == Blocks.dirt || block1 == Blocks.grass || block1 == Blocks.stone || block1 == Blocks.gravel || block1 == Blocks.clay)
        {
            centreY++;
            primer.setBlockState(centreX,centreY,centreZ, wood);

            for (int i = centreY-2; i <= centreY + 3; ++i)
            {
                int j = i - centreY;
                int k = 1 /*was 2*/ - j;

                for (int l = centreX - k; l <= centreX + k; ++l)
                {
                    int i1 = l - centreX;

                    for (int j1 = centreZ - k; j1 <= centreZ + k; ++j1)
                    {
                        int k1 = j1 - centreZ;

                        if (Math.abs(i1) != k || Math.abs(k1) != k || rand.nextInt(2) != 0)
                        {
//                            System.out.println("centre: " + centreX + " " + centreY + " " + centreZ);
//                            System.out.println("co-ords: " + l + " " + i + " " + j1);
                            if (j1>=0&&j1<16&&l>=0&&l<16&&i>12&&i<254) {
                                if(i>=centreY) {
                                    if(primer.getBlock(l,i,j1)==0 || isCarvableBlock(primer.getBlock(l,i,j1),primer.getBlock(l,i,j1+1))) {
                                        primer.setBlockState(l, i, j1, leaves);
                                    }
                                } else if(primer.getBlock(l,i,j1)==0){
                                    primer.setBlockState(l, i, j1, leaves);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected static boolean isCarvableBlock(int idOfBlockToRemove, int idOfBlockAbove)
    {
        return (idOfBlockToRemove == 16 || (idOfBlockToRemove == 48 || (idOfBlockToRemove == 32 || (idOfBlockToRemove == Block.getMultipliedIdFromBlock(Blocks.hardened_clay) || (idOfBlockToRemove == Block.getMultipliedIdFromBlock(Blocks.stained_hardened_clay) || (idOfBlockToRemove == 384) || (idOfBlockToRemove == 2864) || (idOfBlockToRemove == 1760) || (idOfBlockToRemove == 1248) || (idOfBlockToRemove == 192) || idOfBlockToRemove == 208)) &&
                (idOfBlockAbove != Block.getMultipliedIdFromBlock(Blocks.water) && idOfBlockAbove != Block.getMultipliedIdFromBlock(Blocks.flowing_water)))));
    }

}
