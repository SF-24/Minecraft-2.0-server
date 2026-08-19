package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Random;

public class BlockMagma extends Block
{
    public BlockMagma()
    {
        super(Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setLightLevel(0.2F);
        this.setTickRandomly(true);
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    public MapColor getMapColor(IBlockState state, IBlockAccess p_180659_2_, BlockPos p_180659_3_)
    {
        return MapColor.netherrackColor;
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block)
     */
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn)
    {
        if (!entityIn.isImmuneToFire() && entityIn instanceof EntityLivingBase /*&& !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase)entityIn)*/)
        {
            entityIn.attackEntityFrom(DamageSource.hotFloor, 1.0F);
        }

        super.onEntityWalk(worldIn, pos, entityIn);
    }

    public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return 15728880;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        BlockPos blockpos = pos.up();
        IBlockState iblockstate = worldIn.getBlockState(blockpos);

        if (iblockstate.getBlock() == Blocks.water || iblockstate.getBlock() == Blocks.flowing_water)
        {
            worldIn.setBlockToAir(blockpos);
            worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), "fire.fire",0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F,false);

            if (worldIn instanceof WorldServer)
            {
                ((WorldServer)worldIn).spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.25D, (double)blockpos.getZ() + 0.5D, 8, 0.5D, 0.25D, 0.5D, 0.0D);
            }
        }
    }

    public boolean canEntitySpawn(IBlockState state, Entity entityIn)
    {
        return entityIn.isImmuneToFire();
    }
}