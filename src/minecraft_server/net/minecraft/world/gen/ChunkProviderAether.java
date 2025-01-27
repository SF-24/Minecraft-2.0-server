package net.minecraft.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChunkProviderAether implements IChunkProvider
{
    private Random aetherRNG;

    // Block states
    protected static final IBlockState STONE = Blocks.stone.getDefaultState();
    protected static final IBlockState AIR = Blocks.air.getDefaultState();
    protected static final IBlockState DIRT = Blocks.dirt.getDefaultState();
    protected static final IBlockState GRASS = Blocks.grass.getDefaultState();

    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
    public NoiseGeneratorOctaves noiseGen4;
    public NoiseGeneratorOctaves noiseGen5;

    private World aetherWorld;
    private final NoiseGeneratorSimplex islandNoise;

    // buffer
    private double[] densities;

    /** The biomes that are used to generate the chunk */
    private BiomeGenBase[] biomesForGeneration;

    // noise data
    double[] noiseData1;
    double[] noiseData2;
    double[] noiseData3;
    double[] noiseData4;
    double[] noiseData5;

    // unnamed field -> TODO: rename
    int[][] field_73203_h = new int[32][32];

    // island gen -> change to aether island
    private final WorldGenEndIsland aetherIslands = new WorldGenEndIsland();
    private final WorldGenTrees trees = new WorldGenTrees(false);

    // chunk generator
    public ChunkProviderAether(World worldIn, long seed)
    {
        this.aetherWorld = worldIn;
        this.aetherRNG = new Random(seed);
        this.noiseGen1 = new NoiseGeneratorOctaves(this.aetherRNG, 16);
        this.noiseGen2 = new NoiseGeneratorOctaves(this.aetherRNG, 16);
        this.noiseGen3 = new NoiseGeneratorOctaves(this.aetherRNG, 8);
        this.noiseGen4 = new NoiseGeneratorOctaves(this.aetherRNG, 10);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.aetherRNG, 16);
        //ported -> simplex noise
        this.islandNoise = new NoiseGeneratorSimplex(this.aetherRNG);
    }

    public void setBlocksInChunk(int x, int z, ChunkPrimer chunkPrimer)
    {
        int b0 = 2;
        int k = b0 + 1;
        int b1 = 33;
        int l = b0 + 1;
        this.densities = this.initializeNoiseField(this.densities, x * b0, 0, z * b0, k, b1, l);

        for (int i1 = 0; i1 < b0; ++i1)
        {
            for (int j1 = 0; j1 < b0; ++j1)
            {
                for (int k1 = 0; k1 < 32; ++k1)
                {
                    double d0 = 0.25D;
                    double d1 = this.densities[((i1 + 0) * l + j1 + 0) * b1 + k1 + 0];
                    double d2 = this.densities[((i1 + 0) * l + j1 + 1) * b1 + k1 + 0];
                    double d3 = this.densities[((i1 + 1) * l + j1 + 0) * b1 + k1 + 0];
                    double d4 = this.densities[((i1 + 1) * l + j1 + 1) * b1 + k1 + 0];
                    double d5 = (this.densities[((i1 + 0) * l + j1 + 0) * b1 + k1 + 1] - d1) * d0;
                    double d6 = (this.densities[((i1 + 0) * l + j1 + 1) * b1 + k1 + 1] - d2) * d0;
                    double d7 = (this.densities[((i1 + 1) * l + j1 + 0) * b1 + k1 + 1] - d3) * d0;
                    double d8 = (this.densities[((i1 + 1) * l + j1 + 1) * b1 + k1 + 1] - d4) * d0;

                    for (int l1 = 0; l1 < 4; ++l1)
                    {
                        double d9 = 0.125D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for (int i2 = 0; i2 < 8; ++i2)
                        {
                            double d14 = 0.125D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;

                            for (int j2 = 0; j2 < 8; ++j2)
                            {
                                IBlockState iblockstate = Blocks.air.getDefaultState();
                                int k2 = i2 + i1 * 8;
                                int l2 = l1 + k1 * 4;
                                int i3 = j2 + j1 * 8;

                                if (d15 > 0.0D)
                                {
                                    iblockstate = Blocks.stone.getDefaultState();
                                }

                                chunkPrimer.setBlockState(k2, l2, i3, iblockstate);
                                d15 += d16;
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }
    // /\ compared

    // replace blocks for biome
//    public void func_180519_a(ChunkPrimer primer)
    public void replaceBlocksForBiome(int x, int z, ChunkPrimer primer, BiomeGenBase[] biomeGens)
    {
        // TODO:
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    public Chunk provideChunk(int x, int z)
    {
        this.aetherRNG.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.biomesForGeneration = this.aetherWorld.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
        // generate terrain
        this.setBlocksInChunk(x, z, chunkprimer);
        Chunk chunk = new Chunk(this.aetherWorld, chunkprimer, x, z);
        byte[] abyte = chunk.getBiomeArray();

        for (int i = 0; i < abyte.length; ++i)
        {
            abyte[i] = (byte)this.biomesForGeneration[i].biomeID;
        }

        chunk.generateSkylightMap();
        this.replaceBlocksForBiome(x, z, chunkprimer, biomesForGeneration);
        return chunk;
    }

    /**
     * generates a subset of the level's terrain data. Takes 7 arguments: the [empty] noise array, the position, and the
     * size.
     */
    private double[] initializeNoiseField(double[] par1ArrayOfDouble, int par2, int par3, int par4, int par5, int par6, int par7)
    {
        if (par1ArrayOfDouble == null)
        {
            par1ArrayOfDouble = new double[par5 * par6 * par7];
        }

        double d0 = 684.412D;
        double d1 = 684.412D;
        this.noiseData4 = this.noiseGen4.generateNoiseOctaves(this.noiseData4, par2, par4, par5, par7, 1.121D, 1.121D, 0.5D);
        this.noiseData5 = this.noiseGen5.generateNoiseOctaves(this.noiseData5, par2, par4, par5, par7, 200.0D, 200.0D, 0.5D);
        d0 = d0 * 2.0D;
        this.noiseData1 = this.noiseGen3.generateNoiseOctaves(this.noiseData1, par2, par3, par4, par5, par6, par7, d0 / 80.0D, d1 / 160.0D, d0 / 80.0D);
        this.noiseData2 = this.noiseGen1.generateNoiseOctaves(this.noiseData2, par2, par3, par4, par5, par6, par7, d0, d1, d0);
        this.noiseData3 = this.noiseGen2.generateNoiseOctaves(this.noiseData3, par2, par3, par4, par5, par6, par7, d0, d1, d0);
        //!
        int k1 = 0;
        int l1 = 0;
        int i = 0;

        for (int i2 = 0; i2 < par5; ++i2)
        {
            for (int j2 = 0; j2 < par7; ++j2)
            {
                double d2 = (this.noiseData4[l1] + 256.0D) / 512.0D;

                if (d2 > 1.0D)
                {
                    d2 = 1.0D;
                }

                double d3 = this.noiseData5[l1] / 8000.0D;

                if (d3 < 0.0D)
                {
                    d3 = -d3 * 0.3D;
                }

                d3 = d3 * 3.0D - 2.0D;
                float f = (float)(i2 + par2) / 1.0F;
                float f1 = (float)(j2 + par4) / 1.0F;
                float f2 = 100.0F - MathHelper.sqrt_float(f * f + f1 * f1) * 8.0F;

                if (f2 > 80.0F)
                {
                    f2 = 80.0F;
                }

                if (f2 < -100.0F)
                {
                    f2 = -100.0F;
                }

                if (d3 > 1.0D)
                {
                    d3 = 1.0D;
                }

                d3 /= 8.0D;
                d3 = 0.0D;

                if (d2 < 0.0D)
                {
                    d2 = 0.0D;
                }

                d2 += 0.5D;
                d3 = d3 * (double)par6 / 16.0D;
                ++l1;
                double d4 = (double)par6 / 2.0D;

                for (int k2 = 0; k2 < par6; ++k2)
                {
                    double d5 = 0.0D;
                    double d6 = ((double)k2 - d4) * 8.0D / d2;

                    if (d6 < 0.0D)
                    {
                        d6 *= -1.0D;
                    }

                    double d7 = this.noiseData2[k1] / 512.0D;
                    double d8 = this.noiseData3[k1] / 512.0D;
                    double d9 = (this.noiseData1[k1] / 10.0D + 1.0D) / 2.0D;

                    if (d9 < 0.0D)
                    {
                        d5 = d7;
                    }
                    else if (d9 > 1.0D)
                    {
                        d5 = d8;
                    }
                    else
                    {
                        d5 = d7 + (d8 - d7) * d9;
                    }

                    d5 -= 8.0D;
                    d5 += (double)f2;
                    byte b0 = 2;
                    double d10;

                    if (k2 > par6 / 2 - b0)
                    {
                        d10 = (double)((float)(k2 - (par6 / 2 - b0)) / 64.0F);

                        if (d10 < 0.0D)
                        {
                            d10 = 0.0D;
                        }

                        if (d10 > 1.0D)
                        {
                            d10 = 1.0D;
                        }

                        d5 = d5 * (1.0D - d10) + -3000.0D * d10;
                    }

                    b0 = 8;

                    if (k2 < b0)
                    {
                        d10 = (double)((float)(b0 - k2) / ((float)b0 - 1.0F));
                        d5 = d5 * (1.0D - d10) + -30.0D * d10;
                    }

                    par1ArrayOfDouble[k1] = d5;
                    ++k1;
                }
            }
        }

        return par1ArrayOfDouble;
    }

    /**
     * Checks to see if a chunk exists at x, z
     */
    public boolean chunkExists(int x, int z)
    {
        return true;
    }

    /**
     * Populates chunk with ores etc etc
     */
    public void populate(IChunkProvider chunkProvider, int x, int z)
    {
        BlockFalling.fallInstantly = true;
        int k = x * 16;
        int l = z * 16;
        BlockPos blockpos = new BlockPos(k, 0, l);
        BiomeGenBase biomegenbase = this.aetherWorld.getBiomeGenForCoords(blockpos.add(16, 0, 16));
        long i = (long)x * (long)x + (long)z * (long)z;

        // dungeons
        for (int j2 = 0; j2 < 80 /*dungeon chance*/; ++j2)
        {
            int i3 = this.aetherRNG.nextInt(16) + 8;
            int l3 = this.aetherRNG.nextInt(256);
            int l1 = this.aetherRNG.nextInt(16) + 8;
            (new WorldGenAetherDungeons()).generate(this.aetherWorld, this.aetherRNG, blockpos.add(i3, l3, l1));
        }

        //if (i > 4096L) {
        // changing value affects how many mini islands there are
        if (i > 220L)
        {
            float f = this.getIslandHeightValue(x, z, 1, 1);

            if (f < -20.0F && this.aetherRNG.nextInt(14) == 0)
            {
                this.aetherIslands.generate(this.aetherWorld, this.aetherRNG, blockpos.add(this.aetherRNG.nextInt(16) + 8, 55 + this.aetherRNG.nextInt(16), this.aetherRNG.nextInt(16) + 8));

                if (this.aetherRNG.nextInt(4) == 0)
                {
                    this.aetherIslands.generate(this.aetherWorld, this.aetherRNG, blockpos.add(this.aetherRNG.nextInt(16) + 8, 55 + this.aetherRNG.nextInt(16), this.aetherRNG.nextInt(16) + 8));
                }
            }

            if (this.getIslandHeightValue(x, z, 1, 1) > 40.0F)
            {
                // tree chance?
                int j = this.aetherRNG.nextInt(5);

                for (int n = 0; n < j; ++n)
                {
                    int lm = this.aetherRNG.nextInt(16) + 8;
                    int i1 = this.aetherRNG.nextInt(16) + 8;
                    int j1 = this.aetherWorld.getHeight(blockpos.add(lm, 0, i1)).getY();

                    if (j1 > 0)
                    {
                        int k1 = j1 - 1;

                        if (this.aetherWorld.isAirBlock(blockpos.add(lm, k1 + 1, i1)) && this.aetherWorld.getBlockState(blockpos.add(lm, k1, i1)).getBlock() == Blocks.stone)
                        {
                            aetherWorld.setBlockState(blockpos.add(lm, k1, i1), GRASS);

                            if (aetherRNG.nextInt(35) != 0)
                            {
                                trees.generate(aetherWorld, aetherRNG, blockpos.add(lm, k1 + 1, i1));
                            }
                        }
                    }
                }
            }
        }

        // lakes
        if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills)
        {
            int i1 = this.aetherRNG.nextInt(16) + 8;
            int j1 = this.aetherRNG.nextInt(256);
            int k1 = this.aetherRNG.nextInt(16) + 8;
            (new WorldGenLakes(Blocks.water)).generate(this.aetherWorld, this.aetherRNG, blockpos.add(i1, j1, k1));
        }

        // glowstone
        int i1 = this.aetherRNG.nextInt(16) + 8;
        int j1 = this.aetherRNG.nextInt(256);
        int k1 = this.aetherRNG.nextInt(16) + 8;
        (new WorldGenGlowStone1()).generate(this.aetherWorld, this.aetherRNG, blockpos.add(i1, j1, k1));
        int x1 = this.aetherRNG.nextInt(16) + 8;
        int y1 = this.aetherRNG.nextInt(256);
        int z1 = this.aetherRNG.nextInt(16) + 8;
        (new WorldGenGlowStone1()).generate(this.aetherWorld, this.aetherRNG, blockpos.add(i1, j1, k1));
        this.aetherWorld.getBiomeGenForCoords(blockpos.add(k + 16, 0, l + 16)).decorate(this.aetherWorld, this.aetherWorld.rand, blockpos);
        BlockFalling.fallInstantly = false;
    }

    public boolean populateChunk(IChunkProvider chunkProvider, Chunk chunkIn, int x, int z)
    {
        return false;
    }

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    public boolean saveChunks(boolean saveAllChunks, IProgressUpdate progressCallback)
    {
        return true;
    }

    /**
     * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
     * unimplemented.
     */
    public void saveExtraData()
    {
    }

    /**
     * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
     */
    public boolean unloadQueuedChunks()
    {
        return false;
    }

    /**
     * Returns if the IChunkProvider supports saving.
     */
    public boolean canSave()
    {
        return true;
    }

    /**
     * Converts the instance data to a readable string.
     */
    public String makeString()
    {
        return "RandomLevelSource";
    }

    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        return this.aetherWorld.getBiomeGenForCoords(pos).getSpawnableList(creatureType);
    }

    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position)
    {
        return null;
    }

    public int getLoadedChunkCount()
    {
        return 0;
    }

    public void recreateStructures(Chunk chunkIn, int x, int z)
    {
    }

    public Chunk provideChunk(BlockPos blockPosIn)
    {
        return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
    }

    //HEIGHT VALUE
    private float getIslandHeightValue(int p_185960_1_, int p_185960_2_, int p_185960_3_, int p_185960_4_)
    {
        float f = (float)(p_185960_1_ * 2 + p_185960_3_);
        float f1 = (float)(p_185960_2_ * 2 + p_185960_4_);
        float f2 = 100.0F - MathHelper.sqrt_float(f * f + f1 * f1) * 8.0F;

        if (f2 > 80.0F)
        {
            f2 = 80.0F;
        }

        if (f2 < -100.0F)
        {
            f2 = -100.0F;
        }

        for (int i = -12; i <= 12; ++i)
        {
            for (int j = -12; j <= 12; ++j)
            {
                long k = (long)(p_185960_1_ + i);
                long l = (long)(p_185960_2_ + j);

                if (k * k + l * l > 4096L && this.islandNoise.getValue((double)k, (double)l) < -0.8999999761581421D)
                {
                    float f3 = (MathHelper.abs((float)k) * 3439.0F + MathHelper.abs((float)l) * 147.0F) % 13.0F + 9.0F;
                    f = (float)(p_185960_3_ - i * 2);
                    f1 = (float)(p_185960_4_ - j * 2);
                    float f4 = 100.0F - MathHelper.sqrt_float(f * f + f1 * f1) * f3;

                    if (f4 > 80.0F)
                    {
                        f4 = 80.0F;
                    }

                    if (f4 < -100.0F)
                    {
                        f4 = -100.0F;
                    }

                    if (f4 > f2)
                    {
                        f2 = f4;
                    }
                }
            }
        }

        return f2;
    }
}