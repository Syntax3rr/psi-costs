package com.nullpt3r.psicosts.mixin;

import com.nullpt3r.psicosts.item.EnvPsiStorage;
import com.nullpt3r.psicosts.item.UnstableCellItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.ref.WeakReference;

@Mixin(targets = "vazkii.psi.common.core.handler.PlayerDataHandler$PlayerData", remap = false)
public abstract class MixinPlayerData {

    @Shadow private WeakReference<Player> playerWR;
    @Shadow public int availablePsi;
    @Shadow public abstract int getTotalPsi();

    @Inject(method = "getAvailablePsi", at = @At("RETURN"), cancellable = true)
    private void addEnvironmentalToAvailable(CallbackInfoReturnable<Integer> cir) {
        Player p = playerWR != null ? playerWR.get() : null;
        if (p == null) return;
        int extra = EnvPsiStorage.Companion.totalInventoryCharge(p);
        if (extra <= 0) return;
        cir.setReturnValue(Math.min(cir.getReturnValue() + extra, getTotalPsi()));
    }

    @Inject(method = "deductPsi", at = @At("HEAD"), cancellable = true)
    private void drainEnvironmentalFirst(int cost, int sideCost, boolean showy, CallbackInfo ci) {
        if (cost <= 0) return;
        Player p = playerWR != null ? playerWR.get() : null;
        if (p == null) return;

        int totalEnv = EnvPsiStorage.Companion.totalInventoryCharge(p);
        if (totalEnv <= 0) return;
        int fromEnv = Math.min(cost, totalEnv);
        drainEnvInventory(p, fromEnv);

        if (fromEnv >= cost) {
            ci.cancel();
        } else {
            availablePsi += fromEnv;
        }
    }

    private static void drainEnvInventory(Player player, int amount) {
        int remaining = amount;
        var inv = player.getInventory();
        for (int slot = 0; slot < inv.getContainerSize(); slot++) {
            if (remaining <= 0) break;
            ItemStack stack = inv.getItem(slot);
            if (!(stack.getItem() instanceof EnvPsiStorage storage)) continue;
            remaining -= storage.drainCharge(stack, remaining);
            if (storage.getStoredCharge(stack) == 0) {
                if (stack.getItem() instanceof UnstableCellItem) {
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 0.4f, 1.4f);
                }
                inv.setItem(slot, ItemStack.EMPTY);
            }
        }
    }
}
