package econ;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.google.common.base.Stopwatch;

public class Allowance {
	public static final double BASE_HOURLY_RATE = 360.0D;
	public static final double BASE_VOTE_RATE = 1.0D;
	public static double CURRENT_HOURLY_RATE = BASE_HOURLY_RATE;
	public static double CURRENT_VOTE_RATE = BASE_VOTE_RATE;
	public final OfflinePlayer player;
	public final Stopwatch stopwatch = Stopwatch.createStarted();
	public boolean payable = true;

	public Allowance(UUID uuid) {
		player = Bukkit.getOfflinePlayer(uuid);
	}

	public double calculatePay() {
		double elapsedTime = stopwatch.elapsed(TimeUnit.SECONDS) / 3600.0D;
		double wage = elapsedTime > 1? CURRENT_HOURLY_RATE + CURRENT_HOURLY_RATE * Math.log(elapsedTime) : elapsedTime * CURRENT_HOURLY_RATE;
		double pay = payable? wage : 0;
		if (!player.isOnline()) {
			payable = false;
		}
		return pay;
	}
}
