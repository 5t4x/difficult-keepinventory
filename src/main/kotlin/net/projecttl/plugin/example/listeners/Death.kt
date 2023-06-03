package net.projecttl.plugin.example.listeners

import com.github.jikoo.planarwrappers.util.Experience
import net.projecttl.plugin.example.INGREDIENTS
import org.bukkit.entity.EntityType
import org.bukkit.entity.ExperienceOrb
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import kotlin.math.floor
import kotlin.math.round

class Death : Listener {
    /**
     * Remove 1/5 of the item's durability
     * @return The new ItemStack
     */
    private fun damageMain(item: ItemStack, livingEntity: LivingEntity): ItemStack? {
        if (item.itemMeta == null) return null

        val damageable = item.itemMeta as Damageable
        val remainingDurability = item.type.maxDurability - damageable.damage
        // always floor it so damage can be 0
        val takeDamage = floor((remainingDurability / 5).toDouble()).toInt()

        return item.damage(takeDamage, livingEntity)
    }

    /**
     * Remove 1/5 of the player's EXP
     */
    private fun dropEXP(player: Player) {
        val exp = Experience.getExp(player)
        val dropEXP = floor((exp / 5).toDouble()).toInt()
        Experience.changeExp(player, -dropEXP)
        val orb = player.world.spawnEntity(player.location, EntityType.EXPERIENCE_ORB) as ExperienceOrb;
        orb.experience = dropEXP
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        // only execute logic if they're keeping their inventory
        if (event.keepInventory) {
            event.player.inventory.setItemInMainHand(damageMain(event.player.inventory.itemInMainHand, event.player))
            event.player.inventory.setItemInOffHand(damageMain(event.player.inventory.itemInOffHand, event.player))

            dropEXP(event.player)

            for (item in event.player.inventory) {
                if (item == null) continue

                // if there's only one, we shouldn't have to drop it
                if (item.amount == 1) continue

                // only drop ingredients
                if (INGREDIENTS.contains(item.type.name)) {
                    // drop half the stack
                    val newAmount = round((item.amount / 2).toDouble()).toInt()
                    val dropAmount = item.amount - newAmount

                    // drop what belongs to the player
                    val dropStack = item.clone()
                    dropStack.amount = dropAmount

                    event.drops += dropStack

                    // set the amount in the player's inventory
                    item.amount = newAmount
                }
            }
        }
    }
}