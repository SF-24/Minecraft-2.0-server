package net.mineshaft.cavegen;

import net.minecraft.world.biome.BiomeGenBase;
import net.mineshaft.cavegen.cave_types.CaveTypeFrozen;
import net.mineshaft.cavegen.cave_types.CaveTypeJungle;

public class CaveRegistry {

    public static CaveType jungle = new CaveTypeJungle(CaveHeight.SHALLOW, "Lush Caves");
    public static CaveType ice = new CaveTypeFrozen(CaveHeight.SHALLOW, "Frozen Caves");

    public static CaveType getCaveType(int biome, int height){
        if(height>23) {
            if(biome>=128) biome-=128;

            switch (biome) {
                case 10: // Frozen ocean
                case 11: // Frozen river
                case 12: // Ice plains
                case 13: // Ice mountains
                case 30: // Cold taiga
                case 31: // Cold taiga hills
                case 43: // Stone mountains
                    return ice;
                case 6: // Swamp
                case 21: // Jungle
                case 22: // Jungle hills
                case 23: // Jungle edge
                case 41: // Tropical swampland
                case 44: // Rainforest
                case 46: // Roofed swamp
                case 48: // Archipelago
                case 49: // Archipelago hills
                    return jungle;
                default:
                    return null;
            }
        } else {
            // Separate placement. To be done.
            return null;
        }
    }

}
