package com.nullpt3r.psicosts

import com.mojang.logging.LogUtils
import com.nullpt3r.psicosts.event.PsiRegenHandler
import com.nullpt3r.psicosts.registry.ModIngredients
import com.nullpt3r.psicosts.registry.ModItems
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.neoforge.common.NeoForge
import org.slf4j.Logger

@Mod(PsiCosts.MODID)
class PsiCosts(modEventBus: IEventBus, modContainer: ModContainer) {

    companion object {
        const val MODID = "psicosts"
        val LOGGER: Logger = LogUtils.getLogger()
    }

    init {
        ModItems.register(modEventBus)
        ModIngredients.register(modEventBus)
        NeoForge.EVENT_BUS.register(PsiRegenHandler)
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC)
    }
}
