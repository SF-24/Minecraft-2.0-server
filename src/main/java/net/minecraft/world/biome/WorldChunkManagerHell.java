package net.minecraft.world.biome;

import net.minecraft.util.BlockPos;
import net.minecraft.world.gen.NoiseGeneratorSimplex;
import net.mineshaft.NetherConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorldChunkManagerHell extends WorldChunkManager
{
    /** The biome generator object. */
    private BiomeGenBase biomeGenerator;

    /** The rainfall in the world */
    private float rainfall;

    private long chunkSeed;
    private long worldGenSeed;
    NoiseGeneratorSimplex simplexNoise;
    NoiseGeneratorSimplex simplexNoise_second;
    NoiseGeneratorSimplex decoratorNoise;
    private boolean useBiomes = false;

    public WorldChunkManagerHell(BiomeGenBase biome, float p_i45374_2_)
    {
        this.biomeGenerator = biome;
        decoratorNoise = new NoiseGeneratorSimplex(new Random(109437328979L));
        this.rainfall = p_i45374_2_;
    }

    public WorldChunkManagerHell(BiomeGenBase biome, long seed, float p_i45374_2_, boolean useNetherBiomes)
    {
        this.biomeGenerator = biome;
        this.worldGenSeed=seed;
        this.rainfall = p_i45374_2_;
        simplexNoise = new NoiseGeneratorSimplex(new Random(seed));
        simplexNoise_second = new NoiseGeneratorSimplex(new Random(seed+1236767));
        decoratorNoise = new NoiseGeneratorSimplex(new Random(109437328979L));
        useBiomes = useNetherBiomes;
    }

    /**
     * Returns the biome generator
     */
    public BiomeGenBase getBiomeGenerator(BlockPos pos)
    {
        return this.getBiomeGenerator(pos.getX(),pos.getZ());
    }

    // Made the decorator noise wider.
    public double getDecoratorNoise(int x, int z) {
        double noise = decoratorNoise.getValue(x / 36.0, z / 36.0); // was 24 then 48
        double t = (noise + 1.0) * 0.5;
        t = t * t * (3 - 2 * t); // smoothstep
        return t;
    }

    public BiomeGenBase getBiomeGenerator(int x, int z)
    {
        if(useBiomes) {
//            this.initChunkSeed(biomeX,biomeZ); // Div by 256 pr 512
//            double value = simplexNoise.getValue(((double) x) /128, ((double) z) /128);
//            // Smoothing
//            double t = (value + 1.0) * 0.5;
//            t = t * t * (3 - 2 * t); // Polynomial curve
//
//            return (t > 0.5)
//                    ? BiomeGenBase.hell
//                    : BiomeGenBase.soulSandValley;
////            return biomeList[nextInt(biomeList.length)];

            double warpedX = x + 20*simplexNoise.getValue(x / 128.0, z / 128.0);
            double warpedZ = z + 20*simplexNoise.getValue((x + 1000) / 256.0, (z + 1000) / 256.0);

            double soulSandNoise = simplexNoise_second.getValue(
                    (warpedX) / NetherConfig.netherBiomeScale / 4,
                    (warpedZ) / NetherConfig.netherBiomeScale / 4
            );
            double cragNoise = simplexNoise_second.getValue(
                    (warpedX) / NetherConfig.netherBiomeScale,
                    (warpedZ) / NetherConfig.netherBiomeScale
            );
            double value = simplexNoise.getValue(
                    (warpedX) / NetherConfig.netherBiomeScale,
                    (warpedZ) / NetherConfig.netherBiomeScale
            );

            // Small gravel crags
            if ((value*0.25 + 0.4*cragNoise)<(-0.5)) {
                return BiomeGenBase.gravelCrags;
            }

            // Hell override
            if (soulSandNoise*soulSandNoise<0.1) {
                return BiomeGenBase.hell;
            }

            // Gravel crags
            if((value*0.3 + 0.9*soulSandNoise)<(-0.45)) {
                return BiomeGenBase.gravelCrags;
            }

            // New nether biomes
            if(value*0.2 + 0.8*soulSandNoise > 0.3 || (value*0.8 + 0.2*soulSandNoise)>0.4) { // new generator. Was 0.15
                return BiomeGenBase.soulSandValley;
            }
            return BiomeGenBase.hell;
        }
        return this.biomeGenerator;
    }

    /**
     * Returns an array of biomes for the location input.
     */
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomes, int x, int z, int width, int height)
    {
        if (biomes == null || biomes.length < width * height)
        {
            biomes = new BiomeGenBase[width * height];
        }

        Arrays.fill(biomes, 0, width * height, getBiomeGenerator(x,z));
        return biomes;
    }

    /**
     * Returns a list of rainfall values for the specified blocks. Args: listToReuse, x, z, width, length.
     */
    public float[] getRainfall(float[] listToReuse, int x, int z, int width, int length)
    {
        if (listToReuse == null || listToReuse.length < width * length)
        {
            listToReuse = new float[width * length];
        }

        Arrays.fill(listToReuse, 0, width * length, this.rainfall);
        return listToReuse;
    }

    /**
     * Returns biomes to use for the blocks and loads the other data like temperature and humidity onto the
     * WorldChunkManager Args: oldBiomeList, x, z, width, depth
     */
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] oldBiomeList, int x, int z, int width, int depth)
    {
        if (oldBiomeList == null || oldBiomeList.length < width * depth)
        {
            oldBiomeList = new BiomeGenBase[width * depth];
        }

        Arrays.fill(oldBiomeList, 0, width * depth, getBiomeGenerator(x,z));
        return oldBiomeList;
    }

    /**
     * Return a list of biomes for the specified blocks. Args: listToReuse, x, y, width, length, cacheFlag (if false,
     * don't check biomeCache to avoid infinite loop in BiomeCacheBlock)
     */
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
    {
        return this.loadBlockGeneratorData(listToReuse, x, z, width, length);
    }

    public BlockPos findBiomePosition(int x, int z, int range, List<BiomeGenBase> biomes, Random random)
    {
        return biomes.contains(this.biomeGenerator) ? new BlockPos(x - range + random.nextInt(range * 2 + 1), 0, z - range + random.nextInt(range * 2 + 1)) : null;
    }

    /**
     * checks given Chunk's Biomes against List of allowed ones
     */
    public boolean areBiomesViable(int p_76940_1_, int p_76940_2_, int p_76940_3_, List<BiomeGenBase> p_76940_4_)
    {
        return p_76940_4_.contains(this.biomeGenerator);
    }

    /**
     * returns a LCG pseudo random number from [0, x). Args: int x
     */
    protected int nextInt(int p_75902_1_)
    {
        int i = (int)((this.chunkSeed >> 24) % (long)p_75902_1_);

        if (i < 0)
        {
            i += p_75902_1_;
        }

        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += this.worldGenSeed;
        return i;
    }


    /**
     * Initialize layer's current chunkSeed based on the local worldGenSeed and the (x,z) chunk coordinates.
     */
    public void initChunkSeed(long p_75903_1_, long p_75903_3_)
    {
        this.chunkSeed = this.worldGenSeed;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += p_75903_1_;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += p_75903_3_;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += p_75903_1_;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += p_75903_3_;
    }
}
