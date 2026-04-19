package com.nullpt3r.psicosts.registry

import com.nullpt3r.psicosts.Config
import com.nullpt3r.psicosts.PsiCosts
import com.nullpt3r.psicosts.item.CreativeCellItem
import com.nullpt3r.psicosts.item.PsiCellItem
import com.nullpt3r.psicosts.item.UnstableCellItem
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModItems {
    private val ITEMS = DeferredRegister.createItems(PsiCosts.MODID)
    private val TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PsiCosts.MODID)

    val PSI_CELL_1: DeferredItem<PsiCellItem> = ITEMS.register("psi_cell_1", Supplier {
        PsiCellItem(Config.CELL_TIER_1_CAPACITY, Item.Properties().stacksTo(1))
    })
    val PSI_CELL_2: DeferredItem<PsiCellItem> = ITEMS.register("psi_cell_2", Supplier {
        PsiCellItem(Config.CELL_TIER_2_CAPACITY, Item.Properties().stacksTo(1))
    })
    val PSI_CELL_3: DeferredItem<PsiCellItem> = ITEMS.register("psi_cell_3", Supplier {
        PsiCellItem(Config.CELL_TIER_3_CAPACITY, Item.Properties().stacksTo(1))
    })
    val PSI_CELL_4: DeferredItem<PsiCellItem> = ITEMS.register("psi_cell_4", Supplier {
        PsiCellItem(Config.CELL_TIER_4_CAPACITY, Item.Properties().stacksTo(1))
    })

    val UNSTABLE_CELL: DeferredItem<UnstableCellItem> = ITEMS.register("unstable_cell", Supplier {
        UnstableCellItem(Config.UNSTABLE_CELL_CAPACITY, Item.Properties().stacksTo(1))
    })

    val CREATIVE_CELL: DeferredItem<CreativeCellItem> = ITEMS.register("creative_cell", Supplier {
        CreativeCellItem(Item.Properties().stacksTo(1))
    })

    private val CREATIVE_TAB = TABS.register("psi_costs", Supplier {
        CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.psicosts"))
            .icon { PSI_CELL_1.get().fullyCharged() }
            .displayItems { _, output ->
                listOf(PSI_CELL_1, PSI_CELL_2, PSI_CELL_3, PSI_CELL_4)
                    .map { it.get() }
                    .forEach { cell ->
                        output.accept(cell.defaultInstance)
                        output.accept(cell.fullyCharged())
                    }
                output.accept(UNSTABLE_CELL.get().fullyCharged())
                output.accept(CREATIVE_CELL.get())
            }
            .build()
    })

    fun register(bus: IEventBus) {
        ITEMS.register(bus)
        TABS.register(bus)
    }
}
