package net.minecraft.world.gen.feature;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenAetherPillar extends WorldGenerator
{
    private final IBlockState brickSlab = Blocks.stone_slab.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.STONE).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
    private final IBlockState bricks = Blocks.stonebrick.getDefaultState();
    private final IBlockState flowingWater = Blocks.flowing_water.getDefaultState();
    private final IBlockState ruby = Blocks.ruby_block.getDefaultState();

    ArrayList<BlockPos> blockIgnore = new ArrayList<>();

    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        return false;
    }

    public ArrayList<BlockPos> generateBlocks(World worldIn, Random rand, BlockPos position)
    {
        while (worldIn.isAirBlock(position) && position.getY() > 2)
        {
            position = position.down();
        }

        if (false == true)
        {
            return blockIgnore;
        }
        else
        {
            for (int i = -2; i <= 2; ++i)
            {
                for (int j = -2; j <= 2; ++j)
                {
                    if (worldIn.isAirBlock(position.add(i, -1, j)) && worldIn.isAirBlock(position.add(i, -2, j)))
                    {
                        return blockIgnore;
                    }
                }
            }

            for (int l = -1; l <= 0; ++l)
            {
                for (int l1 = -2; l1 <= 2; ++l1)
                {
                    for (int k = -2; k <= 2; ++k)
                    {
                        //? bottom
                        int probability = rand.nextInt(100);

                        if (probability >= 85)
                        {
                            worldIn.setBlockState(position.add(l1, l, k), this.ruby, 2);
                        }
                        else
                        {
                            worldIn.setBlockState(position.add(l1, l, k), this.bricks, 2);
                        }

                        blockIgnore.add(position.add(l1, l, k));
                    }
                }
            }

            worldIn.setBlockState(position, this.flowingWater, 2);
            blockIgnore.add(position);

            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                worldIn.setBlockState(position.offset(enumfacing), this.flowingWater, 2);
            }

            for (int i1 = -2; i1 <= 2; ++i1)
            {
                for (int i2 = -2; i2 <= 2; ++i2)
                {
                    if (i1 == -2 || i1 == 2 || i2 == -2 || i2 == 2)
                    {
                        // sides of well
                        worldIn.setBlockState(position.add(i1, 1, i2), this.bricks, 2);
                    }
                }
            }

            worldIn.setBlockState(position.add(2, 1, 0), this.brickSlab, 2);
            worldIn.setBlockState(position.add(-2, 1, 0), this.brickSlab, 2);
            worldIn.setBlockState(position.add(0, 1, 2), this.brickSlab, 2);
            worldIn.setBlockState(position.add(0, 1, -2), this.brickSlab, 2);
            blockIgnore.add(position.add(2, 1, 0));
            blockIgnore.add(position.add(-2, 1, 0));
            blockIgnore.add(position.add(0, 1, 2));
            blockIgnore.add(position.add(0, 1, -2));

            for (int j1 = -1; j1 <= 1; ++j1)
            {
                for (int j2 = -1; j2 <= 1; ++j2)
                {
                    if (j1 == 0 && j2 == 0)
                    {
                        // tip
                        worldIn.setBlockState(position.add(j1, 4, j2), this.bricks, 2);
                    }
                    else
                    {
                        worldIn.setBlockState(position.add(j1, 4, j2), this.brickSlab, 2);
                    }

                    blockIgnore.add(position.add(j1, 4, j2));
                }
            }

            for (int k1 = 1; k1 <= 3; ++k1)
            {
                // pillars of well
                worldIn.setBlockState(position.add(-1, k1, -1), this.bricks, 2);
                worldIn.setBlockState(position.add(-1, k1, 1), this.bricks, 2);
                worldIn.setBlockState(position.add(1, k1, -1), this.bricks, 2);
                worldIn.setBlockState(position.add(1, k1, 1), this.bricks, 2);
                blockIgnore.add(position.add(-1, k1, -1));
                blockIgnore.add(position.add(-1, k1, 1));
                blockIgnore.add(position.add(1, k1, -1));
                blockIgnore.add(position.add(1, k1, 1));
            }

            return blockIgnore;
        }
    }
}
