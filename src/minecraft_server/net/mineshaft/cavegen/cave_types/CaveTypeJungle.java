package net.mineshaft.cavegen.cave_types;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.mineshaft.cavegen.CaveHeight;
import net.mineshaft.cavegen.CaveType;

import java.util.Random;

public class CaveTypeJungle extends CaveType {
    public CaveTypeJungle(CaveHeight height, String name) {
        super(height,name);
    }

    @Override
    public void decorate(World world, Random random, BiomeGenBase biomeGenBase, BlockPos pos) {
        super.decorate(world, random, biomeGenBase, pos);
    }
}
