package net.minecraft.world.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;

import static net.minecraft.block.BlockSand.VARIANT;

public class ChunkProviderGenerate implements IChunkProvider
{
    /** RNG. */
    private Random rand;
    private NoiseGeneratorOctaves field_147431_j;
    private NoiseGeneratorOctaves field_147432_k;
    private NoiseGeneratorOctaves field_147429_l;
    private NoiseGeneratorPerlin field_147430_m;

    /** A NoiseGeneratorOctaves used in generating terrain */
    public NoiseGeneratorOctaves noiseGen5;

    /** A NoiseGeneratorOctaves used in generating terrain */
    public NoiseGeneratorOctaves noiseGen6;
    public NoiseGeneratorOctaves mobSpawnerNoise;

    /** Reference to the World object. */
    private World worldObj;

    /** are map structures going to be generated (e.g. strongholds) */
    private final boolean mapFeaturesEnabled;
    private WorldType field_177475_o;
    private final double[] field_147434_q;
    private final float[] parabolicField;
    private ChunkProviderSettings settings;
    private Block field_177476_s = Blocks.water;
    private double[] stoneNoise = new double[256];
    private MapGenBase caveGenerator = new MapGenCaves();

    /** Holds Stronghold Generator */
    private MapGenStronghold strongholdGenerator = new MapGenStronghold();

    /** Holds Village Generator */
    private MapGenVillage villageGenerator = new MapGenVillage();

    /** Holds Mineshaft Generator */
    private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
    private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();

    /** Holds ravine generator */
    private MapGenBase ravineGenerator = new MapGenRavine();
    private StructureOceanMonument oceanMonumentGenerator = new StructureOceanMonument();

    /** The biomes that are used to generate the chunk */
    private BiomeGenBase[] biomesForGeneration;
    double[] field_147427_d;
    double[] field_147428_e;
    double[] field_147425_f;
    double[] field_147426_g;

    public ChunkProviderGenerate(World worldIn, long p_i45636_2_, boolean p_i45636_4_, String p_i45636_5_)
    {
        this.worldObj = worldIn;
        this.mapFeaturesEnabled = p_i45636_4_;
        this.field_177475_o = worldIn.getWorldInfo().getTerrainType();
        this.rand = new Random(p_i45636_2_);
        this.field_147431_j = new NoiseGeneratorOctaves(this.rand, 16);
        this.field_147432_k = new NoiseGeneratorOctaves(this.rand, 16);
        this.field_147429_l = new NoiseGeneratorOctaves(this.rand, 8);
        this.field_147430_m = new NoiseGeneratorPerlin(this.rand, 4);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
        this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
        this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.field_147434_q = new double[825];
        this.parabolicField = new float[25];

        for (int i = -2; i <= 2; ++i)
        {
            for (int j = -2; j <= 2; ++j)
            {
                float f = 10.0F / MathHelper.sqrt_float((float)(i * i + j * j) + 0.2F);
                this.parabolicField[i + 2 + (j + 2) * 5] = f;
            }
        }

        if (p_i45636_5_ != null)
        {
            this.settings = ChunkProviderSettings.Factory.jsonToFactory(p_i45636_5_).func_177864_b();
            this.field_177476_s = this.settings.useLavaOceans ? Blocks.lava : Blocks.water;
            worldIn.func_181544_b(this.settings.seaLevel);
        }
    }

    public void setBlocksInChunk(int p_180518_1_, int p_180518_2_, ChunkPrimer p_180518_3_)
    {
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, p_180518_1_ * 4 - 2, p_180518_2_ * 4 - 2, 10, 10);
        this.func_147423_a(p_180518_1_ * 4, 0, p_180518_2_ * 4);

        for (int i = 0; i < 4; ++i)
        {
            int j = i * 5;
            int k = (i + 1) * 5;

            for (int l = 0; l < 4; ++l)
            {
                int i1 = (j + l) * 33;
                int j1 = (j + l + 1) * 33;
                int k1 = (k + l) * 33;
                int l1 = (k + l + 1) * 33;

                for (int i2 = 0; i2 < 32; ++i2)
                {
                    double d0 = 0.125D;
                    double d1 = this.field_147434_q[i1 + i2];
                    double d2 = this.field_147434_q[j1 + i2];
                    double d3 = this.field_147434_q[k1 + i2];
                    double d4 = this.field_147434_q[l1 + i2];
                    double d5 = (this.field_147434_q[i1 + i2 + 1] - d1) * d0;
                    double d6 = (this.field_147434_q[j1 + i2 + 1] - d2) * d0;
                    double d7 = (this.field_147434_q[k1 + i2 + 1] - d3) * d0;
                    double d8 = (this.field_147434_q[l1 + i2 + 1] - d4) * d0;

                    for (int j2 = 0; j2 < 8; ++j2)
                    {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for (int k2 = 0; k2 < 4; ++k2)
                        {
                            double d14 = 0.25D;
                            double d16 = (d11 - d10) * d14;
                            double lvt_45_1_ = d10 - d16;

                            for (int l2 = 0; l2 < 4; ++l2)
                            {
                                if ((lvt_45_1_ += d16) > 0.0D)
                                {
                                    primer.setBlockFromId(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, (short)16);
                                }
                                else if (i2 * 8 + j2 < this.settings.seaLevel)
                                {
                                    primer.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, this.oceanBlockTmpl.getDefaultState());
                                }
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

//    public void replaceBlocksForBiome(int x, int z, ChunkPrimer primer, BiomeGenBase[] biomesIn)
//    {
//        IBlockState BEDROCK = Blocks.bedrock.getDefaultState();
//        IBlockState AIR  = Blocks.air.getDefaultState();
//        IBlockState STONE = Blocks.stone.getDefaultState();
//        IBlockState GRAVEL = Blocks.gravel.getDefaultState();
//        IBlockState GOLD_ORE = Blocks.gold_ore.getDefaultState();
//        IBlockState SAND = Blocks.sand.getDefaultState();
//        IBlockState SNOW = Blocks.snow.getDefaultState();
//        IBlockState WATER = Blocks.water.getDefaultState();
//        IBlockState SANDSTONE = Blocks.sandstone.getDefaultState();
//        byte byte0 = 63;
//        double d = 0.03125D;
//        double[] sandNoise = null;
//        double[] gravelNoise = null;
//        sandNoise = this.field_147429_l.generateNoiseOctaves(sandNoise, x * 16,/*10*/0, z * 16, /*20*/16, 1, /*20*/16, d, 1.0D, d);
//        gravelNoise = this.field_147429_l.generateNoiseOctaves(gravelNoise, x * 8, 109/*109.0134D*/, z * 8, 22, 1, 22, d, 1.0D, d);
//
//        for (int k = 0; k < 16; k++)
//        {
//            for (int l = 0; l < 16; l++)
//            {
//                BiomeGenBase biomegenbase = biomesIn[k + l * 16];
////                BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(new BlockPos(x, 64, z));
//                boolean forceSandBeach = false;
//                List<BiomeGenBase> biomes = new ArrayList<>(Collections.emptyList());
//                biomes.add(BiomeGenBase.jungle);
//                biomes.add(BiomeGenBase.jungleEdge);
//                biomes.add(BiomeGenBase.jungleHills);
//                biomes.add(BiomeGenBase.desert);
//                biomes.add(BiomeGenBase.beach);
//                double sandV = sandNoise[k + l * 16] + rand.nextDouble() * 0.20000000000000001D;
//                double v1 = gravelNoise[k + l * 16] + rand.nextDouble() * 0.20000000000000001D;
//
//                if (biomes.contains(biomegenbase))
//                {
//                    sandV += 20;
//                }
//
//                // flag 1 = sand
//                // flag 2 = gravel
//                boolean flag = sandV > 1D;//0.0D;
//                boolean flag1 = v1 > 14;//3D;
//
//                if (flag)
//                {
//                    if (v1 >= (sandV + 19.0d))
//                    {
//                        flag = false;
//                        flag1 = true;
//                        //System.out.println("gravel override");
//                    }
//                }
//
//                if (biomegenbase.equals(BiomeGenBase.beach))
//                {
//                    flag = true;
//                    flag1 = false;
//                }
//
//                if (biomegenbase.equals(BiomeGenBase.coldBeach))
//                {
//                    flag1 = true;
//                    flag = false;
//                }
//
//                if (biomegenbase.equals(BiomeGenBase.desert) || biomegenbase.equals(BiomeGenBase.mesa))
//                {
//                    flag1 = false;
//                    flag = false;
//                }
//
//                //System.out.println(flag + " |sand| " + v);
//                //System.out.println(flag1 + " |gravel| " + v1);
//                int i1 = (int)(stoneNoise[k + l * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
//                int j1 = -1;
//                IBlockState byte1 = biomegenbase.topBlock;
//                IBlockState byte2 = biomegenbase.fillerBlock;
//
//                for (int k1 = 255; k1 >= 0; k1--)
//                {
//                    int l1 = (l * 16 + k) * 256 + k1;
//
//                    if (k1 <= /*rand.nextInt(1)*/0)
//                    {
//                        // set bedrock at y=0
//                        primer.setBlockState(l, k1, k, BEDROCK);
//                    }
//
//                    IBlockState byte3 = primer.getBlockState(l, k1, k);
//
//                    if (byte3 == AIR)
//                    {
//                        j1 = -1;
//                        continue;
//                    }
//
//                    if (byte3 != STONE)
//                    {
//                        continue;
//                    }
//
//                    if (j1 == -1)
//                    {
//                        if (i1 <= 0)
//                        {
//                            byte1 = AIR;
//                            byte2 = STONE;
//                        }
//                        else if (k1 >= byte0 - 4 && k1 <= byte0 + 1)
//                        {
//                            // improve this
//                            byte1 = biomegenbase.topBlock;
//                            byte2 = biomegenbase.fillerBlock;
//
//                            if (flag1)
//                            {
//                                byte1 = AIR;
//                            }
//
//                            if (flag1)
//                            {
//                                if (!forceSandBeach)
//                                {
//                                    int random = rand.nextInt(1500);
//                                    byte2 = GRAVEL;
//
//                                    if (random == 0)
//                                    {
//                                        byte2 = GOLD_ORE;
//                                    }
//                                }
//                                else
//                                {
//                                    byte2 = SAND;
//                                }
//                            }
//
//                            if (flag)
//                            {
//                                byte1 = SAND;
//                            }
//
//                            if (flag)
//                            {
//                                byte2 = SAND;
//                            }
//                        }
//
//                        if (k1 < byte0 && byte1 == AIR)
//                        {
//                            byte1 = WATER;
//                        }
//
//                        j1 = i1;
//
//                        if (k1 >= byte0 - 1)
//                        {
//                            primer.setBlockState(l, k1, k, byte1);
//                        }
//                        else
//                        {
//                            primer.setBlockState(l, k1, k, byte2);
//                        }
//
//                        continue;
//                    }
//
//                    if (j1 <= 0)
//                    {
//                        continue;
//                    }
//
//                    j1--;
//                    primer.setBlockState(l, k1, k, byte2);
//
//                    if (j1 == 0 && byte2 == SAND)
//                    {
//                        j1 = rand.nextInt(4);
//                        byte2 = SANDSTONE;
//                    }
//                }
//            }
//        }
//    }
    public void replaceBlocksForBiome(int x, int z, ChunkPrimer primer, BiomeGenBase[] biomeGens)
    {
        double d0 = 0.03125D;
        this.stoneNoise = this.field_147430_m.func_151599_a(this.stoneNoise, (double)(x * 16), (double)(z * 16), 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);

        for (int i = 0; i < 16; ++i)
        {
            for (int j = 0; j < 16; ++j)
            {
                BiomeGenBase biomegenbase = biomeGens[j + i * 16];
                biomegenbase.genTerrainBlocks(this.worldObj, this.rand, primer, x * 16 + i, z * 16 + j, this.stoneNoise[j + i * 16]);
            }
        }
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    public Chunk provideChunk(int x, int z)
    {
        this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.setBlocksInChunk(x, z, chunkprimer);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
        this.replaceBlocksForBiome(x, z, chunkprimer, this.biomesForGeneration);

        if (this.settings.useCaves)
        {
            this.caveGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useRavines)
        {
            this.ravineGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useMineShafts && this.mapFeaturesEnabled)
        {
            this.mineshaftGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useVillages && this.mapFeaturesEnabled)
        {
            this.villageGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useStrongholds && this.mapFeaturesEnabled)
        {
            this.strongholdGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useTemples && this.mapFeaturesEnabled)
        {
            this.scatteredFeatureGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }

        if (this.settings.useMonuments && this.mapFeaturesEnabled)
        {
            this.oceanMonumentGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        }

        Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
        byte[] abyte = chunk.getBiomeArray();

        for (int i = 0; i < abyte.length; ++i)
        {
            abyte[i] = (byte)this.biomesForGeneration[i].biomeID;
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    private void func_147423_a(int p_147423_1_, int p_147423_2_, int p_147423_3_)
    {
        this.field_147426_g = this.noiseGen6.generateNoiseOctaves(this.field_147426_g, p_147423_1_, p_147423_3_, 5, 5, (double)this.settings.depthNoiseScaleX, (double)this.settings.depthNoiseScaleZ, (double)this.settings.depthNoiseScaleExponent);
        float f = this.settings.coordinateScale;
        float f1 = this.settings.heightScale;
        this.field_147427_d = this.field_147429_l.generateNoiseOctaves(this.field_147427_d, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, (double)(f / this.settings.mainNoiseScaleX), (double)(f1 / this.settings.mainNoiseScaleY), (double)(f / this.settings.mainNoiseScaleZ));
        this.field_147428_e = this.field_147431_j.generateNoiseOctaves(this.field_147428_e, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, (double)f, (double)f1, (double)f);
        this.field_147425_f = this.field_147432_k.generateNoiseOctaves(this.field_147425_f, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, (double)f, (double)f1, (double)f);
        p_147423_3_ = 0;
        p_147423_1_ = 0;
        int i = 0;
        int j = 0;

        for (int k = 0; k < 5; ++k)
        {
            for (int l = 0; l < 5; ++l)
            {
                float f2 = 0.0F;
                float f3 = 0.0F;
                float f4 = 0.0F;
                int i1 = 2;
                BiomeGenBase biomegenbase = this.biomesForGeneration[k + 2 + (l + 2) * 10];

                for (int j1 = -i1; j1 <= i1; ++j1)
                {
                    for (int k1 = -i1; k1 <= i1; ++k1)
                    {
                        BiomeGenBase biomegenbase1 = this.biomesForGeneration[k + j1 + 2 + (l + k1 + 2) * 10];
                        float f5 = this.settings.biomeDepthOffSet + biomegenbase1.minHeight * this.settings.biomeDepthWeight;
                        float f6 = this.settings.biomeScaleOffset + biomegenbase1.maxHeight * this.settings.biomeScaleWeight;

                        if (this.field_177475_o == WorldType.AMPLIFIED && f5 > 0.0F)
                        {
                            f5 = 1.0F + f5 * 2.0F;
                            f6 = 1.0F + f6 * 4.0F;
                        }

                        float f7 = this.parabolicField[j1 + 2 + (k1 + 2) * 5] / (f5 + 2.0F);

                        if (biomegenbase1.minHeight > biomegenbase.minHeight)
                        {
                            f7 /= 2.0F;
                        }

                        f2 += f6 * f7;
                        f3 += f5 * f7;
                        f4 += f7;
                    }
                }

                f2 = f2 / f4;
                f3 = f3 / f4;
                f2 = f2 * 0.9F + 0.1F;
                f3 = (f3 * 4.0F - 1.0F) / 8.0F;
                double d7 = this.field_147426_g[j] / 8000.0D;

                if (d7 < 0.0D)
                {
                    d7 = -d7 * 0.3D;
                }

                d7 = d7 * 3.0D - 2.0D;

                if (d7 < 0.0D)
                {
                    d7 = d7 / 2.0D;

                    if (d7 < -1.0D)
                    {
                        d7 = -1.0D;
                    }

                    d7 = d7 / 1.4D;
                    d7 = d7 / 2.0D;
                }
                else
                {
                    if (d7 > 1.0D)
                    {
                        d7 = 1.0D;
                    }

                    d7 = d7 / 8.0D;
                }

                ++j;
                double d8 = (double)f3;
                double d9 = (double)f2;
                d8 = d8 + d7 * 0.2D;
                d8 = d8 * (double)this.settings.baseSize / 8.0D;
                double d0 = (double)this.settings.baseSize + d8 * 4.0D;

                for (int l1 = 0; l1 < 33; ++l1)
                {
                    double d1 = ((double)l1 - d0) * (double)this.settings.stretchY * 128.0D / 256.0D / d9;

                    if (d1 < 0.0D)
                    {
                        d1 *= 4.0D;
                    }

                    double d2 = this.field_147428_e[i] / (double)this.settings.lowerLimitScale;
                    double d3 = this.field_147425_f[i] / (double)this.settings.upperLimitScale;
                    double d4 = (this.field_147427_d[i] / 10.0D + 1.0D) / 2.0D;
                    double d5 = MathHelper.denormalizeClamp(d2, d3, d4) - d1;

                    if (l1 > 29)
                    {
                        double d6 = (double)((float)(l1 - 29) / 3.0F);
                        d5 = d5 * (1.0D - d6) + -10.0D * d6;
                    }

                    this.field_147434_q[i] = d5;
                    ++i;
                }
            }
        }
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
        int i = x * 16;
        int j = z * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos.add(16, 0, 16));
        this.rand.setSeed(this.worldObj.getSeed());
        long k = this.rand.nextLong() / 2L * 2L + 1L;
        long l = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)x * k + (long)z * l ^ this.worldObj.getSeed());
        boolean flag = false;
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(x, z);

        if (this.settings.useMineShafts && this.mapFeaturesEnabled)
        {
            this.mineshaftGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
        }

        if (this.settings.useVillages && this.mapFeaturesEnabled)
        {
            flag = this.villageGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
        }

        if (this.settings.useStrongholds && this.mapFeaturesEnabled)
        {
            this.strongholdGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
        }

        if (this.settings.useTemples && this.mapFeaturesEnabled)
        {
            this.scatteredFeatureGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
        }

        if (this.settings.useMonuments && this.mapFeaturesEnabled)
        {
            this.oceanMonumentGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
        }

        if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills && this.settings.useWaterLakes && !flag && this.rand.nextInt(this.settings.waterLakeChance) == 0)
        {
            int i1 = this.rand.nextInt(16) + 8;
            int j1 = this.rand.nextInt(256);
            int k1 = this.rand.nextInt(16) + 8;
            (new WorldGenLakes(Blocks.water)).generate(this.worldObj, this.rand, blockpos.add(i1, j1, k1));
        }

        if (!flag && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes)
        {
            int i2 = this.rand.nextInt(16) + 8;
            int l2 = this.rand.nextInt(this.rand.nextInt(248) + 8);
            int k3 = this.rand.nextInt(16) + 8;

            if (l2 < this.worldObj.getSeaLevel() || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0)
            {
                (new WorldGenLakes(Blocks.lava)).generate(this.worldObj, this.rand, blockpos.add(i2, l2, k3));
            }
        }

        if (this.settings.useDungeons)
        {
            for (int j2 = 0; j2 < this.settings.dungeonChance; ++j2)
            {
                int i3 = this.rand.nextInt(16) + 8;
                int l3 = this.rand.nextInt(256);
                int l1 = this.rand.nextInt(16) + 8;
                (new WorldGenDungeons()).generate(this.worldObj, this.rand, blockpos.add(i3, l3, l1));
            }
        }

        biomegenbase.decorate(this.worldObj, this.rand, new BlockPos(i, 0, j));
        SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, i + 8, j + 8, 16, 16, this.rand);
        blockpos = blockpos.add(8, 0, 8);

        for (int k2 = 0; k2 < 16; ++k2)
        {
            for (int j3 = 0; j3 < 16; ++j3)
            {
                BlockPos blockpos1 = this.worldObj.getPrecipitationHeight(blockpos.add(k2, 0, j3));
                BlockPos blockpos2 = blockpos1.down();

                if (this.worldObj.canBlockFreezeWater(blockpos2))
                {
                    this.worldObj.setBlockState(blockpos2, Blocks.ice.getDefaultState(), 2);
                    this.worldObj.forceBlockUpdateTick(Blocks.ice, blockpos2, new Random());
                }

                // Mountain snow gen included
                if (this.worldObj.canSnowAt(blockpos1, true))
                {
                    if(biomegenbase.equals(BiomeGenBase.stoneMountains)) {
                        this.worldObj.setBlockState(blockpos1.add(0,-1,0), Blocks.snow.getDefaultState(), 2);
                    } else {
                        this.worldObj.setBlockState(blockpos1, Blocks.snow_layer.getDefaultState(), 2);
                    }
                }

                // Mountain snow
                if(biomegenbase.equals(BiomeGenBase.stoneMountains) && blockpos1.getY()>92) {
                    IBlockState block;
                    if (this.worldObj.canSnowAt(blockpos1, false)) {
                        block = Blocks.snow.getDefaultState();
                        while ((worldObj.getBlockState(blockpos1).getBlock()==Blocks.log2||worldObj.getBlockState(blockpos1).getBlock()==Blocks.log) &&blockpos1.getY()>80) {
                            blockpos1=blockpos1.down();
                        }
                        if(blockpos1.getY()>89) {
                            blockpos2 = blockpos1.down();

                            if (worldObj.getBlockState(blockpos2).getBlock() != Blocks.air) {
                                this.worldObj.setBlockState(blockpos2, block);
                            }
                            BlockPos blockpos3 = blockpos2.down();
                            if (worldObj.getBlockState(blockpos3).getBlock() != Blocks.air) {
                                this.worldObj.setBlockState(blockpos3, block);
                            }
                            BlockPos blockpos4 = blockpos3.down();
                            if (worldObj.getBlockState(blockpos4).getBlock() != Blocks.air) {
                                this.worldObj.setBlockState(blockpos4, block);
                            }
                        }
                    }

                }

            }
        }

        BlockFalling.fallInstantly = false;
    }

    public boolean populateChunk(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_)
    {
        boolean flag = false;

        if (this.settings.useMonuments && this.mapFeaturesEnabled && p_177460_2_.getInhabitedTime() < 3600L)
        {
            flag |= this.oceanMonumentGenerator.generateStructure(this.worldObj, this.rand, new ChunkCoordIntPair(p_177460_3_, p_177460_4_));
        }

        return flag;
    }

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate progressCallback)
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
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(pos);

        if (this.mapFeaturesEnabled)
        {
            if (creatureType == EnumCreatureType.MONSTER && this.scatteredFeatureGenerator.func_175798_a(pos))
            {
                return this.scatteredFeatureGenerator.getScatteredFeatureSpawnList();
            }

            if (creatureType == EnumCreatureType.MONSTER && this.settings.useMonuments && this.oceanMonumentGenerator.func_175796_a(this.worldObj, pos))
            {
                return this.oceanMonumentGenerator.func_175799_b();
            }
        }

        return biomegenbase.getSpawnableList(creatureType);
    }

    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position)
    {
        return "Stronghold".equals(structureName) && this.strongholdGenerator != null ? this.strongholdGenerator.getClosestStrongholdPos(worldIn, position) : null;
    }

    public int getLoadedChunkCount()
    {
        return 0;
    }

    public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_)
    {
        if (this.settings.useMineShafts && this.mapFeaturesEnabled)
        {
            this.mineshaftGenerator.generate(this, this.worldObj, p_180514_2_, p_180514_3_, (ChunkPrimer)null);
        }

        if (this.settings.useVillages && this.mapFeaturesEnabled)
        {
            this.villageGenerator.generate(this, this.worldObj, p_180514_2_, p_180514_3_, (ChunkPrimer)null);
        }

        if (this.settings.useStrongholds && this.mapFeaturesEnabled)
        {
            this.strongholdGenerator.generate(this, this.worldObj, p_180514_2_, p_180514_3_, (ChunkPrimer)null);
        }

        if (this.settings.useTemples && this.mapFeaturesEnabled)
        {
            this.scatteredFeatureGenerator.generate(this, this.worldObj, p_180514_2_, p_180514_3_, (ChunkPrimer)null);
        }

        if (this.settings.useMonuments && this.mapFeaturesEnabled)
        {
            this.oceanMonumentGenerator.generate(this, this.worldObj, p_180514_2_, p_180514_3_, (ChunkPrimer)null);
        }
    }

    public Chunk provideChunk(BlockPos blockPosIn)
    {
        return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
    }
}
