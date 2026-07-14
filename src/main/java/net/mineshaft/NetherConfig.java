package net.mineshaft;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import static net.minecraft.block.BlockStone.VARIANT;

public class NetherConfig {

    public static int netherBiomeScale = 256; // Was 128

    public static Block magmaBlock = Blocks.obsidian;
    public static Block basaltBlock = Blocks.obsidian;
    public static IBlockState blackstoneBlockState = Blocks.blackstone.getDefaultState();
    public static Block smoothBasaltBlock = Blocks.stone;
}
