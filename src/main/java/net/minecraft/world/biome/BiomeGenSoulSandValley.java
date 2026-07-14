package net.minecraft.world.biome;

import net.minecraft.entity.monster.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenStonePillar;

import java.util.Random;

public class BiomeGenSoulSandValley extends BiomeGenBase
{

    public BiomeGenSoulSandValley(int id)
    {
        super(id);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.add(new SpawnListEntry(EntityGhast.class, 100, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityPigZombie.class, 10, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityEnderman.class, 1, 1, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntityMagmaCube.class, 5 /*was 1*/, 4, 4));
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 100, 4, 4));
        this.topBlock = Blocks.soul_sand.getDefaultState();
        this.fillerBlock = Blocks.soul_sand.getDefaultState();
    }

    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        super.genTerrainBlocks(worldIn,rand,chunkPrimerIn,x,z,noiseVal);
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

    public void decorate(World world, Random rand, BlockPos pos) {
        super.decorate(world,rand,pos);

        // Fossils/Pillars
        if (rand.nextInt(8) == 0)
        {
            int i = rand.nextInt(16) + 8;
            int j = rand.nextInt(16) + 8;
            BlockPos blockpos = world.getHeight(pos.add(i, 0, j)).up();
            (new WorldGenStonePillar(Blocks.obsidian,5,5)).generate(world, rand, blockpos);
        }
    }
}
