    package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenStonePillar extends WorldGenerator
{
    IBlockState stoneBlock;
    int randHeight;
    int initialHeight;

    public WorldGenStonePillar(Block block, int randHeight, int initialHeight) {
        this.stoneBlock = block.getDefaultState();
        this.randHeight = randHeight;
        this.initialHeight = initialHeight;
    }

    public boolean generate(World worldIn, Random rand, BlockPos blockpos)
    {
        for (int i = 0; i < rand.nextInt(randHeight)+initialHeight; ++i)
        {
            blockpos=blockpos.up();
            if (worldIn.isAirBlock(blockpos))
            {
                int j = 1 + rand.nextInt(rand.nextInt(3) + 1);

                for (int k = 0; k < j; ++k)
                {
                    worldIn.setBlockState(blockpos.up(k), stoneBlock, 2);
                }
            }
        }

        return true;
    }
}
