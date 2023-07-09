package cn.yaxuan97.hangersystemchange.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StemBlock.class)
public class BonemealNerfStemMixin {
    @Shadow @Final public static IntegerProperty AGE;

    @Inject(method = "performBonemeal", at = @At("HEAD"), cancellable = true)
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState blockState, CallbackInfo ci){
        int i = Math.min(StemBlock.MAX_AGE, blockState.getValue(AGE) + 1);
        level.setBlock(pos, blockState.setValue(AGE, i), 2);
        ci.cancel();
    }
}
