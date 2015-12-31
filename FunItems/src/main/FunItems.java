package main;

import main.User.UserList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import clock.Clock;
import sly.Sly;

public class FunItems extends JavaPlugin {
	public static FunItems plugin;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("funitem")) {
			switch (args[0].toLowerCase()) {
			case "sly":
				((Player) sender).getWorld().dropItemNaturally(((Player) sender).getLocation(), Sly.ITEM.item);
				break;
			case "clock":
				((Player) sender).getWorld().dropItemNaturally(((Player) sender).getLocation(), Clock.ITEM.item);
			}
		}
		return false;
	}

	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
		Bukkit.getScheduler().cancelTasks(this);
	}

	@Override
	public void onEnable() {
		plugin = this;
		for (UserList userList : UserList.values()) {
			userList.user.initialize();
		}
	}

	public static void registerFunItem(User user) {

	}
}
