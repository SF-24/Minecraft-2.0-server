package net.minecraft.world.gen.feature;

import com.google.common.base.Predicates;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

public class WorldGenMeteor extends WorldGenerator {
    private final IBlockState cobblestone  = Blocks.cobblestone.getDefaultState();
    private final IBlockState coal = Blocks.coal_ore.getDefaultState();
    private final IBlockState stone = Blocks.stone.getDefaultState();
    private final IBlockState gold = Blocks.gold_ore.getDefaultState();
    private final IBlockState iron = Blocks.iron_ore.getDefaultState();
    private final IBlockState obsidian = Blocks.obsidian.getDefaultState();
    private static final BlockStateHelper stateHelper = BlockStateHelper.forBlock(Blocks.gravel);
    private static final BlockStateHelper stateHelper1 = BlockStateHelper.forBlock(Blocks.grass);
    private static final BlockStateHelper stateHelper2 = BlockStateHelper.forBlock(Blocks.stone);



    public boolean generate(World worldIn, Random rand, BlockPos position) {


        while (worldIn.isAirBlock(position) && position.getY() > 2)
        {
            position = position.down();
        }

        final int type = rand.nextInt(5);
        // TYPES:
        // 0: coal
        // 1: gold
        // 2,3: iron
        // 4: obsidian

        if (!stateHelper.apply(worldIn.getBlockState(position)) && !stateHelper1.apply(worldIn.getBlockState(position)) && !stateHelper2.apply(worldIn.getBlockState(position)))
        {
            return false;
        }
        else {

            for (int x = -2; x <= 2; x++) {
                for (int y = -2; y <= 2; y++) {
                    for (int z = -2; z <= 2; z++) {
                        if (!(Math.abs(z) == 2 && Math.abs(x) == 2) && !(Math.abs(z) == 2 && Math.abs(y) == 2) && !(Math.abs(x) == 2 && Math.abs(y) == 2)) {
                            if (rand.nextInt(7) == 0) {
                                switch (type) {
                                    case 0:
                                        worldIn.setBlockState(position.add(x, y, z), coal);
                                        break;
                                    case 1:
                                        worldIn.setBlockState(position.add(x, y, z), gold);
                                        break;
                                    case 2:
                                        worldIn.setBlockState(position.add(x, y, z), iron);
                                        break;
                                    case 3:
                                        worldIn.setBlockState(position.add(x, y, z), iron);
                                        break;
                                    case 4:
                                        worldIn.setBlockState(position.add(x, y, z), obsidian);
                                        break;
                                    default:
                                        break;
                                }
                            } else if (rand.nextInt(2) == 0) {
                                worldIn.setBlockState(position.add(x, y, z), cobblestone);
                            } else {
                                worldIn.setBlockState(position.add(x, y, z), stone);
                            }
                        }
                    }
                }
            }
            return true;
        }

    }

}
