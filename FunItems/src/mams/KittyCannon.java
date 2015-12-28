package mams;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.craftbukkit.Main;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Ocelot;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;

public class KittyCannon implements Runnable {
	private static final long CAT_LIFETIME = 24;
	private final Ocelot cat;
	private final Plugin plugin = Main.plugin;

	public KittyCannon(Ocelot cat) {
		this.cat = cat;
		Bukkit.getScheduler().runTaskLater(plugin, this, CAT_LIFETIME);
	}

	@Override
	public void run() {
		final Location location = cat.getLocation();
		cat.remove();
		final Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
		FireworkMeta meta = firework.getFireworkMeta();
		meta.addEffects(FireworkEffect.builder().trail(false).flicker(false).withColor(Color.RED).with(Type.BALL).build());
		firework.setFireworkMeta(meta);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				firework.detonate();
			}
		}, 1);
	}
}
