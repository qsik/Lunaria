package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FunItem {
	private static final long COOLDOWN = 1000;
	public static final Map<String, Long> TIMER = new HashMap<String, Long>();
	public final ItemStack item;

	public FunItem(String name, Material mat, String lore, String uuid) {
		this.item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		List<String> loreList = new ArrayList<String>();
		loreList.add(lore);
		loreList.add(uuid);
		meta.setLore(loreList);
		meta.spigot().setUnbreakable(true);
		meta.setDisplayName(name);
		item.setItemMeta(meta);
	}

	public static boolean cooldown(String uuid) {
		if (TIMER.containsKey(uuid)) {
			return System.currentTimeMillis() - TIMER.get(uuid) >= COOLDOWN;
		}
		return true;
	}

	public static boolean matchFunItem(String id, Player player) {
		if (player.getItemInHand().hasItemMeta()) {
			if (player.getItemInHand().getItemMeta().hasLore()) {
				return (player.getItemInHand().getItemMeta().getLore().contains(id));
			}
		}
		return false;
	}
}
