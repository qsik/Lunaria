package mams;

import io.netty.util.internal.ThreadLocalRandom;
import main.FunItems;

import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Ocelot;
import org.bukkit.inventory.meta.FireworkMeta;

import util.CustomFirework.EffectColor;

public class KittyCannon implements Runnable {
	private static final long CAT_LIFETIME = 24;
	private final Ocelot cat;
	private final FunItems plugin = FunItems.plugin;

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
		ThreadLocalRandom r = ThreadLocalRandom.current();
		meta.addEffects(FireworkEffect.builder().trail(false)
				.flicker(false).withColor(EffectColor.values()[r.nextInt(17)].color).with(Type.BALL).build());
		firework.setFireworkMeta(meta);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				firework.detonate();
			}
		}, 1);
	}
}
