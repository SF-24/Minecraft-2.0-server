package net.minecraft.world.biome;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.*;

import java.util.Random;

public class BiomeGenSwampDark extends BiomeGenBase
{
    private static final IBlockState JUNGLE_LOG = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
    private static final IBlockState OAK_LOG = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
    private static final IBlockState JUNGLE_LEAVES = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
    private static final IBlockState OAK_LEAVES = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
    protected static final WorldGenCanopyTree canopyTree = new WorldGenCanopyTree(false);


    public BiomeGenSwampDark(int id)
    {
        super(id);
        this.theBiomeDecorator.treesPerChunk = 8;
        this.theBiomeDecorator.flowersPerChunk = 1;
        this.theBiomeDecorator.deadBushPerChunk = 1;
        this.theBiomeDecorator.reedsPerChunk = 10;
        this.theBiomeDecorator.clayPerChunk = 1;
        this.theBiomeDecorator.waterlilyPerChunk = 4;
        this.theBiomeDecorator.sandPerChunk2 = 0;
        this.theBiomeDecorator.sandPerChunk = 0;
        this.theBiomeDecorator.grassPerChunk = 5;
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 1, 1, 4));
    }

    public void decorate(World worldIn, Random rand, BlockPos pos) {
        super.decorate(worldIn, rand, pos);

        if (rand.nextInt(2) == 0)
        {
            int i = rand.nextInt(16) + 8;
            int j = rand.nextInt(16) + 8;
            BlockPos blockpos = worldIn.getHeight(pos.add(i, 2, j)).up();
            (new WorldGenLakes(Blocks.water)).generate(worldIn, rand, blockpos);
        }
        if (rand.nextInt(2) == 0)
        {
            int i = rand.nextInt(16) + 8;
            int j = rand.nextInt(16) + 8;
            BlockPos blockpos = worldIn.getHeight(pos.add(i, 2, j)).up();
            (new WorldGenLakes(Blocks.water)).generate(worldIn, rand, blockpos);
        }
        if (rand.nextInt(2) == 0)
        {
            int i = rand.nextInt(16) + 8;
            int j = rand.nextInt(16) + 8;
            BlockPos blockpos = worldIn.getHeight(pos.add(i, 2, j)).up();
            (new WorldGenLakes(Blocks.water)).generate(worldIn, rand, blockpos);
        }
        if (rand.nextInt(2) == 0)
        {
            int i = rand.nextInt(16) + 8;
            int j = rand.nextInt(16) + 8;
            BlockPos blockpos = worldIn.getHeight(pos.add(i, 2, j)).up();
            (new WorldGenLakes(Blocks.water)).generate(worldIn, rand, blockpos);
        }
    }

    public WorldGenAbstractTree genBigTreeChance(Random rand)
    {
        return (WorldGenAbstractTree)
                (rand.nextInt(3) == 0 ? new WorldGenShrub(OAK_LOG, OAK_LEAVES) :
                        (rand.nextInt(3) == 0 ? canopyTree :
                                new WorldGenTrees(false, 4 + rand.nextInt(7),OAK_LOG , JUNGLE_LEAVES, true)));
    }

    public BlockFlower.EnumFlowerType pickRandomFlower(Random rand, BlockPos pos)
    {
        if(rand.nextInt(3)<=1) return BlockFlower.EnumFlowerType.BLUE_ORCHID;
        if(rand.nextInt(2)==0) return BlockFlower.EnumFlowerType.DANDELION;
        return BlockFlower.EnumFlowerType.POPPY;
    }

    public WorldGenerator getRandomWorldGenForGrass(Random rand)
    {
        return rand.nextInt(4) == 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }

    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        double d0 = GRASS_COLOR_NOISE.func_151601_a((double)x * 0.25D, (double)z * 0.25D);

        if (d0 > 0.0D)
        {
            int i = x & 15;
            int j = z & 15;

            for (int k = 255; k >= 0; --k)
            {
                if (chunkPrimerIn.getBlockState(j, k, i).getBlock().getMaterial() != Material.air)
                {
                    if (k == 62 && chunkPrimerIn.getBlockState(j, k, i).getBlock() != Blocks.water)
                    {
                        chunkPrimerIn.setBlockState(j, k, i, Blocks.water.getDefaultState());

                        if (d0 < 0.12D)
                        {
                            chunkPrimerIn.setBlockState(j, k + 1, i, Blocks.waterlily.getDefaultState());
                        }
                    }

                    break;
                }
            }
        }

        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }
}
