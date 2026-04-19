package com.nullpt3r.psicosts.item

import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

/** Implemented by items that contribute environmental PSI - drawn before the main bar and consumed on use. */
interface EnvPsiStorage {
    /** PSI currently stored in this stack. Creative implementations always return [Int.MAX_VALUE]. */
    fun getStoredCharge(stack: ItemStack): Int

    /** Drain up to [requested] PSI from this stack. Returns the amount actually drained. */
    fun drainCharge(stack: ItemStack, requested: Int): Int

    companion object {
        /** Total environmental PSI across all slots, saturating at [Int.MAX_VALUE] to avoid overflow. */
        fun totalInventoryCharge(player: Player): Int {
            var total = 0L
            for (slot in 0 until player.inventory.containerSize) {
                val stack = player.inventory.getItem(slot)
                total += (stack.item as? EnvPsiStorage)?.getStoredCharge(stack)?.toLong() ?: continue
                if (total >= Int.MAX_VALUE) return Int.MAX_VALUE
            }
            return total.toInt()
        }
    }
}
