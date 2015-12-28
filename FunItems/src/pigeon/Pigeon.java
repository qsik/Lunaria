package pigeon;

import java.util.ArrayList;
import java.util.List;

import main.User;

import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Pigeon extends User {
	public static final String UUID = "dd23cc7b22444348a43a8c916dd53385";

	@EventHandler
	public void pigeonCook(FurnaceSmeltEvent event) {
		if (event.getSource().getItemMeta().getDisplayName().equals(RAW_PIGEON)) {
			ItemStack cookedPigeon = new ItemStack(Material.COOKED_CHICKEN);
			ItemMeta meta = cookedPigeon.getItemMeta();
			meta.setDisplayName(COOKED_PIGEON);
			List<String> lore = new ArrayList<String>();
			lore.add("Gives you wings!");
			meta.setLore(lore);
			cookedPigeon.setItemMeta(meta);
			event.setResult(cookedPigeon);
		}
	}

	@EventHandler
	public void pigeonDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Chicken) {
			Chicken chicken = (Chicken) event.getEntity();
			if (chicken.getCustomName().equalsIgnoreCase("EVIL_PIGEON")) {
				event.setDroppedExp(20);
				event.getDrops().removeAll(event.getDrops());
				ItemStack rawPigeon = new ItemStack(Material.RAW_CHICKEN);
				ItemMeta meta = rawPigeon.getItemMeta();
				meta.setDisplayName(RAW_PIGEON);
				List<String> lore = new ArrayList<String>();
				lore.add("Cook and eat!");
				meta.setLore(lore);
				rawPigeon.setItemMeta(meta);
				chicken.getWorld().dropItemNaturally(chicken.getLocation(), rawPigeon);
			}
		}
	}

	@EventHandler
	public void pigeonEat(PlayerItemConsumeEvent event) {
		if (event.getItem().getItemMeta().getDisplayName().equals(COOKED_PIGEON)) {
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2400, 10));
		}
	}
}
