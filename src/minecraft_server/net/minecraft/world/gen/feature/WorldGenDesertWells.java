package net.minecraft.world.gen.feature;

import com.google.common.base.Predicates;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

public class WorldGenDesertWells extends WorldGenerator
{
    private static final BlockStateHelper stateHelper = BlockStateHelper.forBlock(Blocks.sand).where(BlockSand.VARIANT, Predicates.equalTo(BlockSand.EnumType.SAND));
    private final IBlockState sandstoneSlab = Blocks.stone_slab.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
    private final IBlockState sandstone = Blocks.sandstone.getDefaultState();
    private final IBlockState flowingWater = Blocks.flowing_water.getDefaultState();
    private final IBlockState gold = Blocks.gold_block.getDefaultState();
    private final IBlockState oreGold = Blocks.gold_ore.getDefaultState();
    private final IBlockState oreDiamond = Blocks.diamond_ore.getDefaultState();

    private static final List<WeightedRandomChestContent> itemsToGenerate = Lists.newArrayList(
            new WeightedRandomChestContent(Items.gold_nugget, 0, 8, 25, 3),
            new WeightedRandomChestContent(Items.glass_bottle, 0, 1, 3, 2),
            new WeightedRandomChestContent(Items.arrow, 0, 1, 8, 2),
            new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 1),
            new WeightedRandomChestContent(Items.bone, 0, 1, 12, 3),
            new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.sand), 0, 3, 24, 4));


    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        while (worldIn.isAirBlock(position) && position.getY() > 2)
        {
            position = position.down();
        }

        if (!stateHelper.apply(worldIn.getBlockState(position)))
        {
            return false;
        }
        else
        {
            for (int i = -2; i <= 2; ++i)
            {
                for (int j = -2; j <= 2; ++j)
                {
                    if (worldIn.isAirBlock(position.add(i, -1, j)) && worldIn.isAirBlock(position.add(i, -2, j)))
                    {
                        return false;
                    }
                }
            }

            for (int l = -1; l <= 0; ++l)
            {
                for (int l1 = -2; l1 <= 2; ++l1)
                {
                    for (int k = -2; k <= 2; ++k)
                    {
                        //? bottom
                        int probability = rand.nextInt(10000);
                        if(probability>=9900) {
                            worldIn.setBlockState(position.add(l1, l, k), this.gold, 2);
                        } else if(probability<5) {
                            worldIn.setBlockState(position.add(l1, l, k), this.oreDiamond, 2);
                        } else {
                            worldIn.setBlockState(position.add(l1, l, k), this.sandstone, 2);
                        }
                    }
                }
            }

            worldIn.setBlockState(position, this.flowingWater, 2);

            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                worldIn.setBlockState(position.offset(enumfacing), this.flowingWater, 2);
            }

            for (int i1 = -2; i1 <= 2; ++i1)
            {
                for (int i2 = -2; i2 <= 2; ++i2)
                {
                    if (i1 == -2 || i1 == 2 || i2 == -2 || i2 == 2)
                    {
                        // sides of well
                        worldIn.setBlockState(position.add(i1, 1, i2), this.sandstone, 2);
                    }
                }
            }

            worldIn.setBlockState(position.add(2, 1, 0), this.sandstoneSlab, 2);
            worldIn.setBlockState(position.add(-2, 1, 0), this.sandstoneSlab, 2);
            worldIn.setBlockState(position.add(0, 1, 2), this.sandstoneSlab, 2);
            worldIn.setBlockState(position.add(0, 1, -2), this.sandstoneSlab, 2);

            for (int j1 = -1; j1 <= 1; ++j1)
            {
                for (int j2 = -1; j2 <= 1; ++j2)
                {
                    if (j1 == 0 && j2 == 0)
                    {
                        // tip
                        if(rand.nextInt(1)==0) {
                            // spawn chest
                            IBlockState chest = Blocks.chest.getDefaultState();
                            int y = -1;
                            worldIn.setBlockState(position.add(j1,y,j2), chest, 2);
                            TileEntity tileentity = worldIn.getTileEntity(position.add(j1,y,j2));
                            WeightedRandomChestContent.generateChestContents(rand,itemsToGenerate,(TileEntityChest) tileentity, 5);

                        }
                        worldIn.setBlockState(position.add(j1, 4, j2), this.sandstone, 2);
                    }
                    else
                    {
                        worldIn.setBlockState(position.add(j1, 4, j2), this.sandstoneSlab, 2);
                    }
                }
            }

            for (int k1 = 1; k1 <= 3; ++k1)
            {
                // pillars of well
                worldIn.setBlockState(position.add(-1, k1, -1), this.sandstone, 2);
                worldIn.setBlockState(position.add(-1, k1, 1), this.sandstone, 2);
                worldIn.setBlockState(position.add(1, k1, -1), this.sandstone, 2);
                worldIn.setBlockState(position.add(1, k1, 1), this.sandstone, 2);
            }

            return true;
        }
    }
}
