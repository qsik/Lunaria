package sly;

import main.FunItem;
import main.User;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

public class Sly extends User {
	public static final String UUID = "f5caf61e6ab046bf8bbd5eb06442536e";
	public static final String ID = ChatColor.RED + UUID;
	public static final FunItem ITEM = new FunItem("Slyerworks Launcher", Material.BOW, "Oooh...Ahhhh...", ID);

	@EventHandler
	public void shootFirework(EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player) {
			if (FunItem.matchFunItem(ID, (Player) event.getEntity())) {
				Entity arrow = event.getProjectile();
				LivingEntity player = event.getEntity();
				final Location location = arrow.getLocation();
				final Vector velocity = arrow.getVelocity();
				arrow.remove();
				location.add(player.getLocation().getDirection());
				new FireworkArrow(location, velocity);
			}
		}
	}
}
