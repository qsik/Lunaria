package rpg.heroes.shaman;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;

import rpg.Main;
import rpg.heroes.Hero;

import com.google.common.base.Stopwatch;

public class Shaman extends Hero {
	public static final String PERM = "Shaman";
	public static final ShamanListener LISTENER = new ShamanListener();
	static {
		Bukkit.getPluginManager().registerEvents(LISTENER, Main.plugin);
	}
	public static final int MAX_STORM = 100;
	public static final String FIREBALL_NAME = "Shaman Fireball";
	public static final String SNOWBALL_NAME = "Shaman Snowball";
	public static final int FIREBALL_DAMAGE = 10;
	public static final int SNOWBALL_DAMAGE = 10;
	public static final int LIGHTNING_COST = 30;
	public static final int TELEPORT_COST = 20;
	public static final int LIGHTNING_DAMAGE = 10;
	public static final long FIREBALL_COOLDOWN = 500;
	public static final long SNOWBALL_COOLDOWN = 500;
	private final Stopwatch fireballTimer = Stopwatch.createStarted();
	private final Stopwatch snowballTimer = Stopwatch.createStarted();
	private int storm = 0;

	public Shaman(Player player) {
		super(player);
		PERMS.get(player.getUniqueId()).setPermission(PERM, true);
	}

	public void addStorm(double amount) {
		storm += amount;
		checkStorm();
	}

	public void checkStorm() {
		if (storm >= MAX_STORM) {
			storm = MAX_STORM;
			Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin,
					new Runnable() {
				@Override
				public void run() {
					storm = 0;
				}
			}, 30 * 20);
		}
	}

	@Override
	public void cleanup() {
		PERMS.get(player.getUniqueId()).unsetPermission(PERM);
	}

	public void fireball() {
		if (fireballTimer.elapsed(TimeUnit.MILLISECONDS) >= FIREBALL_COOLDOWN) {
			Fireball fireball = storm == MAX_STORM? player.launchProjectile(LargeFireball.class) : player.launchProjectile(SmallFireball.class);
			fireball.setIsIncendiary(false);
			fireball.setYield(0);
			fireball.setCustomName(FIREBALL_NAME);
			fireballTimer.reset();
			fireballTimer.start();
		}
	}

	public void lightning(LivingEntity target) {
		if (storm >= LIGHTNING_COST) {
			storm -= storm == MAX_STORM? 0 : LIGHTNING_COST;
			player.getWorld().strikeLightningEffect(target.getLocation());
			target.damage(LIGHTNING_DAMAGE, player);
			target.setFireTicks(40);
		}
	}

	public void snowball() {
		if (snowballTimer.elapsed(TimeUnit.MILLISECONDS) >= SNOWBALL_COOLDOWN) {
			Snowball snowball = player.launchProjectile(Snowball.class);
			snowball.setCustomName(SNOWBALL_NAME);
			snowballTimer.reset();
			snowballTimer.start();
		}
	}

	public void teleport() {
		if (storm >= TELEPORT_COST) {
			storm -= storm == MAX_STORM? 0 : TELEPORT_COST;
			player.setVelocity(player.getEyeLocation().getDirection().multiply(5));
			player.sendMessage("Storm Remaining: " + storm);
		}
	}
}
