package makamys.treenoise;

import net.minecraft.world.gen.structure.MapGenMineshaft;

public class MapGenCustomMineshaft extends MapGenMineshaft {
    double chance;
    
    public MapGenCustomMineshaft(double chance) {
        this.chance = chance;
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
        return this.rand.nextDouble() < chance && this.rand.nextInt(80) < Math.max(Math.abs(p_75047_1_), Math.abs(p_75047_2_));
    }
}
