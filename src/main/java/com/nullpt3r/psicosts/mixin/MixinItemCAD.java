package com.nullpt3r.psicosts.mixin;

import com.nullpt3r.psicosts.Config;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.psi.common.item.ItemCAD;

@Mixin(value = ItemCAD.class, remap = false)
public class MixinItemCAD {

    @Inject(method = "getRealCost", at = @At("RETURN"), cancellable = true)
    private static void onGetRealCost(ItemStack cad, ItemStack bullet, int cost, CallbackInfoReturnable<Integer> cir) {
        int realCost = cir.getReturnValue();
        if (realCost <= 0) return;
        double multiplier = Config.INSTANCE.getCOST_MULTIPLIER().get();
        if (multiplier == 1.0) return;
        cir.setReturnValue((int) Math.round(realCost * multiplier));
    }
}
