package com.nullpt3r.psicosts.item

import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.CustomData
import net.neoforged.neoforge.common.ModConfigSpec

class UnstableCellItem(private val capacityConfig: ModConfigSpec.IntValue, properties: Properties) :
    Item(properties), EnvPsiStorage {

    val capacity: Int get() = capacityConfig.asInt

    override fun isBarVisible(stack: ItemStack) = getCharge(stack) < capacity

    override fun getBarWidth(stack: ItemStack): Int =
        (13.0 * getCharge(stack) / capacity).toInt().coerceIn(0, 13)

    override fun getBarColor(stack: ItemStack) = 0xff6600 // amber - distinct from regular cells

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        lines: MutableList<Component>,
        flag: TooltipFlag
    ) {
        lines.add(
            Component.translatable(
                "item.psicosts.psi_cell.stored",
                "%,d".format(getCharge(stack)),
                "%,d".format(capacity)
            ).withStyle(ChatFormatting.GOLD)
        )
        lines.add(Component.translatable("item.psicosts.unstable_cell.tooltip").withStyle(ChatFormatting.GRAY))
    }

    /** Removes up to [requested] PSI from this cell. Returns the amount actually extracted. */
    fun extractCharge(stack: ItemStack, requested: Int): Int {
        val stored = getCharge(stack)
        val extracted = minOf(requested, stored)
        if (extracted > 0) setCharge(stack, stored - extracted)
        return extracted
    }

    /** Adds up to [amount] PSI into this cell. Returns the amount actually stored. */
    fun injectCharge(stack: ItemStack, amount: Int): Int {
        val stored = getCharge(stack)
        val injected = minOf(amount, capacity - stored)
        if (injected > 0) setCharge(stack, stored + injected)
        return injected
    }

    override fun getStoredCharge(stack: ItemStack) = getCharge(stack)
    override fun drainCharge(stack: ItemStack, requested: Int) = extractCharge(stack, requested)

    private fun setCharge(stack: ItemStack, amount: Int) {
        val tag = stack.get(DataComponents.CUSTOM_DATA)?.copyTag() ?: CompoundTag()
        tag.putInt(TAG_CHARGE, amount.coerceIn(0, capacity))
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag))
    }

    fun fullyCharged(): ItemStack {
        val stack = defaultInstance
        val tag = CompoundTag()
        tag.putInt(TAG_CHARGE, capacity)
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag))
        return stack
    }

    companion object {
        const val TAG_CHARGE = "PsioCharge"

        fun getCharge(stack: ItemStack): Int =
            stack.get(DataComponents.CUSTOM_DATA)?.copyTag()?.getInt(TAG_CHARGE) ?: 0
    }
}
