package bdays;

import io.netty.util.internal.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Main extends JavaPlugin implements Listener {
	public static Main plugin;
	public static final String RAW_PIGEON = ChatColor.GOLD + "" + ChatColor.MAGIC + "Raw Pigeon";
	public static final String COOKED_PIGEON = ChatColor.GOLD + "" + ChatColor.MAGIC + "Cooked Pigeon";
	public static final String BAY_BALE = ChatColor.RED + "" + ChatColor.ITALIC +  "Bay Bayle";

	@EventHandler
	public void bayBayle(BlockPlaceEvent event) {
		if (event.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "" + ChatColor.ITALIC +  "Bay Bayle")) {
			final ItemStack baygle = new ItemStack(Material.BREAD);
			ItemMeta meta = baygle.getItemMeta();
			final Location location = event.getBlockPlaced().getLocation();
			meta.setDisplayName(ChatColor.RED + "" + ChatColor.ITALIC + "Baygle");
			List<String> lore = new ArrayList<String>();
			lore.add("Bae");
			meta.setLore(lore);
			baygle.setItemMeta(meta);
			final World world = event.getPlayer().getWorld();
			final int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				@Override
				public void run() {
					Item item = world.dropItem(location.add(0, 1, 0), baygle);
					double x = ThreadLocalRandom.current().nextDouble(-0.1, 0.1);
					double z = ThreadLocalRandom.current().nextDouble(-0.1, 0.1);
					item.setVelocity(new Vector(x, 0.2, z));
				}
			}, 0, 15);
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

				@Override
				public void run() {
					Bukkit.getScheduler().cancelTask(taskId);
				}
			}, 100);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (cmd.getName().equalsIgnoreCase("party")) {
				if (args.length == 0) {
					for (Birthday birthday : Birthday.values()) {
						sender.sendMessage(birthday.toString());
					}
				}
				if (args.length == 1 && Birthday.getBirthday(args[0]) != null) {
					Birthday birthday = Birthday.getBirthday(args[0]);
					new Fireworks((Player) sender, birthday);
				}
			}
		}
		return false;
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEnable() {
		plugin = this;
		Bukkit.getPluginManager().registerEvents(this, this);
	}


}
