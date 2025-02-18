package net.minecraft.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenEndIsland extends WorldGenerator
{
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        float f = (float)(rand.nextInt(3) + 4);
        float backupF = f;
        int highestI = 0;

        for (int i = 0; f > 0.5F; --i)
        {
            for (int j = MathHelper.floor_float(-f); j <= MathHelper.ceiling_float_int(f); ++j)
            {
                for (int k = MathHelper.floor_float(-f); k <= MathHelper.ceiling_float_int(f); ++k)
                {
                    if ((float)(j * j + k * k) <= (f + 1.0F) * (f + 1.0F))
                    {
                        this.setBlockAndNotifyAdequately(worldIn, position.add(j, i, k), Blocks.dirt.getDefaultState());
                    }
                }
            }

            if (i > highestI)
            {
                highestI = i;
                backupF = f;
            }

            f = (float)((double)f - ((double)rand.nextInt(2 /* WAS 2 */) + 0.5D));
            // changing number
            //f = (float)((double)f - ((double)rand.nextInt(2) + 0.5D));
        }

        int i = highestI;

        for (int j = MathHelper.floor_float(-backupF); j <= MathHelper.ceiling_float_int(backupF); ++j)
        {
            for (int k = MathHelper.floor_float(-backupF); k <= MathHelper.ceiling_float_int(backupF); ++k)
            {
                if ((float)(j * j + k * k) <= (backupF + 1.0F) * (backupF + 1.0F))
                {
                    this.setBlockAndNotifyAdequately(worldIn, position.add(j, i, k), Blocks.grass.getDefaultState());
                }
            }
        }

        return true;
    }
}
