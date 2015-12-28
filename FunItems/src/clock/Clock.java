package clock;

import main.FunItem;
import main.User;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class Clock extends User {
	private static final String UUID = "6ac41297318f47018ff943715aaee2d0";
	private static final String ID = ChatColor.RED + UUID;
	public static final FunItem ITEM = new FunItem("Clock's Wand", Material.BLAZE_ROD, "I...HAVE...THE...POWER!", ID);

	static {
		ITEM.item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 4);
	}

	@EventHandler
	public static void lightningStrike(PlayerInteractAtEntityEvent event) {
		if (event.getRightClicked() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getRightClicked();
			if (FunItem.matchFunItem(ID, event.getPlayer()) && FunItem.cooldown(UUID)) {
				entity.getWorld().strikeLightningEffect(entity.getLocation());
				event.getPlayer().spigot().playEffect(entity.getLocation(), Effect.COLOURED_DUST, 0, 0, 255, 255, 0, 0, 0, 10);
				FunItem.TIMER.put(UUID, System.currentTimeMillis());
			}
		}
	}
}
