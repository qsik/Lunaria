package fourth;

import io.netty.util.internal.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class Surprise {
	private static final ItemStack CAKE = new ItemStack(Material.CAKE);
	private static final ItemStack COOKIE = new ItemStack(Material.COOKIE);
	private static final ItemStack STEAK = new ItemStack(Material.COOKED_BEEF);

	static {
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.LIGHT_PURPLE + "For Celebrating Invalid's Birthday!");
		ItemMeta cakeMeta = CAKE.getItemMeta();
		cakeMeta.setLore(lore);
		cakeMeta.setDisplayName(ChatColor.RED + "Invalid Cake");
		ItemMeta cookieMeta = COOKIE.getItemMeta();
		cookieMeta.setLore(lore);
		cookieMeta.setDisplayName(ChatColor.RED + "Invalid Cookie");
		CAKE.setItemMeta(cakeMeta);
		COOKIE.setItemMeta(cookieMeta);
		ItemMeta steakMeta = STEAK.getItemMeta();
		steakMeta.setDisplayName(ChatColor.RED + "U" + ChatColor.WHITE + "S" + ChatColor.BLUE + "A" + ChatColor.YELLOW + " Steak");
		List<String> steakLore = new ArrayList<String>();
		steakLore.add(ChatColor.GOLD + "Good Ol' 100% American Beef");
		steakMeta.setLore(steakLore);
		STEAK.setItemMeta(steakMeta);
	}

	public static void cookout(Player player) {
		World world = player.getWorld();
		Location location = player.getLocation();
		world.dropItemNaturally(location, STEAK);
	}

	public static void party(Player player) {
		World world = player.getWorld();
		Location location = player.getLocation();
		world.dropItemNaturally(location, CAKE);
		world.dropItemNaturally(location, COOKIE);
	}

	public static void usa(Player player) {
		Location location = player.getLocation();
		ThreadLocalRandom r = ThreadLocalRandom.current();
		new FourthFireworks(location, new Vector(r.nextDouble(0.2), 0.5, r.nextDouble(0.2)));
	}
}
