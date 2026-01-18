
package net.minecraft.world.gen;

import com.google.common.base.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.mineshaft.cavegen.CaveRegistry;
import net.mineshaft.cavegen.CaveType;
import net.mineshaft.cavegen.CaveUtil;

import java.util.Random;

public class MapGenCaves extends MapGenBase
{
    Random randDecorator = null;

    protected void addTunnel(long seed, int p_180703_3_, int p_180703_4_, ChunkPrimer p_180703_5_, double p_180703_6_, double p_180703_8_, double p_180703_10_)
    {
        this.addRoom(seed, p_180703_3_, p_180703_4_, p_180703_5_, p_180703_6_, p_180703_8_, p_180703_10_, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    protected void addRoom(long seed, int originX, int originZ, ChunkPrimer chunkPrimer, double tunnelCentreX, double tunnelCentreY, double tunnelCentreZ, float baseRadius, float tunnelYaw, float tunnelPitch, int setIncrement, int stepCount, double ellipsoidStretchFactor)
    {
        // Allocate random decorator if missing.
        // Warning: generation of cobwebs and cave decoration will vary and not be tied to the world seed!
        if(randDecorator==null) randDecorator=new Random(seed);

        CaveType caveType = CaveRegistry.getCaveType(worldObj.getBiomeGenForCoords((int) tunnelCentreX, (int) tunnelCentreZ).biomeID, (int) tunnelCentreX, (int) (tunnelCentreY + 0.5), (int) tunnelCentreZ);

        double d0 = (double)(originX * 16 + 8);
        double d1 = (double)(originZ * 16 + 8);
        float f = 0.0F;
        float f1 = 0.0F;
        Random random = new Random(seed);

        if (stepCount <= 0)
        {
            int i = this.range * 16 - 16;
            stepCount = i - random.nextInt(i / 4);
        }

        boolean flag2 = false;

        if (setIncrement == -1)
        {
            setIncrement = stepCount / 2;
            flag2 = true;
        }

        int j = random.nextInt(stepCount / 2) + stepCount / 4;

        for (boolean flag = random.nextInt(6) == 0; setIncrement < stepCount; ++setIncrement)
        {
            double d2 = 1.5D + (double)(MathHelper.sin((float)setIncrement * (float)Math.PI / (float)stepCount) * baseRadius * 1.0F);
            double d3 = d2 * ellipsoidStretchFactor;
            float f2 = MathHelper.cos(tunnelPitch);
            float f3 = MathHelper.sin(tunnelPitch);
            tunnelCentreX += (double)(MathHelper.cos(tunnelYaw) * f2);
            tunnelCentreY += (double)f3;
            tunnelCentreZ += (double)(MathHelper.sin(tunnelYaw) * f2);

            if (flag)
            {
                tunnelPitch = tunnelPitch * 0.92F;
            }
            else
            {
                tunnelPitch = tunnelPitch * 0.7F;
            }

            tunnelPitch = tunnelPitch + f1 * 0.1F;
            tunnelYaw += f * 0.1F;
            f1 = f1 * 0.9F;
            f = f * 0.75F;
            f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;

            if (!flag2 && setIncrement == j && baseRadius > 1.0F && stepCount > 0)
            {
                this.addRoom(random.nextLong(), originX, originZ, chunkPrimer, tunnelCentreX, tunnelCentreY, tunnelCentreZ, random.nextFloat() * 0.5F + 0.5F, tunnelYaw - ((float)Math.PI / 2F), tunnelPitch / 3.0F, setIncrement, stepCount, 1.0D);
                this.addRoom(random.nextLong(), originX, originZ, chunkPrimer, tunnelCentreX, tunnelCentreY, tunnelCentreZ, random.nextFloat() * 0.5F + 0.5F, tunnelYaw + ((float)Math.PI / 2F), tunnelPitch / 3.0F, setIncrement, stepCount, 1.0D);
                return;
            }

            if (flag2 || random.nextInt(4) != 0)
            {
                double d4 = tunnelCentreX - d0;
                double d5 = tunnelCentreZ - d1;
                double d6 = (double)(stepCount - setIncrement);
                double d7 = (double)(baseRadius + 2.0F + 16.0F);

                if (d4 * d4 + d5 * d5 - d6 * d6 > d7 * d7)
                {
                    return;
                }

                if (tunnelCentreX >= d0 - 16.0D - d2 * 2.0D && tunnelCentreZ >= d1 - 16.0D - d2 * 2.0D && tunnelCentreX <= d0 + 16.0D + d2 * 2.0D && tunnelCentreZ <= d1 + 16.0D + d2 * 2.0D)
                {
                    int k2 = MathHelper.floor_double(tunnelCentreX - d2) - originX * 16 - 1;
                    int k = MathHelper.floor_double(tunnelCentreX + d2) - originX * 16 + 1;
                    int l2 = MathHelper.floor_double(tunnelCentreY - d3) - 1;
                    int l = MathHelper.floor_double(tunnelCentreY + d3) + 1;
                    int i3 = MathHelper.floor_double(tunnelCentreZ - d2) - originZ * 16 - 1;
                    int i1 = MathHelper.floor_double(tunnelCentreZ + d2) - originZ * 16 + 1;

                    if (k2 < 0)
                    {
                        k2 = 0;
                    }

                    if (k > 16)
                    {
                        k = 16;
                    }

                    if (l2 < 1)
                    {
                        l2 = 1;
                    }

                    if (l > 248)
                    {
                        l = 248;
                    }

                    if (i3 < 0)
                    {
                        i3 = 0;
                    }

                    if (i1 > 16)
                    {
                        i1 = 16;
                    }

                    boolean flag3 = false;

                    for (int j1 = k2; !flag3 && j1 < k; ++j1)
                    {
                        for (int k1 = i3; !flag3 && k1 < i1; ++k1)
                        {
                            for (int l1 = l + 1; !flag3 && l1 >= l2 - 1; --l1)
                            {
                                if (l1 >= 0 && l1 < 256)
                                {
                                    IBlockState iblockstate = chunkPrimer.getBlockState(j1, l1, k1);

                                    if (iblockstate.getBlock() == Blocks.flowing_water || iblockstate.getBlock() == Blocks.water)
                                    {
                                        flag3 = true;
                                    }

                                    if (l1 != l2 - 1 && j1 != k2 && j1 != k - 1 && k1 != i3 && k1 != i1 - 1)
                                    {
                                        l1 = l2;
                                    }
                                }
                            }
                        }
                    }

                    if (!flag3)
                    {
                        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                        for (int j3 = k2; j3 < k; ++j3)
                        {
                            double dz = ((double)(j3 + originX * 16) + 0.5D - tunnelCentreX) / d2;

                            for (int i2 = i3; i2 < i1; ++i2)
                            {
                                double dx = ((double)(i2 + originZ * 16) + 0.5D - tunnelCentreZ) / d2;
                                boolean flag1 = false;

                                if (dz * dz + dx * dx < 1.0D)
                                {
                                    for (int j2 = l; j2 > l2; --j2)
                                    {
                                        double dy = ((double)(j2 - 1) + 0.5D - tunnelCentreY) / d3;

                                        if (dy > -0.7D && dz * dz + dy * dy + dx * dx < 1.0D)
                                        {
                                            IBlockState iblockstate1 = chunkPrimer.getBlockState(j3, j2, i2);
                                            IBlockState iblockstate2 = (IBlockState)Objects.firstNonNull(chunkPrimer.getBlockState(j3, j2 + 1, i2), Blocks.air.getDefaultState());

                                            if (iblockstate1.getBlock() == Blocks.grass || iblockstate1.getBlock() == Blocks.mycelium)
                                            {
                                                flag1 = true;
                                            }

                                            if (this.isCarvableBlock(iblockstate1, iblockstate2))
                                            {
                                                if (j2 - 1 < 4 /*was 10*/)
                                                {
                                                    chunkPrimer.setBlockState(j3, j2, i2, Blocks.lava.getDefaultState());
                                                }
                                                else
                                                {
                                                    // Set air
                                                    chunkPrimer.setBlockState(j3, j2, i2, Blocks.air.getDefaultState());
                                                    if(chunkPrimer.getBlock(j3,j2+1,i2)==498) {
                                                        chunkPrimer.setBlockFromId(j3,j2+1,i2, (short) 0);
                                                    }
                                                    // Cave decoration logic, optimised
                                                    // Only 1/12 blocks attempt to decorate
                                                    // And function will only be parsed if the block is near a wall
                                                    // 0.0-> centre, 0.3-> near wall, 1.0-> edge of tunnel
                                                    if(dz*dz+dx*dx < 0.4 && dy > 0.3) {  // Near ceiling
                                                        if (randDecorator.nextInt(120) < (caveType!=null?caveType.getDecorationCount():1)) {
                                                            CaveUtil.decorateCave(caveType, chunkPrimer, j3, j2, i2, randDecorator);
                                                        }
                                                    }

                                                    if (iblockstate2.getBlock() == Blocks.sand)
                                                    {
                                                        chunkPrimer.setBlockState(j3, j2 + 1, i2, iblockstate2.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? Blocks.red_sandstone.getDefaultState() : Blocks.sandstone.getDefaultState());
                                                    }

                                                    if (flag1 && chunkPrimer.getBlockState(j3, j2 - 1, i2).getBlock() == Blocks.dirt)
                                                    {
                                                        blockpos$mutableblockpos.set(j3 + originX * 16, 0, i2 + originZ * 16);
                                                        chunkPrimer.setBlockState(j3, j2 - 1, i2, this.worldObj.getBiomeGenForCoords(blockpos$mutableblockpos).topBlock.getBlock().getDefaultState());
                                                    }

                                                    // Change the surface:
                                                    CaveUtil.setCaveSurface(caveType,chunkPrimer,j3,j2,i2,randDecorator);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (flag2)
                        {
                            break;
                        }
                    }
                }
            }
        }
    }

    protected boolean isCarvableBlock(IBlockState block, IBlockState blockAbove)
    {
        return block.getBlock() == Blocks.stone ? true : (block.getBlock() == Blocks.dirt ? true : (block.getBlock() == Blocks.grass ? true : (block.getBlock() == Blocks.hardened_clay ? true : (block.getBlock() == Blocks.stained_hardened_clay ? true : (block.getBlock() == Blocks.sandstone ? true : (block.getBlock() == Blocks.red_sandstone ? true : (block.getBlock() == Blocks.mycelium ? true : (block.getBlock() == Blocks.snow_layer ? true : (block.getBlock() == Blocks.sand || block.getBlock() == Blocks.gravel) && blockAbove.getBlock().getMaterial() != Material.water))))))));
    }

    protected boolean isCarvableBlock(int idOfBlockToRemove, int idOfBlockAbove)
    {
        return (idOfBlockToRemove == 16 || (idOfBlockToRemove == 48 || (idOfBlockToRemove == 32 || (idOfBlockToRemove == Block.getMultipliedIdFromBlock(Blocks.hardened_clay) || (idOfBlockToRemove == Block.getMultipliedIdFromBlock(Blocks.stained_hardened_clay) || (idOfBlockToRemove == 384) || (idOfBlockToRemove == 2864) || (idOfBlockToRemove == 1760) || (idOfBlockToRemove == 1248) || (idOfBlockToRemove == 192) || idOfBlockToRemove == 208)) &&
                (idOfBlockAbove != Block.getMultipliedIdFromBlock(Blocks.water) && idOfBlockAbove != Block.getMultipliedIdFromBlock(Blocks.flowing_water)))));
    }


    /**
     * Recursively called by generate()
     */
    protected void recursiveGenerate(World worldIn, int chunkX, int chunkZ, int p_180701_4_, int p_180701_5_, ChunkPrimer chunkPrimerIn)
    {
        int i = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(40) + 1) + 1);

        if (this.rand.nextInt(15) != 0)
        {
            i = 0;
        }

        for (int j = 0; j < i; ++j)
        {
            double d0 = (double)(chunkX * 16 + this.rand.nextInt(16));
            double d1 = (double)this.rand.nextInt(this.rand.nextInt(120) + 8);
            double d2 = (double)(chunkZ * 16 + this.rand.nextInt(16));
            int k = 1;

            if (this.rand.nextInt(4) == 0)
            {
                this.addTunnel(this.rand.nextLong(), p_180701_4_, p_180701_5_, chunkPrimerIn, d0, d1, d2);
                k += this.rand.nextInt(4);
            }

            for (int l = 0; l < k; ++l)
            {
                float f = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float f2 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();

                if (this.rand.nextInt(10) == 0)
                {
                    f2 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F;
                }

                this.addRoom(this.rand.nextLong(), p_180701_4_, p_180701_5_, chunkPrimerIn, d0, d1, d2, f2, f, f1, 0, 0, 1.0D);
            }
        }
    }
}
