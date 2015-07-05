package craftables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class Craftables {
	private final YamlConfiguration recipes;

	public Craftables(craftablemain plugin, File pluginFolder) {
		File craftable = new File(pluginFolder.getPath() + "/craftable.yml");
		try {
			InputStream input = plugin.getResource("craftable.yml");
			OutputStream output = new FileOutputStream(craftable);

			int data = 0;
			while ((data = input.read()) != -1) {
				output.write(data);
			}
			input.close();
			output.close();
		} catch (IOException e) {
			plugin.getLogger().info("Unable to create Craftables file");
		}

		recipes = YamlConfiguration.loadConfiguration(craftable);

		for (String key : recipes.getKeys(false)) {
			Material result = Material.matchMaterial(recipes.getString(key + ".result.material"));
			int quantity = recipes.getInt(key + ".result.quantity");
			ShapedRecipe recipe = new ShapedRecipe(new ItemStack(result, quantity));
			craftablemain.materials.add(result);
			String rawmap = recipes.getString(key + ".shape.map");
			String[] shapemap = {rawmap.substring(0, 3), rawmap.substring(3, 6), rawmap.substring(6, 9)};
			Bukkit.getLogger().info(shapemap[1]);
			recipe.shape(shapemap);
			for (String k : recipes.getConfigurationSection(key).getKeys(false)) {
				if (!k.equals("result") && !k.equals("shape")) {
					Material mat = Material.matchMaterial(recipes.getString(key + "." + k + ".material"));
					int amount = recipes.getInt(key + "." + k + ".quantity");
					ItemStack ingredient = new ItemStack(mat, amount);
					recipe.setIngredient(k.charAt(0), ingredient.getData());
				}
			}
			Bukkit.addRecipe(recipe);
		}
	}

	public YamlConfiguration getRecipes() {
		return recipes;
	}
}
