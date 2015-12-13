package main;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class FunListener implements Listener {

	@EventHandler
	public void fireworksBow(EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player) {
			ItemStack bow = event.getBow();
			if (FunItem.getFunItem(bow).alias.equals("Sly")) {
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
