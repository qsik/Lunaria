package fourth;

import io.netty.util.internal.ThreadLocalRandom;
import lunaria.Main;

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

public class FourthFireworks implements Runnable {
	private static final byte MAX_TICKS = 40;
	private Location dx;
	private final World world;
	private final Vector velocity;
	private byte ticksLived = 0;
	private final int taskId;
	private final Plugin plugin = Main.plugin;

	public FourthFireworks(Location location, Vector velocity) {
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
		effect.withColor(Color.RED).withFade(Color.RED);
		effect.withColor(Color.BLUE).withFade(Color.BLUE);
		effect.withColor(Color.WHITE).withFade(Color.WHITE);
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
