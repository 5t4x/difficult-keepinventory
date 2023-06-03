package com.github.fivetfourx.difficultkeepinventory

import com.github.fivetfourx.difficultkeepinventory.listeners.Death
import org.bukkit.plugin.java.JavaPlugin

lateinit var instance: CorePlugin

class CorePlugin : JavaPlugin() {
    override fun onEnable() {
        instance = this
        server.pluginManager.apply {
            registerEvents(Death(), this@CorePlugin)
        }
    }

    override fun onDisable() {}
}