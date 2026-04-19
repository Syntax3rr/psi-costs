package com.nullpt3r.psicosts

import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory

@Mod(value = PsiCosts.MODID, dist = [Dist.CLIENT])
class PsiCostsClient(modEventBus: IEventBus, container: ModContainer) {
    init {
        container.registerExtensionPoint(IConfigScreenFactory::class.java, IConfigScreenFactory { mod, screen -> ConfigurationScreen(mod, screen) })
    }
}
