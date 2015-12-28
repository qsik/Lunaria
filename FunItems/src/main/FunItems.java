package main;

import main.User.UserList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class FunItems extends JavaPlugin {


	public static Plugin plugin;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("funitem")) {
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
