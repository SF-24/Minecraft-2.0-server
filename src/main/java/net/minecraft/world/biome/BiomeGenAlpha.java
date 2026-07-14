package net.minecraft.world.biome;

import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;

import java.util.Random;

public class BiomeGenAlpha extends BiomeGenBase
{
    protected static final WorldGenForest field_150629_aC = new WorldGenForest(false, true);
    protected static final WorldGenForest field_150630_aD = new WorldGenForest(false, false);
    protected static final WorldGenCanopyTree field_150631_aE = new WorldGenCanopyTree(false);

    public BiomeGenAlpha(int id)
    {
        super(id);
        this.theBiomeDecorator.treesPerChunk = 1;
        this.theBiomeDecorator.grassPerChunk = 2;
        this.setFillerBlockMetadata(5159473);
        this.setTemperatureRainfall(0.7F, 0.8F);
        this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 5, 4, 4));
    }

    protected BiomeGenBase func_150557_a(int colorIn, boolean p_150557_2_)
    {
        return super.func_150557_a(colorIn, p_150557_2_);
    }

    public WorldGenAbstractTree genBigTreeChance(Random rand)
    {
        return (WorldGenAbstractTree)(rand.nextInt(3) != 0 ? this.worldGeneratorTrees : field_150630_aD);
    }

    public BlockFlower.EnumFlowerType pickRandomFlower(Random rand, BlockPos pos)
    {
        return rand.nextInt(2) == 0 ? BlockFlower.EnumFlowerType.POPPY : BlockFlower.EnumFlowerType.DANDELION;
    }

    public void decorate(World worldIn, Random rand, BlockPos pos)
    {
//        for (int i = 0; i < 4; ++i)
//        {
//            for (int j = 0; j < 4; ++j)
//            {
//                int k = i * 4 + 1 + 8 + rand.nextInt(3);
//                int l = j * 4 + 1 + 8 + rand.nextInt(3);
//                BlockPos blockpos = worldIn.getHeight(pos.add(k, 0, l));
//
//                {
//                    WorldGenAbstractTree worldgenabstracttree = this.genBigTreeChance(rand);
//                    worldgenabstracttree.func_175904_e();
//
//                    if (worldgenabstracttree.generate(worldIn, rand, blockpos))
//                    {
//                        worldgenabstracttree.func_180711_a(worldIn, rand, blockpos);
//                    }
//                }
//            }
//        }
        super.decorate(worldIn, rand, pos);
    }
}
