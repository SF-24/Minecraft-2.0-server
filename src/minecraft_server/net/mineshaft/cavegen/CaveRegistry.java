package net.mineshaft.cavegen;

import net.minecraft.world.biome.BiomeGenBase;
import net.mineshaft.cavegen.cave_types.CaveTypeJungle;

public class CaveRegistry {

    public static CaveType jungle = new CaveTypeJungle(CaveHeight.SHALLOW, "Lush Caves");

    public static CaveType getCaveType(BiomeGenBase biome, int height){
        if(height>23) {
            // Biome dependant
            return jungle;
        } else {
            // Separate placement. To be done.
            return null;
        }
    }

}
