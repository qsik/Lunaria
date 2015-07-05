package craftables;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class craftablemain extends JavaPlugin implements Listener {
	private World world;
	private Smeltables smeltables;
	private Craftables craftables;
	public static Set<Material> materials = new HashSet<Material>();
	public static Set<Location> furnaceExp = new HashSet<Location>();

	@Override
	public void onDisable() {
		Bukkit.resetRecipes();
	}

	@Override
	public void onEnable() {
		if ((world = Bukkit.getWorld("Survival")) == null) {
			world = Bukkit.getWorlds().iterator().next();
		}

		File pluginFolder = new File(new File("").getAbsolutePath()	+ "/plugins/Ingot Recovery");
		if (pluginFolder.exists() == false) {
			if (pluginFolder.mkdir() == false) {
				this.getLogger().info("Unable to create Plugin folder");
			}
		}

		Bukkit.getPluginManager().registerEvents(this, this);
		smeltables = new Smeltables(this, pluginFolder);
		craftables = new Craftables(this, pluginFolder);
	}

	@EventHandler
	private void extract(FurnaceExtractEvent event) {
		if (furnaceExp.contains(event.getBlock().getLocation())) {
			event.setExpToDrop(0);
		}
	}

	private int getIngots(double mean) {
		return (int) (ThreadLocalRandom.current().nextGaussian() + mean);
	}

	@EventHandler
	private void smelt(FurnaceSmeltEvent event) {
		if (world.getGameRuleValue("RecoverIngots").equalsIgnoreCase("true")) {
			if (materials.contains(event.getSource().getType())) {
				if (event.getResult().getAmount() > 1) {
					ItemStack result = event.getResult();
					String key = event.getSource().getType().name();
					int maxIngots = result.getAmount();
					int maxDurability = smeltables.getRecipes().getInt(key + ".DURABILITY");
					short durability = (short) (maxDurability - event.getSource().getDurability());
					double mean = (double) maxIngots * durability / maxDurability;
					int ingots = getIngots(mean);
					while (ingots > maxIngots || ingots < 0) {
						ingots = getIngots(mean);
					}
					if (ingots == 0) {
						event.setResult(new ItemStack(Material.AIR));
					}
					else {
						result.setAmount(ingots);
					}
					furnaceExp.add(event.getBlock().getLocation());
				}
			}
		}
		else {
			if (materials.contains(event.getSource().getType())) {
				event.setCancelled(true);
			}
		}
	}
}
