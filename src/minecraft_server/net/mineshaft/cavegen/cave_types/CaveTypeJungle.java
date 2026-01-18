package net.mineshaft.cavegen.cave_types;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.mineshaft.cavegen.CaveDecorator;
import net.mineshaft.cavegen.CaveHeight;
import net.mineshaft.cavegen.CaveType;

import java.util.Random;

public class CaveTypeJungle extends CaveType {

    private static final IBlockState JUNGLE_LOG = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
    //    private static final IBlockState JUNGLE_LEAVES = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
    private static final IBlockState OAK_LEAVES = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

    public CaveTypeJungle(CaveHeight height, String name) {
        super(height, name);
    }

    @Override
    public void decorateMapGenCaves(ChunkPrimer chunkPrimer, int localChunkX, int localChunkY, int localChunkZ, Random rand) {
        super.decorateMapGenCaves(chunkPrimer, localChunkX, localChunkY, localChunkZ, rand);
        if(localChunkY<40) return;
        if (rand.nextInt(2) == 0) {
            CaveDecorator.placeCaveFoliage(chunkPrimer, localChunkX, localChunkY, localChunkZ, JUNGLE_LOG, OAK_LEAVES, rand);
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
