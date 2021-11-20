package makamys.worldtweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import makamys.worldtweaks.Config;
import net.minecraft.world.gen.structure.StructureMineshaftPieces;

@Mixin(StructureMineshaftPieces.Corridor.class)
public abstract class MixinStructureMineshaftPieces_Corridor {
    
    @Shadow
    private boolean hasSpiders;
    
    @Inject(method = "<init>*", at = @At("RETURN"))
    private void postConstructed(CallbackInfo ci) {
        if(Config.disableMineshaftSpiders) {
            hasSpiders = false;
        }
    }

}
