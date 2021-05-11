package makamys.treenoise;

import net.minecraft.world.gen.structure.MapGenVillage;

public class MapGenNoVillage extends MapGenVillage {
    
    protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
        return false;
    }
    
}
