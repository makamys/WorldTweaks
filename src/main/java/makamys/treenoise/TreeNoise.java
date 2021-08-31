package makamys.treenoise;

import java.lang.reflect.Field;
import java.util.Random;

import biomesoplenty.api.biome.BOPBiome;
import biomesoplenty.api.biome.BOPInheritedBiome;
import biomesoplenty.common.biome.decoration.BOPOverworldBiomeDecorator;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import makamys.treenoise.lib.owg.noise.NoiseOctavesBeta;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.terraingen.InitMapGenEvent;

@Mod(modid = TreeNoise.MODID, version = TreeNoise.VERSION)
public class TreeNoise
{
    public static final String MODID = "treenoise";
    public static final String VERSION = "0.0";
    
    public static boolean doTreeDensityModification;
    public static double mineshaftChance;
    public static boolean blockVillages;
    public static double treeMultiplierBase;
    public static double treeMultiplierSpread;
    public static double treeCutoff;
    public static double treeDampener;
    
    public static boolean disableQuicksand;
    public static boolean disableMud;
    public static boolean disablePoisonIvy;
    public static double bopFoliageMultiplier;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        
        config.load();
        mineshaftChance = config.getFloat("mineshaftChance", "Structure options", 0.004f, -1f, 1f, "Vanilla value: 0.004. Set to -1 to disable modification");
        blockVillages = config.getBoolean("disableVillages", "Structure options", false, "");
        
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
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.TERRAIN_GEN_BUS.register(this);
        
        if(Loader.isModLoaded("BiomesOPlenty")) {
            tweakBOPBiomes();
        }
        
        if(blockVillages) {
            MapGenVillage.villageSpawnBiomes.clear();
        }
    }
    
    public void tweakBOPBiomes() {
        for(BiomeGenBase bgb : BiomeGenBase.getBiomeGenArray()) {
            if (bgb instanceof BOPBiome<?>) {
                BiomeDecorator bd = getBOPBiomeDecorator(bgb, false);
                if(bd instanceof BOPOverworldBiomeDecorator) {
                    BOPOverworldBiomeDecorator bopBd = (BOPOverworldBiomeDecorator)bd;
                    if(disableQuicksand) {
                        bopBd.bopFeatures.generateQuicksand = false;
                    }
                    if(disableMud) {
                        bopBd.bopFeatures.mudPerChunk = 0;
                    }
                    if(disablePoisonIvy) {
                        bopBd.bopFeatures.poisonIvyPerChunk = 0;
                    }
                    if(bopFoliageMultiplier != 1) {
                        bopBd.bopFeatures.bopGrassPerChunk =
                                (int)Math.ceil(bopBd.bopFeatures.bopGrassPerChunk * bopFoliageMultiplier);
                        bopBd.grassPerChunk =
                                (int)Math.ceil(bopBd.grassPerChunk * bopFoliageMultiplier);
                        bopBd.bopFeatures.bushesPerChunk =
                                (int)Math.ceil(bopBd.bopFeatures.bushesPerChunk * bopFoliageMultiplier);
                        bopBd.bopFeatures.tinyCactiPerChunk =
                                (int)Math.ceil(bopBd.bopFeatures.tinyCactiPerChunk * bopFoliageMultiplier);
                        bopBd.bopFeatures.desertGrassPerChunk =
                                (int)Math.ceil(bopBd.bopFeatures.desertGrassPerChunk * bopFoliageMultiplier);
                        bopBd.bopFeatures.deadLeafPilesPerChunk =
                                (int)Math.ceil(bopBd.bopFeatures.deadLeafPilesPerChunk * bopFoliageMultiplier);
                        bopBd.bopFeatures.leafPilesPerChunk =
                                (int)Math.ceil(bopBd.bopFeatures.leafPilesPerChunk * bopFoliageMultiplier);
                        bopBd.bopFeatures.cloverPatchesPerChunk =
                                (int)Math.ceil(bopBd.bopFeatures.cloverPatchesPerChunk * bopFoliageMultiplier);
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void meh(InitMapGenEvent e) {
        if(mineshaftChance != -1 && e.type == InitMapGenEvent.EventType.MINESHAFT) {
            e.newGen = new MapGenCustomMineshaft(mineshaftChance);
        }
    }
    
    public static Field getField(Class clazz, String deobfName, String obfName){
        try {
            return clazz.getField(deobfName);
        } catch(Exception e) {}
        try {
            return clazz.getField(obfName);
        } catch(Exception e) {}
        System.out.println("Couldn't get field " + deobfName + " / " + obfName + " in class " + clazz);
        return null;
    }
    
    public static BiomeDecorator getBOPBiomeDecorator(BiomeGenBase biomegenbase, boolean followInherited) {
        BiomeDecorator bd = null;
        try {
            if (followInherited && biomegenbase instanceof BOPInheritedBiome<?>) {
                Field inheritedBiomeField = BOPInheritedBiome.class.getDeclaredField("inheritedBiome");
                inheritedBiomeField.setAccessible(true);
                bd = ((BiomeGenBase) inheritedBiomeField.get(biomegenbase)).theBiomeDecorator;
            } else {
                bd = (BiomeDecorator) getField(biomegenbase.getClass(), "theBiomeDecorator", "field_76760_I").get(biomegenbase);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bd;
    }
    
    public static void redirectDecorate(BiomeGenBase biomegenbase, World world, Random rand, int k, int l, ChunkProviderGenerate cpg, NoiseOctavesBeta myNoise) {
        BiomeDecorator bd = null;
        int trees = 0;
        if(doTreeDensityModification) {
            bd = biomegenbase.theBiomeDecorator;
            if (biomegenbase instanceof BOPBiome<?>) {
                bd = getBOPBiomeDecorator(biomegenbase, true);
            }
            trees = bd.treesPerChunk;
            double d = 0.5D;
            int k4 = (int) ((myNoise.func_806_a((double) k * d, (double) l * d) / 8D + rand.nextDouble() * 4D + 4D) * ((treeMultiplierSpread * 10.0)/9.0));
            // -3 ~ +3
            int newTrees = (int) (trees * ((treeMultiplierBase * 10.0) + k4) / 10f);
            if (newTrees > treeCutoff) {
                newTrees = (int)(treeCutoff + (trees - treeCutoff) * treeDampener); // stop tree spam
            }
            
            bd.treesPerChunk = newTrees;
        }
        
        biomegenbase.decorate(world, rand, k, l);
        
        if(doTreeDensityModification) {
            bd.treesPerChunk = trees;
        }
    }
}
