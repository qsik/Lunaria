package main;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FunItem {
	private static final FunItem NONE = new FunItem("Easter Egg", "Troll", Material.EGG);
	private static Set<FunItem> funItems = new HashSet<FunItem>();
	static {
		funItems.add(new FunItem("Slyerworks Launcher", "Sly", Material.BOW));
		funItems.add(new FunItem("Mamz's MLG No-Scoper Kitty Cannon", "Mamz", Material.IRON_BARDING));
	}
	public final String name;
	public final String alias;
	public final Material material;

	private FunItem(String name, String alias, Material material) {
		this.name = name;
		this.alias = alias;
		this.material = material;
	}

	public static FunItem getFunItem(ItemStack item) {
		if (item.hasItemMeta()) {
			if (item.getItemMeta().hasDisplayName()) {
				for (FunItem funItem : funItems) {
					if (item.getItemMeta().getDisplayName().equals(funItem.name)) {
						if (item.getType().equals(funItem.material)) {
							return funItem;
						}
					}
				}
			}
		}
		return NONE;
	}

	public static String getList() {
		String list = "";
		for (FunItem funItem : funItems) {
			list = list + funItem.alias + ", ";
		}
		return list.substring(0, list.length() - 2);
	}

	public static FunItem search(String alias) {
		for (FunItem funItem : funItems) {
			if (funItem.alias.equalsIgnoreCase(alias)) {
				return funItem;
			}
		}
		return NONE;
	}
}
