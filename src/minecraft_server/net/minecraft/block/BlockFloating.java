package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockFloating extends Block
{
    public BlockFloating(Material blockMaterialIn, MapColor blockMapColorIn)
    {
        super(blockMaterialIn, blockMapColorIn);
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        updateTick(worldIn, pos, new Random());
    }

    private void updateTick(World worldIn, BlockPos pos, Random random)
    {
        if ((pos.getY() <= 256) && worldIn.getBlockState(pos.up()).getBlock().equals(Blocks.air))
        {
            //worldIn.setBlockState(pos, Blocks.air.getDefaultState());
//            EntityFallingBlock floatingBlockEntity = new EntityFallingBlock(worldIn, (double) pos.getX() + 0.5, (double) pos.getY() + 2, (double) pos.getZ() + 0.5, Blocks.gravitite_ore.getDefaultState());
//            floatingBlockEntity.motionY += 0.1F;
        }
        else
        {
            //       worldIn.forceBlockUpdateTick(Blocks.gravitite_ore, pos, new Random());
        }
    }
}
