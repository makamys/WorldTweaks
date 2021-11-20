package makamys.worldtweaks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class MixinConfigPlugin implements IMixinConfigPlugin {
    
    @Override
    public void onLoad(String mixinPackage) {
        Config.reload();
    }

    @Override
    public String getRefMapperConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<String> getMixins() {
        List<String> mixins = new ArrayList<>();
        
        if(Config.doTreeDensityModification) {
            mixins.add("MixinChunkProviderGenerate");
        }
        
        return mixins;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // TODO Auto-generated method stub
        
    }

}
