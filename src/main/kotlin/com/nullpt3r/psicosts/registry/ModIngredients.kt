package com.nullpt3r.psicosts.registry

import com.nullpt3r.psicosts.PsiCosts
import com.nullpt3r.psicosts.ingredient.UnchargedCellIngredient
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.crafting.IngredientType
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Supplier

object ModIngredients {
    private val INGREDIENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, PsiCosts.MODID)

    val UNCHARGED_CELL = INGREDIENT_TYPES.register(
        "uncharged_cell",
        Supplier<IngredientType<UnchargedCellIngredient>> { UnchargedCellIngredient.TYPE }
    )

    fun register(bus: IEventBus) = INGREDIENT_TYPES.register(bus)
}
