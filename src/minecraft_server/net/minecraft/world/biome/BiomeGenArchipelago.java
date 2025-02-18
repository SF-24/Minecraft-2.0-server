package net.minecraft.world.biome;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

import java.util.Random;

public class BiomeGenArchipelago extends BiomeGenBase
{
    private boolean field_150614_aC;
    private static final IBlockState OAK_LOG = Blocks.log.getDefaultState();
    private static final IBlockState OAK_LEAVES = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

    public BiomeGenArchipelago(int id)
    {
        super(id);
        this.theBiomeDecorator.treesPerChunk = 1;
        this.theBiomeDecorator.grassPerChunk = 8;
        this.theBiomeDecorator.flowersPerChunk = 3;

        this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10, 4, 4));
    }

    public WorldGenAbstractTree genBigTreeChance(Random rand)
    {
        return (WorldGenAbstractTree)(rand.nextInt(10) == 0 ? this.worldGeneratorBigTree :
                new WorldGenTrees(false, 4 + rand.nextInt(7), OAK_LOG, OAK_LEAVES, true));
    }

    /**
     * Gets a WorldGen appropriate for this biome.
     */
    public WorldGenerator getRandomWorldGenForGrass(Random rand)
    {
        return rand.nextInt(4) == 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }

    public void decorate(World worldIn, Random rand, BlockPos pos)
    {
        super.decorate(worldIn, rand, pos);
        int i = rand.nextInt(16) + 8;
        int j = rand.nextInt(16) + 8;
        int k = rand.nextInt(worldIn.getHeight(pos.add(i, 0, j)).getY() * 2);
        (new WorldGenPumpkin()).generate(worldIn, rand, pos.add(i, k, j));

    }
}
