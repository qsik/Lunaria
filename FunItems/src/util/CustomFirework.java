package util;

import main.FunItems;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class CustomFirework extends BukkitRunnable  {
	public enum EffectColor {
		FUCHSIA (Color.FUCHSIA),
		LIME (Color.LIME),
		ORANGE (Color.ORANGE),
		BLUE (Color.BLUE),
		RED (Color.RED),
		AQUA (Color.AQUA),
		WHITE (Color.WHITE),
		BLACK (Color.BLACK),
		GRAY (Color.GRAY),
		MAROON (Color.MAROON),
		NAVY (Color.NAVY),
		OLIVE (Color.OLIVE),
		TEAL (Color.TEAL),
		GREEN (Color.GREEN),
		SILVER (Color.SILVER),
		PURPLE (Color.PURPLE),
		YELLOW (Color.YELLOW);

		public final Color color;

		EffectColor(Color color) {
			this.color = color;
		}
	}

	private Location p;
	private final World world;
	private final Vector v;
	private int ticks;
	private FireworkData data;

	public CustomFirework(Location location, Vector velocity, int ticks) {
		this.p = location;
		this.v = velocity;
		world = location.getWorld();
		this.ticks = ticks;
		this.runTaskTimer(FunItems.plugin, 0, 1);
	}

	@Override
	public void run() {
		if (ticks == 0 || !p.clone().add(v).getBlock().isEmpty()) {
			generateFirework();
			this.cancel();
		}
		else {
			world.playEffect(p, Effect.FIREWORKS_SPARK, 0);
			p.add(v);
			ticks--;
		}
	}

	public CustomFirework setFirework(FireworkData data) {
		this.data = data;
		return this;
	}

	private void generateFirework() {
		final Firework firework = (Firework) world.spawnEntity(p, EntityType.FIREWORK);
		FireworkMeta meta = firework.getFireworkMeta();
		if (data == null) {
			data = new FireworkData();
		}
		firework.setFireworkMeta(data.buildMeta(meta));
		Bukkit.getScheduler().runTaskLater(FunItems.plugin, new Runnable() {
			@Override
			public void run() {
				firework.detonate();
				firework.playEffect(EntityEffect.FIREWORK_EXPLODE);
			}
		}, 1);
	}
}
