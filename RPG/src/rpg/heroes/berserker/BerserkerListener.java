package rpg.heroes.berserker;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BerserkerListener implements Listener {

	@EventHandler
	private void damage(EntityDamageByEntityEvent event) {
		if (event.getDamager().isPermissionSet(Berserker.PERM)) {
			if (event.getEntity() instanceof LivingEntity) {
				Berserker berserker = (Berserker) Berserker.LIST.get(event.getDamager().getUniqueId());
				berserker.bloodlust();
			}
		}
	}

	@EventHandler
	private void playerInteract(PlayerInteractEvent event) {
		if (event.getPlayer().isPermissionSet(Berserker.PERM)) {
			Berserker berserker = (Berserker) Berserker.LIST.get(event.getPlayer().getUniqueId());
			switch (event.getAction()) {
			case LEFT_CLICK_AIR:
			case LEFT_CLICK_BLOCK:
				if (event.getPlayer().isSneaking()) {
					berserker.groundslam();
				}
				break;
			case RIGHT_CLICK_AIR:
			case RIGHT_CLICK_BLOCK:
				if (event.getPlayer().isSneaking()) {
					berserker.berserking();
				}
				else {
					berserker.battlecry();
				}
				break;
			default:
				break;
			}
		}
	}
}
