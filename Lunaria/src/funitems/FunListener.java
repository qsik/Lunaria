package funitems;

import io.netty.util.internal.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import runnables.FireworkArrow;
import runnables.KittyCannon;

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

	@EventHandler
	public void kittyCannon(PlayerInteractEvent event) {
		switch (event.getAction()) {
		case RIGHT_CLICK_AIR:
		case RIGHT_CLICK_BLOCK:
			if (event.hasItem()) {
				if (FunItem.getFunItem(event.getItem()).alias.equals("Mamz")) {
					Location location = event.getPlayer().getLocation();
					Vector velocity = location.getDirection().multiply(3);
					Ocelot cat = (Ocelot) location.getWorld().spawnEntity(location, EntityType.OCELOT);
					cat.setBaby();
					ThreadLocalRandom r = ThreadLocalRandom.current();
					int i = r.nextInt(Ocelot.Type.values().length);
					cat.setCatType(Ocelot.Type.values()[i]);
					cat.setVelocity(velocity);
					new KittyCannon(cat);
				}
			}
			break;
		default:
			break;
		}
	}
}
