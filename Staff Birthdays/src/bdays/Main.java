package bdays;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin implements Listener {
	public static Main plugin;
	public static final String RAW_PIGEON = ChatColor.GOLD + "" + ChatColor.MAGIC + "Raw Pigeon";
	public static final String COOKED_PIGEON = ChatColor.GOLD + "" + ChatColor.MAGIC + "Cooked Pigeon";
	public static final String BAY_BALE = ChatColor.RED + "" + ChatColor.ITALIC +  "Bay Bayle";

	@EventHandler
	public void bayBayle(BlockPlaceEvent event) {
		if (event.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "" + ChatColor.ITALIC +  "Bay Bayle")) {
			PotionEffect blind = new PotionEffect(PotionEffectType.INVISIBILITY, 200, 10);
			for (Entity entity : event.getPlayer().getNearbyEntities(10, 10, 10)) {
				if (entity instanceof Player) {
					((Player) entity).addPotionEffect(blind);
				}
			}
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

	@EventHandler
	public void pigeonCook(FurnaceSmeltEvent event) {
		if (event.getSource().getItemMeta().getDisplayName().equals(RAW_PIGEON)) {
			ItemStack cookedPigeon = new ItemStack(Material.COOKED_CHICKEN);
			ItemMeta meta = cookedPigeon.getItemMeta();
			meta.setDisplayName(COOKED_PIGEON);
			List<String> lore = new ArrayList<String>();
			lore.add("Gives you wings!");
			meta.setLore(lore);
			cookedPigeon.setItemMeta(meta);
			event.setResult(cookedPigeon);
		}
	}

	@EventHandler
	public void pigeonDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Chicken) {
			Chicken chicken = (Chicken) event.getEntity();
			if (chicken.getCustomName().equalsIgnoreCase("EVIL_PIGEON")) {
				event.setDroppedExp(20);
				event.getDrops().removeAll(event.getDrops());
				ItemStack rawPigeon = new ItemStack(Material.RAW_CHICKEN);
				ItemMeta meta = rawPigeon.getItemMeta();
				meta.setDisplayName(RAW_PIGEON);
				List<String> lore = new ArrayList<String>();
				lore.add("Cook and eat!");
				meta.setLore(lore);
				rawPigeon.setItemMeta(meta);
				chicken.getWorld().dropItemNaturally(chicken.getLocation(), rawPigeon);
			}
		}
	}

	@EventHandler
	public void pigeonEat(PlayerItemConsumeEvent event) {
		if (event.getItem().getItemMeta().getDisplayName().equals(COOKED_PIGEON)) {
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2400, 10));
		}
	}
}
