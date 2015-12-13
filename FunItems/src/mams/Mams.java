package mams;

import io.netty.util.internal.ThreadLocalRandom;
import main.FunItem;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class Mams {

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
