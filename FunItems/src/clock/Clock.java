package clock;

import main.FunItem;
import main.FunItems;
import main.User;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Clock extends User {
	private static final String UUID = "6ac41297318f47018ff943715aaee2d0";
	public static final FunItem ITEM = new FunItem("Clock's Wand", Material.BLAZE_ROD, ChatColor.RED + "I...HAVE...THE...POWER!", UUID);

	static {
		ITEM.item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 4);
	}

	@EventHandler
	public static void lightningStrike(PlayerInteractAtEntityEvent event) {
		if (event.getRightClicked() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getRightClicked();
			if (FunItem.matchFunItem(UUID, event.getPlayer()) && FunItem.cooldown(UUID)) {
				final Location location = entity.getLocation();
				final World world = entity.getWorld();
				world.spigot().playEffect(location.clone().add(0, 1, 0), Effect.PORTAL, 0, 0, 0, 0, 0, 2, 50, 10);
				new BukkitRunnable() {
					@Override
					public void run() {
						world.strikeLightningEffect(location);
					}
				}.runTaskLater(FunItems.plugin, 55);
				FunItem.TIMER.put(UUID, System.currentTimeMillis());
			}
		}
	}
}
