package makamys.worldtweaks;

import java.io.File;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

public class Config {

    public static boolean doTreeDensityModification;
    public static float mineshaftChance;
    public static boolean blockVillages;
    public static boolean disableDungeons;
    public static boolean disableMineshaftSpiders;
    public static float treeMultiplierBase;
    public static float treeMultiplierSpread;
    public static float treeCutoff;
    public static float treeDampener;
    
    public static boolean disableQuicksand;
    public static boolean disableMud;
    public static boolean disablePoisonIvy;
    public static float bopFoliageMultiplier;
    
    public static void reload() {
        Configuration config = new Configuration(new File(Launch.minecraftHome, "config/" + Constants.MODID + ".cfg"));
        
        config.load();
        mineshaftChance = config.getFloat("mineshaftChance", "Structure options", -1f, -1f, 1f, "Vanilla value: 0.004. Set to -1 to disable modification");
        blockVillages = config.getBoolean("disableVillages", "Structure options", false, "");
        disableDungeons = config.getBoolean("disableDungeons", "Structure options", false, "");
        
        doTreeDensityModification = config.getBoolean("doTreeDensityModification", "Tree density options", false, "The number of trees per chunk will be modified by a noise-based value. The other settings in this category only apply if this is set to true");
        treeMultiplierBase = config.getFloat("treeMultiplierBase", "Tree density options", 0.5f, 0.0f, Float.MAX_VALUE, "");
        treeMultiplierSpread = config.getFloat("treeMultiplierSpread", "Tree density options", 0.3f, 0.0f, Float.MAX_VALUE, "");
        treeCutoff = config.getFloat("treeCutoff", "Tree density options", 15, 0, Integer.MAX_VALUE, "");
        treeDampener = config.getFloat("treeDampenerAboveCutoff", "Tree density options", 0.5f, 0f, Float.MAX_VALUE, "");
        
        disableQuicksand = config.getBoolean("disableQuicksand", "BOP options", false, "");
        disableMud = config.getBoolean("disableMud", "BOP options", false, "");
        disablePoisonIvy = config.getBoolean("disablePoisonIvy", "BOP options", false, "");
        
        bopFoliageMultiplier = config.getFloat("bopFoliageMultiplier", "BOP options", 1f, 0f, Float.MAX_VALUE, "The number of BoP foliage per chunk will be multiplied by this.");
        if(config.hasChanged()) {
            config.save();
        }
    }

}
