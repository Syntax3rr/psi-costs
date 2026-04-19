package com.nullpt3r.psicosts.ingredient

import com.mojang.serialization.MapCodec
import com.nullpt3r.psicosts.item.PsiCellItem
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
import java.util.stream.Stream

/** Ingredient that only matches a PSI cell of the given item type when it is not fully charged. */
class UnchargedCellIngredient(val item: Item) : ICustomIngredient {

    override fun test(stack: ItemStack): Boolean {
        val cell = stack.item as? PsiCellItem ?: return false
        return cell == item && PsiCellItem.getCharge(stack) < cell.capacity
    }

    override fun getItems(): Stream<ItemStack> = Stream.of(item.defaultInstance)

    override fun isSimple() = false

    override fun getType(): IngredientType<UnchargedCellIngredient> = TYPE

    companion object {
        private val CODEC: MapCodec<UnchargedCellIngredient> =
            BuiltInRegistries.ITEM.byNameCodec()
                .fieldOf("item")
                .xmap(::UnchargedCellIngredient) { it.item }

        val TYPE = IngredientType(CODEC)
    }
}
