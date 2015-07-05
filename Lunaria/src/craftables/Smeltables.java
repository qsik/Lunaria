package craftables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class Smeltables {
	private final YamlConfiguration recipes;

	public Smeltables(craftablemain plugin, File pluginFolder) {
		File smeltable = new File(pluginFolder.getPath() + "/smeltable.yml");
		try {
			InputStream input = plugin.getResource("smeltable.yml");
			OutputStream output = new FileOutputStream(smeltable);

			int data = 0;
			while ((data = input.read()) != -1) {
				output.write(data);
			}
			input.close();
			output.close();
		} catch (IOException e) {
			plugin.getLogger().info("Unable to create Smeltables file");
		}

		recipes = YamlConfiguration.loadConfiguration(smeltable);

		for (String key : recipes.getKeys(false)) {
			craftablemain.materials.add(Material.getMaterial(key));
			for (short i = 0; i <= recipes.getInt(key + ".durability"); i++) {
				MaterialData source = new ItemStack(Material.getMaterial(key), 1, i).getData();
				ItemStack result = new ItemStack(Material.matchMaterial(recipes.getString(key + ".result")), recipes.getInt(key + ".ingots"));
				FurnaceRecipe recipe = new FurnaceRecipe(result, source);
				Bukkit.addRecipe(recipe);
			}
		}
	}

	public YamlConfiguration getRecipes() {
		return recipes;
	}
}
