package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAetherPillar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BiomeGenAether extends BiomeGenBase
{
    public BiomeGenAether(int id)
    {
        super(id);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntitySheep.class, 10, 1, 4));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySlime.class, 10, 4, 4));
        this.topBlock = Blocks.grass.getDefaultState();
        this.fillerBlock = Blocks.dirt.getDefaultState();
        this.theBiomeDecorator = new BiomeAetherDecorator();
    }

    public void decorate(World worldIn, Random rand, BlockPos pos)
    {
        super.decorate(worldIn, rand, pos);

        for (int k2 = 0; k2 < 16; ++k2)
        {
            for (int j3 = 0; j3 < 16; ++j3)
            {
                BlockPos blockpos1 = worldIn.getPrecipitationHeight(pos.add(k2, 0, j3)); //ERROR
                ArrayList<BlockPos> blockPos = new ArrayList<>();
                blockPos.add(blockpos1.down());
                blockPos.add(blockpos1.down(2));
                Block top = worldIn.getBiomeGenForCoords(blockpos1).topBlock.getBlock();

                if (!top.equals(Blocks.stone_slab) && !top.equals(Blocks.chest) && !top.equals(Blocks.air) && !top.equals(Blocks.water) && !top.equals(Blocks.cobblestone) && !top.equals(Blocks.stonebrick) && !top.equals(Blocks.mossy_cobblestone))
                {
                    BlockPos pos1 = blockpos1;
                    int m = 0;

                    while (pos1.equals(Blocks.leaves) || pos.equals(Blocks.leaves2))
                    {
                        pos1 = pos1.down();
                        m++;

                        if (m >= 12)
                        {
                            pos1 = blockpos1;
                            break;
                        }
                    }

                    Block top1 = worldIn.getBiomeGenForCoords(pos1).topBlock.getBlock();

                    if (!(top1 instanceof BlockLeaves && !top1.equals(Blocks.leaves) && !top1.equals(Blocks.leaves2)))
                    {
                        worldIn.setBlockState(pos1, Blocks.grass.getDefaultState());

                        for (BlockPos blockPo : blockPos)
                        {
                            if (!worldIn.getBlockState(blockPo).getBlock().equals(Blocks.air))
                            {
                                worldIn.setBlockState(blockPo, Blocks.dirt.getDefaultState());
                            }
                        }
                    }
                }
            }
        }
    }
}
