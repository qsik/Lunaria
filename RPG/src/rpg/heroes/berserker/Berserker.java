package rpg.heroes.berserker;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import rpg.Main;
import rpg.heroes.Hero;

import com.google.common.base.Stopwatch;

public class Berserker extends Hero {
	public static final String PERM = "Berserker";
	private static final BerserkerListener BERSERKER_LISTENER = new BerserkerListener();
	static {
		Bukkit.getPluginManager().registerEvents(BERSERKER_LISTENER, Main.plugin);
	}
	private static final PotionEffect BLOODLUST_EFFECT = new PotionEffect(PotionEffectType.SPEED, 100, 0);
	private static final long GROUNDSLAM_COOLDOWN = 10000;
	private static final long BERSERKING_COOLDOWN = 30000;
	private static final int BERSERKING_DURATION = 100;
	private static final Set<PotionEffect> BERSERKING_BUFFS = new HashSet<PotionEffect>();
	static {
		BERSERKING_BUFFS.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, BERSERKING_DURATION, 3));
		BERSERKING_BUFFS.add(new PotionEffect(PotionEffectType.SPEED, BERSERKING_DURATION, 3));
		BERSERKING_BUFFS.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, BERSERKING_DURATION, 3));
		BERSERKING_BUFFS.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, BERSERKING_DURATION, 3));
	}
	private static final Set<PotionEffect> BERSERKING_DEBUFFS = new HashSet<PotionEffect>();
	static {
		BERSERKING_DEBUFFS.add(new PotionEffect(PotionEffectType.WEAKNESS, BERSERKING_DURATION, 3));
		BERSERKING_DEBUFFS.add(new PotionEffect(PotionEffectType.SLOW, BERSERKING_DURATION, 3));
		BERSERKING_DEBUFFS.add(new PotionEffect(PotionEffectType.BLINDNESS, BERSERKING_DURATION, 3));
		BERSERKING_DEBUFFS.add(new PotionEffect(PotionEffectType.HUNGER, BERSERKING_DURATION, 3));
	}
	private static final int BATTLECRY_DURATION = 100;
	private static final long BATTLECRY_COOLDOWN = 10000;
	private static final Set<PotionEffect> GROUNDSLAM_EFFECTS = new HashSet<PotionEffect>();
	static {
		GROUNDSLAM_EFFECTS.add(new PotionEffect(PotionEffectType.WEAKNESS, 60, 3));
		GROUNDSLAM_EFFECTS.add(new PotionEffect(PotionEffectType.SLOW, 60, 3));
	}
	private final Stopwatch berserkingTimer = Stopwatch.createStarted();
	private final Stopwatch groundslamTimer = Stopwatch.createStarted();
	private final Stopwatch battlecryTimer = Stopwatch.createStarted();


	public Berserker(Player player) {
		super(player);
		PERMS.get(player.getUniqueId()).setPermission(PERM, true);
	}

	public void battlecry() {
		if (battlecryTimer.elapsed(TimeUnit.MILLISECONDS) >= BATTLECRY_COOLDOWN) {
			double healthMultiplier = player.getMaxHealth() / player.getHealth();
			double targetsMultiplier = 0;
			for (Entity entity : player.getNearbyEntities(5, 2, 5)) {
				if (entity instanceof LivingEntity) {
					targetsMultiplier++;
				}
			}
			targetsMultiplier = targetsMultiplier / 2.0D;
			int amplifier = (int) (healthMultiplier + targetsMultiplier);
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, BATTLECRY_DURATION, amplifier));
			battlecryTimer.reset();
			battlecryTimer.start();
		}
	}

	public void berserking() {
		if (berserkingTimer.elapsed(TimeUnit.MILLISECONDS) >= BERSERKING_COOLDOWN) {
			player.addPotionEffects(BERSERKING_BUFFS);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
				@Override
				public void run() {
					player.addPotionEffects(BERSERKING_DEBUFFS);
					berserkingTimer.reset();
					berserkingTimer.start();
				}
			}, BERSERKING_DURATION);
		}
	}

	public void bloodlust() {
		player.addPotionEffect(BLOODLUST_EFFECT);
	}

	@Override
	public void cleanup() {
		PERMS.get(player.getUniqueId()).unsetPermission(Berserker.PERM);
	}

	public void groundslam() {
		if (groundslamTimer.elapsed(TimeUnit.MILLISECONDS) >= GROUNDSLAM_COOLDOWN) {
			for (Entity entity : player.getNearbyEntities(5, 2, 5)) {
				if (entity instanceof LivingEntity) {
					LivingEntity target = (LivingEntity) entity;
					if (target instanceof Creature) {
						((Creature) target).setTarget(player);
					}
					target.setVelocity(player.getLocation().subtract(target.getLocation()).toVector());
					target.addPotionEffects(GROUNDSLAM_EFFECTS);
				}
			}
			groundslamTimer.reset();
			groundslamTimer.start();
		}
	}
}
