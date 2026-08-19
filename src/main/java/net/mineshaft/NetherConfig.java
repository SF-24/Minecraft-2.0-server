package net.mineshaft;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class NetherConfig {

    public static int fortressWellRoomMaxSize = 18;

    public static int hellBlendRadius = 800; // was 224, then 512 then 1024

    public static int hellBlendRadiusSquared = (hellBlendRadius*hellBlendRadius);
    public static double hellBlendRadiusSquaredReciprocal = 1.0/hellBlendRadiusSquared;

    public static double netherBiomeScaleReciprocal = (float) 1 /256; // Was 128
    public static double netherBiomeScaleReciprocalQuarter = (0.25 * netherBiomeScaleReciprocal); // Was 128

    public static Block magmaBlock = Blocks.obsidian;
    public static Block basaltBlock = Blocks.obsidian;
    public static IBlockState blackstoneBlockState = Blocks.blackstone.getDefaultState();
    public static Block smoothBasaltBlock = Blocks.stone;

}
