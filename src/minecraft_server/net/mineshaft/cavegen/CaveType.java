package net.mineshaft.cavegen;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.Random;

public class CaveType {

    String name;
    CaveHeight height;

    public CaveType(CaveHeight height, String name)
    {
        this.name = name;
        this.height = height;
    }

    public void decorate(World worldIn, Random rand, BiomeGenBase biomeGenBase, BlockPos pos) {

    }

}
