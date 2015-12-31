package sly;

import main.FunItem;
import main.User;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

import util.CustomFirework;

public class Sly extends User {
	public static final String UUID = "f5caf61e6ab046bf8bbd5eb06442536e";
	public static final FunItem ITEM = new FunItem("Slyerworks Launcher", Material.BOW, org.bukkit.ChatColor.RED + "Oooh...Ahhhh...", UUID);

	static {
		ITEM.item.addEnchantment(Enchantment.ARROW_INFINITE, 1);
	}

	@EventHandler
	public void shootFirework(EntityShootBowEvent event) throws IllegalArgumentException, Exception {
		if (event.getEntity() instanceof Player) {
			if (FunItem.matchFunItem(UUID, (Player) event.getEntity())) {
				Entity arrow = event.getProjectile();
				final Location location = arrow.getLocation();
				final Vector velocity = arrow.getVelocity();
				arrow.remove();
				new CustomFirework(location, velocity, 8);
			}
		}
	}
}
