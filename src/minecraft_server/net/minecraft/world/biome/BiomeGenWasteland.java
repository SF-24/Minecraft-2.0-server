package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

public class BiomeGenWasteland extends BiomeGenBase
{
    private static final IBlockState OAK_LOG = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);

    public BiomeGenWasteland(int id)
    {
        super(id);
        this.hasBeach=false;
        this.spawnableCreatureList.clear();
        this.topBlock = Blocks.gravel.getDefaultState();
        this.fillerBlock = Blocks.stone.getDefaultState();
        this.theBiomeDecorator.treesPerChunk = 1;
        this.theBiomeDecorator.deadBushPerChunk = 2;
        this.theBiomeDecorator.reedsPerChunk = 50;
        this.theBiomeDecorator.cactiPerChunk = 10;
        this.spawnableCreatureList.clear();

    }

    public void decorate(World worldIn, Random rand, BlockPos pos)
    {
        super.decorate(worldIn, rand, pos);

        if (rand.nextInt(55) == 0)
        {
            int i = rand.nextInt(16) + 8;
            int j = rand.nextInt(16) + 8;
            BlockPos blockpos = worldIn.getHeight(pos.add(i, 2, j)).up();
            (new WorldGenMeteor()).generate(worldIn, rand, blockpos);
        } else if (rand.nextInt(3) == 0)
        {
            int i = rand.nextInt(16) + 8;
            int j = rand.nextInt(16) + 8;
            BlockPos blockpos = worldIn.getHeight(pos.add(i, rand.nextInt(4), j)).up();
            (new WorldGenLakes(Blocks.lava)).generate(worldIn, rand, blockpos);
        }
    }

    public WorldGenAbstractTree genBigTreeChance(Random rand)
    {
        return (WorldGenAbstractTree)
                (rand.nextInt(40) == 0 ? new WorldGenRock(Blocks.obsidian.getDefaultState(), Blocks.obsidian.getDefaultState(),rand.nextInt(2)+1) :
                        (rand.nextInt(11) == 0 ? new WorldGenRock(Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(),rand.nextInt(2)+1) :
                                (rand.nextInt(11) == 0 ? new WorldGenTreesAnySurface(false, 3 + rand.nextInt(3), OAK_LOG, Blocks.air.getDefaultState(), true, true):
                                        (new WorldGenTreesAnySurface(false, 0, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false, true)))));
    }
}
