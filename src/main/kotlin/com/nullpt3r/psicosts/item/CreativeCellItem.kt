package com.nullpt3r.psicosts.item

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class CreativeCellItem(properties: Properties) : Item(properties) {

    override fun isBarVisible(stack: ItemStack) = false

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        lines: MutableList<Component>,
        flag: TooltipFlag
    ) {
        lines.add(Component.translatable("item.psicosts.creative_cell.tooltip").withStyle(ChatFormatting.LIGHT_PURPLE))
    }
}
