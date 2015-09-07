package rpg.heroes.assassin;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class AssassinListener implements Listener {

	@EventHandler
	private void dealDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			LivingEntity target = (LivingEntity) event.getEntity();
			if (event.getDamager().isPermissionSet(Assassin.PERM)) {
				Assassin assassin = (Assassin) Assassin.LIST.get(event.getDamager().getUniqueId());
				assassin.shadowStrike(event);
				assassin.poison(target);
			}
			if (event.getDamager() instanceof Arrow) {
				Arrow arrow = (Arrow) event.getDamager();
				if (arrow.getShooter() instanceof Player) {
					Player player = (Player) arrow.getShooter();
					if (player.isPermissionSet(Assassin.PERM)) {
						Assassin assassin = (Assassin) Assassin.LIST.get(player.getUniqueId());
						if (arrow.getLocation().getY() >= target.getEyeLocation().getY()) {
							assassin.headShot(event);
						}
						assassin.poison(target);
					}
				}
			}
		}
	}


	@EventHandler
	private void login(PlayerJoinEvent event) {
		for (Player sneaker : Assassin.SNEAKING) {
			event.getPlayer().hidePlayer(sneaker);
		}
	}

	@EventHandler
	private void logoff(PlayerQuitEvent event) {
		if (event.getPlayer().isPermissionSet(Assassin.PERM)) {
			Assassin assassin = (Assassin) Assassin.LIST.get(event.getPlayer().getUniqueId());
			assassin.destealth();
		}
		else {
			for (Player sneaker : Assassin.SNEAKING) {
				event.getPlayer().showPlayer(sneaker);
			}
		}
	}

	@EventHandler
	private void playerInteract(PlayerInteractEvent event) {
		if (event.getPlayer().isPermissionSet(Assassin.PERM)) {
			switch (event.getAction()) {
			case RIGHT_CLICK_AIR:
			case RIGHT_CLICK_BLOCK:
				Assassin assassin = (Assassin) Assassin.LIST.get(event.getPlayer().getUniqueId());
				assassin.smokeBomb();
			default:
				break;
			}
		}
	}

	@EventHandler
	private void playerInteractEntity(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof LivingEntity) {
			if (event.getPlayer().isPermissionSet(Assassin.PERM)) {
				Assassin assassin = (Assassin) Assassin.LIST.get(event.getPlayer().getUniqueId());
				assassin.rip((LivingEntity) event.getRightClicked());
			}
		}
	}

	@EventHandler
	private void sneak(PlayerToggleSneakEvent event) {
		if (event.getPlayer().isPermissionSet(Assassin.PERM)) {
			Assassin assassin = (Assassin) Assassin.LIST.get(event.getPlayer().getUniqueId());
			if (event.isSneaking()) {
				assassin.stealth();
			}
			else {
				assassin.destealth();
			}
		}
	}


	@EventHandler
	private void takeDamage(EntityDamageEvent event) {
		if (event.getEntity().isPermissionSet(Assassin.PERM)) {
			Assassin assassin = (Assassin) Assassin.LIST.get(event.getEntity().getUniqueId());
			assassin.destealth();
			assassin.adrenaline();
		}
	}
}
