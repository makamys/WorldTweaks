package makamys.treenoise;

import java.lang.reflect.Field;
import java.util.Random;

import biomesoplenty.api.biome.BOPBiome;
import biomesoplenty.api.biome.BOPInheritedBiome;
import cpw.mods.fml.common.FMLCommonHandler;
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

@Mod(modid = TreeNoise.MODID, version = TreeNoise.VERSION)
public class TreeNoise
{
    public static final String MODID = "treenoise";
    public static final String VERSION = "0.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        
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
    
    public static void redirectDecorate(BiomeGenBase biomegenbase, World world, Random rand, int k, int l, ChunkProviderGenerate cpg, NoiseOctavesBeta myNoise) {
        BiomeDecorator bd = biomegenbase.theBiomeDecorator;
        if (biomegenbase instanceof BOPBiome<?>) {
            try {
                if (biomegenbase instanceof BOPInheritedBiome<?>) {
                    Field inheritedBiomeField = BOPInheritedBiome.class.getDeclaredField("inheritedBiome");
                    inheritedBiomeField.setAccessible(true);
                    bd = ((BiomeGenBase) inheritedBiomeField.get(biomegenbase)).theBiomeDecorator;
                } else {
                    bd = (BiomeDecorator) getField(biomegenbase.getClass(), "theBiomeDecorator", "field_76760_I").get(biomegenbase);
                    // XXX get this properly
                                 }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int trees = bd.treesPerChunk;
        double d = 0.5D;
        int k4 = (int) ((myNoise.func_806_a((double) k * d, (double) l * d) / 8D + rand.nextDouble() * 4D + 4D) * (3.0/9.0));
        // -3 ~ +3
        int newTrees = (int) (trees * (5 + k4) / 10f);
        if (newTrees > 15) {
            newTrees = 15 + (trees - 15) / 2; // stop tree spam
        }
        
        bd.treesPerChunk = newTrees;
        
        biomegenbase.decorate(world, rand, k, l);
        
        bd.treesPerChunk = trees;
    }
}
