package main.rottenbread.im.interactionManager

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.plugin.java.JavaPlugin

class InteractionManager : JavaPlugin(), Listener {

    private lateinit var nameTables: List<String>

    fun loadnameTables() {
        nameTables = config.getStringList("entities")
    }

    override fun onEnable() {
        saveDefaultConfig()
        loadnameTables()
        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {

        if (event.hand != EquipmentSlot.HAND) return

        val entity = event.rightClicked
        if (entity is LivingEntity) {
            val customName = entity.customName
            if (customName != null && customName in nameTables) {
                event.player.sendMessage("§a[IM] §7You interacted with entity: §e$customName")
            }
        }
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (command.name.equals("imreload", ignoreCase = true)) {
            if (sender.hasPermission("im.reload")) {
                reloadConfig()
                loadnameTables()
                sender.sendMessage("§a[IM] §7Config reloaded.")
            } else {
                sender.sendMessage("§cYou don’t have permission to use this command.")
            }
            return true
        }
        return false
    }
}
