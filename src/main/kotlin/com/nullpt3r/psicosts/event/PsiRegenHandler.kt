package com.nullpt3r.psicosts.event

import com.nullpt3r.psicosts.Config
import com.nullpt3r.psicosts.PsiCosts
import com.nullpt3r.psicosts.item.CreativeCellItem
import com.nullpt3r.psicosts.item.EnvPsiStorage
import com.nullpt3r.psicosts.item.PsiCellItem
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent
import vazkii.psi.api.cad.RegenPsiEvent
import vazkii.psi.common.core.handler.PlayerDataHandler

object PsiRegenHandler {

    /**
     * Redirects PSI regeneration through regular PSI cells only.
     * Unstable cells are environmental PSI - they are drained directly when spells are
     * cast (via MixinPlayerData) and never participate in regen.
     */
    @SubscribeEvent
    fun onPsiRegen(event: RegenPsiEvent) {
        val player = event.player
        if (event.regenCooldown > 0) return

        val toRegen = event.playerRegen.takeIf { it > 0 } ?: return

        val inv = player.inventory
        for (slot in 0 until inv.containerSize) {
            if (inv.getItem(slot).item is CreativeCellItem) {
                event.maxPlayerRegen = toRegen
                return
            }
        }

        var remaining = toRegen
        for (slot in 0 until inv.containerSize) {
            if (remaining <= 0) break
            val stack = inv.getItem(slot)
            val cell = stack.item as? PsiCellItem ?: continue
            remaining -= cell.extractCharge(stack, remaining)
        }

        val provided = toRegen - remaining
        if (provided > 0) {
            event.maxPlayerRegen = provided
        } else {
            event.maxPlayerRegen = 0
            if (event.cadRegen <= 0) event.regenCooldown = 20
        }
    }

    /**
     * Converts a portion of XP orb pickups into PSI when the player's pool is not full.
     *
     * Uses deductPsi with a negative cost as the only available path to add PSI while
     * also triggering Psi's client sync packet. The drainEnvironmentalFirst mixin guards
     * against this call with a `cost <= 0` early-return so unstable cells are unaffected.
     */
    @SubscribeEvent
    fun onXpPickup(event: PlayerXpEvent.PickupXp) {
        if (!Config.XP_CONVERSION_ENABLED.asBoolean) return
        val player = event.entity
        if (player.level().isClientSide) return

        val playerData = PlayerDataHandler.get(player)

        val inv = player.inventory
        for (slot in 0 until inv.containerSize) {
            if (inv.getItem(slot).item is CreativeCellItem) return
        }

        val effectivePsi = (playerData.availablePsi + EnvPsiStorage.totalInventoryCharge(player))
            .coerceAtMost(playerData.totalPsi)
        val psiNeeded = playerData.totalPsi - effectivePsi
        if (psiNeeded <= 0) return

        val xpValue = event.orb.value.takeIf { it > 0 } ?: return

        val ratio = Config.XP_CONVERSION_RATIO.asDouble
        val multiplier = Config.XP_CONVERSION_MULTIPLIER.asDouble

        val psiGained = (xpValue * ratio * multiplier).coerceAtMost(psiNeeded.toDouble()).toInt()
        if (psiGained <= 0) return

        val xpConsumed = (psiGained / multiplier).toInt().coerceAtMost(xpValue)

        event.orb.value = (xpValue - xpConsumed).coerceAtLeast(0)
        playerData.deductPsi(-psiGained, 0, true, false)

        PsiCosts.LOGGER.debug("Converted {} XP → {} PSI for {}", xpConsumed, psiGained, player.name.string)
    }
}
