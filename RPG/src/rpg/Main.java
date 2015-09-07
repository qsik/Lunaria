package rpg;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import rpg.heroes.Hero;
import rpg.heroes.HeroType;

public class Main extends JavaPlugin implements Listener {
	public static JavaPlugin plugin;
	private static final String GAMERULE = "rpg";

	@EventHandler
	public void login(PlayerJoinEvent event) {
	}

	@EventHandler
	public void logoff(PlayerQuitEvent event) {
		Hero.LIST.get(event.getPlayer().getUniqueId()).cleanup();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("hero")) {
			if (args.length == 1 && sender instanceof Player) {
				Player player = (Player) sender;
				UUID id = player.getUniqueId();
				if (Hero.LIST.containsKey(id)) {
					Hero.LIST.get(id).cleanup();
					Hero.LIST.remove(id);
				}
				if (rpgWorld(player.getWorld())) {
					Hero.LIST.put(player.getUniqueId(), HeroType.getHeroType(args[0]).getHero(player));
				}
			}
		}
		if (cmd.getName().equalsIgnoreCase("test")) {
			Player player = (Player) sender;
			for (java.util.Map.Entry<String, Boolean> perm : Hero.PERMS.get(player.getUniqueId()).getPermissions().entrySet()) {
				player.sendMessage(perm.getKey() + " : " + perm.getValue());
			}
		}
		return false;
	}

	@Override
	public void onDisable() {
		HandlerList.unregisterAll();
		Bukkit.getScheduler().cancelTasks(this);
	}

	@Override
	public void onEnable() {
		plugin = this;
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onLoad() {
	}

	private boolean rpgWorld(World world) {
		return world.isGameRule(GAMERULE) && world.getGameRuleValue(GAMERULE).equalsIgnoreCase("true");
	}
}
