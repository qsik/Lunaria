package sly;

import io.netty.util.internal.ThreadLocalRandom;
import main.FunItems;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class FireworkArrow implements Runnable {
	public enum EffectColor {
		FUCHSIA (Color.FUCHSIA),
		LIME (Color.LIME),
		ORANGE (Color.ORANGE),
		BLUE (Color.BLUE),
		RED (Color.RED),
		AQUA (Color.AQUA),
		WHITE (Color.WHITE),
		YELLOW (Color.YELLOW);

		private final Color c;

		EffectColor(Color c) {
			this.c = c;
		}

		public Color getColor() {
			return c;
		}
	}

	private static final int COLOR_COUNT = EffectColor.values().length;
	private static final byte MAX_TICKS = 8;
	private Location dx;
	private final World world;
	private final Vector velocity;
	private byte ticksLived = 0;
	private final int taskId;
	private final Plugin plugin = FunItems.plugin;

	public FireworkArrow(Location location, Vector velocity) {
		dx = location;
		this.velocity = velocity;
		world = location.getWorld();
		taskId = Bukkit.getScheduler().runTaskTimer(plugin, this, 0, 1).getTaskId();
	}

	@Override
	public void run() {
		if (!dx.getBlock().getType().equals(Material.AIR) || ticksLived == MAX_TICKS) {
			generateFirework();
			Bukkit.getScheduler().cancelTask(taskId);
		}
		else {
			world.playEffect(dx, Effect.FIREWORKS_SPARK, 0);
			dx.add(velocity);
			ticksLived++;
		}
	}

	private void generateFirework() {
		final Firework firework = (Firework) world.spawnEntity(dx, EntityType.FIREWORK);
		FireworkMeta fireworkMeta = firework.getFireworkMeta();
		ThreadLocalRandom r = ThreadLocalRandom.current();
		FireworkEffect.Builder effect = FireworkEffect.builder().flicker(r.nextBoolean()).trail(r.nextBoolean());
		int maxC = r.nextInt(COLOR_COUNT) + 1;
		for (int i = 0; i < maxC; i++) {
			Color c = EffectColor.values()[r.nextInt(COLOR_COUNT)].getColor();
			effect.withColor(c);
			if (r.nextBoolean()) {
				effect.withFade(c);
			}
		}
		switch (r.nextInt(5)) {
		case 0:
			effect.with(Type.BALL);
			break;
		case 1:
			effect.with(Type.BALL_LARGE);
			break;
		case 2:
			effect.with(Type.BURST);
			break;
		case 3:
			effect.with(Type.CREEPER);
			break;
		case 4:
			effect.with(Type.STAR);
			break;
		}
		fireworkMeta.addEffects(effect.build());
		firework.setFireworkMeta(fireworkMeta);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

			@Override
			public void run() {
				firework.detonate();
			}
		}, 1);
	}
}
