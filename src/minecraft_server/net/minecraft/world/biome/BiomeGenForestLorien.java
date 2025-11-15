package net.minecraft.world.biome;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

import java.util.Random;

public class BiomeGenForestLorien extends BiomeGenBase
{
    private int field_150632_aF;

    private static final IBlockState birchLog = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH);
    private static final IBlockState acaciaLeaves = Blocks.leaves2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
    private static final IBlockState darkOakLeaves = Blocks.leaves2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

    protected static final WorldGenCanopyTreeLorien lorienTree = new WorldGenCanopyTreeLorien(false);
    protected static final WorldGenForest smallTree2 = new WorldGenForest(false, false);

    public BiomeGenForestLorien(int id, int p_i45377_2_)
    {
        super(id);
        this.theBiomeDecorator.treesPerChunk = 4;
        this.theBiomeDecorator.grassPerChunk = 2;
        this.setFillerBlockMetadata(5159473);
        this.setTemperatureRainfall(0.7F, 0.8F);

        if (this.field_150632_aF == 2)
        {
            this.field_150609_ah = 353825;
            this.color = 3175492;
            this.setTemperatureRainfall(0.6F, 0.6F);
        }

        if (this.field_150632_aF == 0)
        {
            this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 5, 4, 4));
        }

        if (this.field_150632_aF == 3)
        {
            this.theBiomeDecorator.treesPerChunk = -999;
        }
    }

    protected BiomeGenBase func_150557_a(int colorIn, boolean p_150557_2_)
    {
        if (this.field_150632_aF == 2)
        {
            this.field_150609_ah = 353825;
            this.color = colorIn;

            if (p_150557_2_)
            {
                this.field_150609_ah = (this.field_150609_ah & 16711422) >> 1;
            }

            return this;
        }
        else
        {
            return super.func_150557_a(colorIn, p_150557_2_);
        }
    }

    public WorldGenAbstractTree genBigTreeChance(Random rand)
    {
        if (rand.nextInt(2) == 0)
        {
            return lorienTree;
        }
        else if (rand.nextInt(6) == 0)
        {
            return  new WorldGenTreesAnySurface(false, 8, birchLog, acaciaLeaves, false, false);
        }

        //else if(rand.nextInt(8) == 0) {return  new WorldGenTreesAnySurface(false, 8, birchLog, darkOakLeaves, false, false);}
        return smallTree2;
    }

    public BlockFlower.EnumFlowerType pickRandomFlower(Random rand, BlockPos pos)
    {
        if (this.field_150632_aF == 1)
        {
            double d0 = MathHelper.clamp_double((1.0D + GRASS_COLOR_NOISE.func_151601_a((double)pos.getX() / 48.0D, (double)pos.getZ() / 48.0D)) / 2.0D, 0.0D, 0.9999D);
            BlockFlower.EnumFlowerType blockflower$enumflowertype = BlockFlower.EnumFlowerType.values()[(int)(d0 * (double)BlockFlower.EnumFlowerType.values().length)];
            return blockflower$enumflowertype == BlockFlower.EnumFlowerType.BLUE_ORCHID ? BlockFlower.EnumFlowerType.POPPY : blockflower$enumflowertype;
        }
        else
        {
            return super.pickRandomFlower(rand, pos);
        }
    }

    public void decorate(World worldIn, Random rand, BlockPos pos)
    {
        for (int i = 0; i < 4; ++i)
        {
            for (int j = 0; j < 4; ++j)
            {
                int k = i * 4 + 1 + 8 + rand.nextInt(3);
                int l = j * 4 + 1 + 8 + rand.nextInt(3);
                BlockPos blockpos = worldIn.getHeight(pos.add(k, 0, l));

                if (rand.nextInt(5) == 0)
                {
                    WorldGenAbstractTree worldgenabstracttree = this.genBigTreeChance(rand);
                    worldgenabstracttree.func_175904_e();

                    if (worldgenabstracttree.generate(worldIn, rand, blockpos))
                    {
                        worldgenabstracttree.func_180711_a(worldIn, rand, blockpos);
                    }
                }
            }
        }

        int j1 = rand.nextInt(5) - 3;

        if (this.field_150632_aF == 1)
        {
            j1 += 2;
        }

        for (int k1 = 0; k1 < j1; ++k1)
        {
            int l1 = rand.nextInt(3);

            if (l1 == 0)
            {
                DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.SYRINGA);
            }
            else if (l1 == 1)
            {
                DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.ROSE);
            }
            else if (l1 == 2)
            {
                DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.PAEONIA);
            }

            for (int i2 = 0; i2 < 5; ++i2)
            {
                int j2 = rand.nextInt(16) + 8;
                int k2 = rand.nextInt(16) + 8;
                int i1 = rand.nextInt(worldIn.getHeight(pos.add(j2, 0, k2)).getY() + 32);

                if (DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, new BlockPos(pos.getX() + j2, i1, pos.getZ() + k2)))
                {
                    break;
                }
            }
        }

        super.decorate(worldIn, rand, pos);
    }
}
