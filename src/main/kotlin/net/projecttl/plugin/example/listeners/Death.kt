package net.projecttl.plugin.example.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import kotlin.math.round

class Death : Listener {
    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        // only execute logic if they're keeping their inventory
        if (event.keepInventory) for (item in event.player.inventory) {
            if (item == null) continue

            // if there's only one, we shouldn't have to drop it
            if (item.amount === 1) continue

            // TODO: only drop ingredients, nothing else

            // drop half the stack
            val newAmount = round((item.amount / 2).toDouble()).toInt()
            val dropAmount = item.amount - newAmount

            // drop what belongs to the player
            val dropStack = item.clone()
            dropStack.amount = dropAmount
            /*event.player.world.dropItem(
                event.player.location,
                dropStack
            )*/

            event.drops += dropStack

            // set the amount in the player's inventory
            item.amount = newAmount
        }
    }
}