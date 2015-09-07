package bdays;

import io.netty.util.internal.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.google.common.base.Stopwatch;

public class Fireworks implements Runnable {
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

	private static final Map<UUID, Stopwatch> COOLDOWNS = new ConcurrentHashMap<UUID, Stopwatch>();
	private static final long COOLDOWN = 1;
	private static final int COLOR_COUNT = EffectColor.values().length;
	private static final byte MAX_TICKS = 16;
	private static final Vector VELOCITY = new Vector(0, 1, 0);
	private Location dx;
	private World world;
	private byte ticksLived = 0;
	private int taskId;
	private final Birthday birthday;
	private final Plugin plugin = Main.plugin;

	public Fireworks(Player player, Birthday birthday) {
		this.birthday = birthday;
		boolean onCooldown = COOLDOWNS.containsKey(player.getUniqueId()) && COOLDOWNS.get(player.getUniqueId()).elapsed(TimeUnit.HOURS) <= COOLDOWN;
		if (onCooldown) {
			player.sendMessage("You must wait before partying again!");
		}
		else {
			COOLDOWNS.put(player.getUniqueId(), Stopwatch.createStarted());
			dx = player.getLocation();
			dx.setPitch(0);
			dx.setYaw(0);
			world = player.getWorld();
			taskId = Bukkit.getScheduler().runTaskTimer(plugin, this, 0, 1).getTaskId();
		}
	}

	@Override
	public void run() {
		if (!dx.getBlock().getType().equals(Material.AIR) || ticksLived == MAX_TICKS) {
			generateFirework();
			Bukkit.getScheduler().cancelTask(taskId);
		}
		else {
			world.playEffect(dx, Effect.FIREWORKS_SPARK, 0);
			dx.add(VELOCITY);
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
				Location location = firework.getLocation();
				firework.detonate();
				location.getWorld().dropItem(location, new ItemStack(Material.CAKE));
				switch (birthday) {
				case BAY:
					ItemStack bayBayle = new ItemStack(Material.HAY_BLOCK);
					ItemMeta itemMeta = bayBayle.getItemMeta();
					itemMeta.setDisplayName(ChatColor.RED + "" + ChatColor.ITALIC +  "Bay Bayle");
					List<String> lore = new ArrayList<String>();
					lore.add("Don't worry, it's bae!");
					itemMeta.setLore(lore);
					bayBayle.setItemMeta(itemMeta);
					world.dropItem(location, bayBayle);
					break;
				case PIGEON:
					Chicken chicken = (Chicken) world.spawnEntity(location, EntityType.CHICKEN);
					chicken.setCustomName("EVIL_PIGEON");
					chicken.setCustomNameVisible(true);
					break;
				case EMI:
					ItemStack emerald = new ItemStack(Material.EMERALD);
					ItemMeta meta = emerald.getItemMeta();
					meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.ITALIC +  "Emirrald");
					emerald.setItemMeta(meta);
					emerald.addUnsafeEnchantment(Enchantment.LUCK, 1);
					world.dropItem(location, emerald);
					break;
				default:
					break;
				}
			}
		}, 1);
	}
}
