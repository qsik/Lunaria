package rpg.heroes.shaman;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import rpg.heroes.Hero;

public class ShamanListener implements Listener {

	@EventHandler
	private static void damage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			if (event.getDamager().isPermissionSet(Shaman.PERM)) {
				Shaman shaman = (Shaman) Shaman.LIST.get(event.getDamager().getUniqueId());
				shaman.addStorm(event.getDamage());
			}
			else if (event.getDamager() instanceof Projectile) {
				if (event.getDamager() instanceof Snowball) {
					Snowball snowball = (Snowball) event.getDamager();
					if (snowball.getCustomName().equals(Shaman.SNOWBALL_NAME)) {
						event.setDamage(Shaman.SNOWBALL_DAMAGE);
						Player player = ((Player)snowball.getShooter());
						Shaman shaman = (Shaman) Hero.LIST.get(player.getUniqueId());
						shaman.addStorm(Shaman.SNOWBALL_DAMAGE);
					}
				}
				if (event.getDamager() instanceof Fireball) {
					Fireball fireball = (Fireball) event.getDamager();
					if (fireball.getCustomName().equals(Shaman.FIREBALL_NAME)) {
						event.setDamage(Shaman.FIREBALL_DAMAGE);
						Player player = ((Player)fireball.getShooter());
						Shaman shaman = (Shaman) Hero.LIST.get(player.getUniqueId());
						shaman.addStorm(Shaman.SNOWBALL_DAMAGE);
					}
				}
			}
		}

	}

	@EventHandler
	private static void playerInteract(PlayerInteractEvent event) {
		if (event.getPlayer().isPermissionSet(Shaman.PERM)) {
			Shaman shaman = (Shaman) Hero.LIST.get(event.getPlayer().getUniqueId());
			switch (event.getAction()) {
			case LEFT_CLICK_AIR:
			case LEFT_CLICK_BLOCK:
				if (event.getPlayer().isSneaking()) {
					shaman.teleport();
				}
				else {
					shaman.fireball();
				}
				break;
			case RIGHT_CLICK_AIR:
			case RIGHT_CLICK_BLOCK:
				shaman.snowball();
				break;
			default:
				break;
			}
		}
	}

	@EventHandler
	private static void playerInteractEntity(PlayerInteractEntityEvent event) {
		if (event.getPlayer().isPermissionSet(Shaman.PERM)) {
			if (event.getRightClicked() instanceof LivingEntity) {
				if (event.getPlayer().isSneaking()) {
					Shaman shaman = (Shaman) Shaman.LIST.get(event.getPlayer().getUniqueId());
					shaman.lightning((LivingEntity) event.getRightClicked());
				}
			}
		}
	}
}
