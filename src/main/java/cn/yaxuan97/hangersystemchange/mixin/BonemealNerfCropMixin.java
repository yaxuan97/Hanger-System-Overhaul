package cn.yaxuan97.hangersystemchange.mixin;

import cn.yaxuan97.hangersystemchange.Config;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public class BonemealNerfCropMixin {
    @Inject(method = "getBonemealAgeIncrease(Lnet/minecraft/world/level/Level;)I", at = @At("HEAD"), cancellable = true)
    protected void getBonemealAgeIncrease(Level p_52262_, CallbackInfoReturnable<Integer> cir){
        if (Config.nerfBonemeal){
            cir.setReturnValue(1);
            cir.cancel();
        }
    }
}
