package net.projecttl.plugin.example

import net.projecttl.plugin.example.listeners.Death
import net.projecttl.plugin.example.listeners.JoinQuit
import org.bukkit.plugin.java.JavaPlugin

lateinit var instance: CorePlugin

class CorePlugin : JavaPlugin() {
    override fun onEnable() {
        instance = this
        server.pluginManager.apply {
            registerEvents(Death(), this@CorePlugin)
            registerEvents(JoinQuit(), this@CorePlugin)
        }
    }

    override fun onDisable() {}
}