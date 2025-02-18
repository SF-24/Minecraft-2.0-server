package net.minecraft.world.biome;

import net.minecraft.block.state.IBlockState;

public class BiomeGenBeach extends BiomeGenBase
{
    public BiomeGenBeach(int id, IBlockState material)
    {
        super(id);
        this.spawnableCreatureList.clear();
        this.topBlock = material;
        this.fillerBlock = material;
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.deadBushPerChunk = 0;
        this.theBiomeDecorator.reedsPerChunk = 0;
        this.theBiomeDecorator.cactiPerChunk = 0;
    }
}
