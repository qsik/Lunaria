package main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemGenerator {
	public static final String KEY = "Fun Item";
	private static List<String> lore = new ArrayList<String>();
	static {
		lore.add(KEY);
	}

	public static boolean convert(ItemStack item) {
		if (item.hasItemMeta()) {
			if (item.getItemMeta().hasLore()) {
				if (item.getItemMeta().getLore().contains(KEY)) {
					boolean breakable = !item.getItemMeta().spigot().isUnbreakable();
					ItemStack funItem = null;
					switch (item.getType()) {
					case BOW:
						funItem = generateSly(breakable);
						break;
					case IRON_BARDING:
						funItem = generateMamz(breakable);
						break;
					default:
						break;
					}
					if (funItem != null) {
						item.setItemMeta(funItem.getItemMeta());
						return true;
					}
				}
			}
		}
		return false;
	}

	public static ItemStack generateFunItem(String funItem, boolean breakable) {
		switch (funItem.toUpperCase()) {
		case "SLY":
			return generateSly(breakable);
		case "MAMZ":
			return generateMamz(breakable);
		default:
			return null;
		}
	}

	private static ItemStack generateMamz(boolean breakable) {
		ItemStack cannon = new ItemStack(FunItem.search("Mamz").material);
		ItemMeta meta = cannon.getItemMeta();
		meta.setLore(lore);
		meta.spigot().setUnbreakable(!breakable);
		meta.setDisplayName(FunItem.search("Mamz").name);
		cannon.setItemMeta(meta);
		return cannon;
	}

	private static ItemStack generateSly(boolean breakable) {
		ItemStack bow = new ItemStack(FunItem.search("Sly").material);
		bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		ItemMeta meta = bow.getItemMeta();
		meta.setLore(lore);
		meta.spigot().setUnbreakable(!breakable);
		meta.setDisplayName(FunItem.search("Sly").name);
		bow.setItemMeta(meta);
		return bow;
	}
}
