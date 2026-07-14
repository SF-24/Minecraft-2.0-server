package net.mineshaft.cavegen.cave_types;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;
import net.mineshaft.cavegen.CaveDecorator;
import net.mineshaft.cavegen.CaveHeight;
import net.mineshaft.cavegen.CaveType;

import java.util.Random;

public class CaveTypeJungle extends CaveType {

    boolean overgrown;

    private static final IBlockState JUNGLE_LOG = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
    //    private static final IBlockState JUNGLE_LEAVES = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
    private static final IBlockState OAK_LEAVES = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

    public CaveTypeJungle(CaveHeight height, String name, boolean overgrown) {
        super(height, name, overgrown);
        this.overgrown=overgrown;
    }

    @Override
    public void decorateMapGenCaves(ChunkPrimer chunkPrimer, int localChunkX, int localChunkY, int localChunkZ, Random rand) {
        super.decorateMapGenCaves(chunkPrimer, localChunkX, localChunkY, localChunkZ, rand);
        if(localChunkY<40) return;
        if (rand.nextInt(2) == 0) {
            CaveDecorator.placeCaveFoliage(chunkPrimer, localChunkX, localChunkY, localChunkZ, JUNGLE_LOG, OAK_LEAVES, rand);
        }
    }

    @Override
    public void surfaceRules(ChunkPrimer chunkPrimer, int localChunkX, int localChunkY, int localChunkZ, Random random) {
        super.surfaceRules(chunkPrimer, localChunkX, localChunkY, localChunkZ, random);
        if (overgrown && random.nextInt(3) == 0) {  // 33% chance to place grass as ground if overgrown
            chunkPrimer.setBlockFromId(localChunkX, localChunkY, localChunkZ, (short) 32);
            if(random.nextInt(5)==0) {
                chunkPrimer.setBlockFromId(localChunkX,localChunkY+1,localChunkZ, (short) 498); // ferns
            }
        }
    }

    //    @Override
//    public void decorate(World worldIn, Random rand, BiomeGenBase biomeGenBase, BlockPos chunkPos) {
//        super.decorate(worldIn, rand, biomeGenBase, chunkPos);
//
//        forEachCavePosition(worldIn, rand, chunkPos, 6, (worldX, y, worldZ, r) -> {
//            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(worldX, y, worldZ);
//
//            if (!isValidCaveFloor(worldIn, pos)) return;
//
//            if (r.nextInt(18) == 0) {
//                new WorldGenCaveShrub(JUNGLE_LOG, OAK_LEAVES).generate(worldIn, r, pos);
//            }
//        });
//    }

}
