package net.mineshaft.cavegen.cave_types;

import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;
import net.mineshaft.cavegen.CaveDecorator;
import net.mineshaft.cavegen.CaveHeight;
import net.mineshaft.cavegen.CaveType;

import java.util.Random;

public class CaveTypeFrozen extends CaveType {
    public CaveTypeFrozen(CaveHeight height, String name) {
        super(height, name, false);
    }

    @Override
    public void decorateMapGenCaves(ChunkPrimer chunkPrimer, int localChunkX, int localChunkY, int localChunkZ, Random rand) {
        super.decorateMapGenCaves(chunkPrimer, localChunkX, localChunkY, localChunkZ, rand);
        CaveDecorator.placeBoulder(chunkPrimer, localChunkX, localChunkY, localChunkZ, Blocks.packed_ice.getDefaultState(), Blocks.ice.getDefaultState(), Blocks.air.getDefaultState(), rand);
    }
}
