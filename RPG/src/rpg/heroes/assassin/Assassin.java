package rpg.heroes.assassin;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import rpg.Main;
import rpg.heroes.Hero;

import com.google.common.base.Stopwatch;

public class Assassin extends Hero {
	public static final String PERM = "Assassin";
	public static final Queue<Player> SNEAKING = new ConcurrentLinkedQueue<Player>();
	private static final AssassinListener ASSASSIN_LISTENER = new AssassinListener();
	static {
		Bukkit.getPluginManager().registerEvents(ASSASSIN_LISTENER, Main.plugin);
	}
	private static final PotionEffect RIP_EFFECT = new PotionEffect(PotionEffectType.WITHER, 100, 0);
	private static final int POISON_DURATION = 90;
	private static final double RIP_DAMAGE = 3;
	private static final long ADRENALINE_COOLDOWN = 30000;
	private static final long SMOKE_BOMB_COOLDOWN = 10000;
	private static final double HEADSHOT_MULTIPLIER = 2;
	private static final double SHADOW_STRIKE_MULTIPLIER = 2;
	private static final PotionEffect SMOKE_BOMB_EFFECT = new PotionEffect(PotionEffectType.BLINDNESS, 100, 3);
	private static final Set<PotionEffect> SHADOW_STRIKE_EFFECTS = new HashSet<PotionEffect>();
	static {
		SHADOW_STRIKE_EFFECTS.add(new PotionEffect(PotionEffectType.WEAKNESS, 60, 3));
		SHADOW_STRIKE_EFFECTS.add(new PotionEffect(PotionEffectType.SLOW, 60, 3));
	}
	private static final Set<PotionEffect> ADRENALINE_EFFECTS = new HashSet<PotionEffect>();
	static {
		ADRENALINE_EFFECTS.add(new PotionEffect(PotionEffectType.SPEED, 100, 3));
		ADRENALINE_EFFECTS.add(new PotionEffect(PotionEffectType.JUMP, 100, 0));
	}
	private final Stopwatch adrenalineTimer = Stopwatch.createStarted();
	private final Stopwatch smokebombTimer = Stopwatch.createStarted();

	public Assassin(Player player) {
		super(player);
		PERMS.get(player.getUniqueId()).setPermission(PERM, true);
	}

	public void adrenaline() {
		if (adrenalineTimer.elapsed(TimeUnit.MILLISECONDS) >= ADRENALINE_COOLDOWN) {
			if (player.getMaxHealth() / player.getHealth() >= 4.0D) {
				player.addPotionEffects(ADRENALINE_EFFECTS);
			}
		}
	}

	@Override
	public void cleanup() {
		PERMS.get(player.getUniqueId()).unsetPermission(PERM);
	}

	public void destealth() {
		SNEAKING.remove(player);
		for (Player target : Bukkit.getOnlinePlayers()) {
			target.showPlayer(player);
		}
	}

	public void headShot(EntityDamageByEntityEvent event) {
		LivingEntity target = (LivingEntity) event.getEntity();
		target.addPotionEffect(SMOKE_BOMB_EFFECT);
		event.setDamage(event.getDamage() * HEADSHOT_MULTIPLIER);
	}

	public void poison(LivingEntity target) {
		if (target.hasPotionEffect(PotionEffectType.POISON)) {
			for (PotionEffect effect : target.getActivePotionEffects()) {
				if (effect.getType().equals(PotionEffectType.POISON)) {
					target.removePotionEffect(PotionEffectType.POISON);
					int amplifier = Math.min(effect.getAmplifier() + 1, 127);
					target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, POISON_DURATION, amplifier));
				}
			}
		}
		else {
			target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, POISON_DURATION, 0));
		}
	}

	public void rip(LivingEntity target) {
		if (target.hasPotionEffect(PotionEffectType.POISON)) {
			for (PotionEffect effect : target.getActivePotionEffects()) {
				if (effect.getType().equals(PotionEffectType.POISON)) {
					target.removePotionEffect(PotionEffectType.POISON);
					target.damage((effect.getAmplifier() + 1) * RIP_DAMAGE, player);
					target.addPotionEffect(RIP_EFFECT);
				}
			}
		}
	}

	public void shadowStrike(EntityDamageByEntityEvent event) {
		if (SNEAKING.contains(player)) {
			LivingEntity target = (LivingEntity) event.getEntity();
			double damage = event.getDamage();
			target.addPotionEffects(SHADOW_STRIKE_EFFECTS);
			event.setDamage(damage * SHADOW_STRIKE_MULTIPLIER);
		}
	}

	public void smokeBomb() {
		if (smokebombTimer.elapsed(TimeUnit.MILLISECONDS) >= SMOKE_BOMB_COOLDOWN) {
			for (Entity entity : player.getNearbyEntities(5, 2, 5)) {
				if (entity instanceof LivingEntity) {
					((LivingEntity) entity).addPotionEffect(SMOKE_BOMB_EFFECT);
				}
			}
		}
	}

	public void stealth() {
		SNEAKING.add(player);
		for (Player target : Bukkit.getOnlinePlayers()) {
			target.hidePlayer(player);
		}
	}
}
