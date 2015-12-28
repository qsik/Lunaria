package econ;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitWorker;

public class Main extends JavaPlugin implements Listener {
	public static Economy econ = null;
	Map<UUID, Allowance> allowances = new ConcurrentHashMap<UUID, Allowance>();

	@EventHandler
	public void login(PlayerLoginEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		if (allowances.containsKey(uuid)) {
			allowances.get(uuid).stopwatch.start();
			allowances.get(uuid).payable = true;
		}
		else {
			allowances.put(uuid, new Allowance(uuid));
		}
	}

	@EventHandler
	public void logoff(PlayerQuitEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		allowances.get(uuid).stopwatch.stop();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		switch (cmd.getName().toLowerCase()) {
		case "votepay":
			if (args.length == 1) {
				try {
					UUID uuid = UUIDFetcher.getUUIDOf(args[0]);
					OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
					EconomyResponse response = econ.depositPlayer(player, Allowance.CURRENT_HOURLY_RATE * Allowance.CURRENT_VOTE_RATE);
					if (player.isOnline()) {
						((Player) player).sendMessage("Thank you for voting! You've been given " + response.amount + " as a reward.");
					}
					Bukkit.getLogger().info("Paid " + player.getName() + " " + response.amount + " for voting");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		case "lecon":
			switch (args.length) {
			case 1:
				if (args[0].equalsIgnoreCase("payall")) {
					payPlayers();
				}
				break;
			case 2:
				switch (args[0].toLowerCase()) {
				case "pay":
					try {
						UUID uuid = UUIDFetcher.getUUIDOf(args[1]);
						OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
						if (args[0].equals("pay")) {
							econ.depositPlayer(player, allowances.get(uuid).calculatePay());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case "hourlyrate":
					Allowance.CURRENT_HOURLY_RATE = Double.parseDouble(args[1]);
					sender.sendMessage("Current Hourly Rate now " + Allowance.CURRENT_HOURLY_RATE);
					break;
				case "voterate":
					Allowance.CURRENT_VOTE_RATE = Double.parseDouble(args[1]);
					sender.sendMessage("Current Vote Rate now " + Allowance.CURRENT_VOTE_RATE);
					break;
				default:
					break;
				}
			default:
				sender.sendMessage("Current Hourly Rate: " + Allowance.CURRENT_HOURLY_RATE);
				sender.sendMessage("Current Vote Rate: " + Allowance.CURRENT_VOTE_RATE);
				break;
			}
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void onDisable() {
		boolean paid = false;
		Iterator<BukkitWorker> iterator = Bukkit.getScheduler().getActiveWorkers().iterator();
		while (iterator.hasNext()) {
			BukkitWorker worker = iterator.next();
			if (worker.getOwner().equals(this)) {
				paid = true;
			}
		}
		if (!paid) {
			payPlayers();
		}
		Bukkit.getScheduler().cancelTasks(this);
	}

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return;
		}
		econ = rsp.getProvider();
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
			@Override
			public void run() {
				payPlayers();
			}
		}, 72000, 72000);
	}

	private void payPlayers() {
		for (Allowance allowance : allowances.values()) {
			double pay = allowance.calculatePay();
			if (pay > 0.0D) {
				EconomyResponse response = econ.depositPlayer(allowance.player, pay);
				Bukkit.getLogger().info("Paid player " + allowance.player.getName() + " $" + response.amount);
				Bukkit.getLogger().info("Elapsed Time: " + allowance.stopwatch.elapsed(TimeUnit.SECONDS));
			}
		}
	}
}
