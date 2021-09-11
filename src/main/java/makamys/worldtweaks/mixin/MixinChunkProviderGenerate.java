package makamys.worldtweaks.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import makamys.worldtweaks.WorldTweaks;
import makamys.worldtweaks.lib.owg.noise.NoiseOctavesBeta;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderGenerate;


@Mixin(ChunkProviderGenerate.class)
abstract class MixinChunkProviderGenerate {
    
    @Shadow
    private Random rand;
    
    private NoiseOctavesBeta myNoise;
    
    @Inject(method = "<init>*", at = @At("RETURN"))
    private void onConstructed(World world, long p2, boolean p3, CallbackInfo ci) {
        myNoise = new NoiseOctavesBeta(rand, 8);
    }
    
    @Redirect(method = "populate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/BiomeGenBase;decorate(Lnet/minecraft/world/World;Ljava/util/Random;II)V"))
    private void redirectPopulate(BiomeGenBase bgb, World world, Random random, int a, int b) {
        WorldTweaks.redirectDecorate(bgb, world, random, a, b, ChunkProviderGenerate.class.cast(this), myNoise);
        
    }
    
    
}
