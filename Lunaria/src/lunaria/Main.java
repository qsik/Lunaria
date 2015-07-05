package lunaria;


import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import runnables.Mail;
import fourth.Surprise;
import funitems.FunItem;
import funitems.FunListener;
import funitems.ItemGenerator;

public class Main extends JavaPlugin implements Listener {
	private static final String GAMERULE = "ProtectHorses";
	public static org.bukkit.plugin.Plugin plugin;
	//	private static final Surprise surprise = new Surprise();
	private final ConcurrentLinkedQueue<Mail> funItems = new ConcurrentLinkedQueue<Mail>();

	@EventHandler
	public void login(PlayerLoginEvent event) {
		//		Iterator<Mail> i = funItems.iterator();
		//		while (i.hasNext()) {
		//			Mail mail = i.next();
		//			if (mail.player.equals(event.getPlayer().getUniqueId())) {
		//				ItemStack funItem = ItemGenerator.generateFunItem(mail.funItem, true);
		//				Player player = event.getPlayer();
		//				player.getWorld().dropItemNaturally(player.getLocation(), funItem);
		//			}
		//			funItems.remove(mail);
		//		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("party")) {
			Surprise.party((Player) sender);
		}
		else if (cmd.getName().equalsIgnoreCase("cake")) {
			sender.sendMessage("Tis a lie!");
		}
		else if (cmd.getName().equalsIgnoreCase("usa")) {
			Surprise.usa((Player) sender);
			sender.sendMessage("Look Up!");
		}
		else if (cmd.getName().equalsIgnoreCase("cookout")) {
			Surprise.cookout((Player) sender);
		}
		else if (cmd.getName().equalsIgnoreCase("funitems")) {
			if (sender instanceof Player) {
				if (sender.hasPermission("funitems")) {
					Player player = (Player) sender;
					boolean breakable = args.length < 2 || args[1].equalsIgnoreCase("false");
					if (args.length == 0) {
						sender.sendMessage(FunItem.getList());
						return false;
					}
					switch (args[0].toUpperCase()) {
					//					case "MAIL":
					//						if (args.length >= 3) {
					//							UUID reciever;
					//							try {
					//								reciever = UUIDFetcher.getUUIDOf(args[1]);
					//								if (Bukkit.getOfflinePlayer(reciever).isOnline()) {
					//									Player rPlayer = Bukkit.getPlayer(reciever);
					//									FunItem f = FunItem.getConstant(args[2]);
					//									rPlayer.getWorld().dropItemNaturally(rPlayer.getLocation(), ItemGenerator.generateFunItem(f, true));
					//								}
					//								else {
					//									Mail mail = new Mail(reciever, FunItem.getConstant(args[2]));
					//									if (!funItems.contains(mail)) {
					//										funItems.add(mail);
					//									}
					//								}
					//							} catch (Exception e) {
					//								sender.sendMessage("Unable to find player or invalid command!");
					//							}
					//						}
					//						break;
					case "SLY":
						player.getWorld().dropItemNaturally(player.getLocation(), ItemGenerator.generateFunItem("Sly", breakable));
						player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.ARROW));
						sender.sendMessage("Have fun! :D");
						break;
					case "MAMZ":
						player.getWorld().dropItemNaturally(player.getLocation(), ItemGenerator.generateFunItem("Mamz", breakable));
						sender.sendMessage("DO NOT ABUSE");
						break;
					case "CLOCK":
						break;
					case "CONVERT":
						if (ItemGenerator.convert(player.getItemInHand())) {
							sender.sendMessage("Have fun :)");
						}
						else {
							sender.sendMessage("Not a Fun Item!");
						}
						break;
					case "LIST":
					default:
						sender.sendMessage(FunItem.getList());
					}
				}
			}
		}
		return false;
	}

	@Override
	public void onDisable() {
		HandlerList.unregisterAll();
	}

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new FunListener(), this);
		plugin = this;
	}

	@EventHandler
	public void stopTeleport(PlayerTeleportEvent event) {
		if (!event.getPlayer().hasPermission("bypasstp")) {
			if (event.getCause().equals(TeleportCause.SPECTATE)) {
				if (!event.getTo().getWorld().equals(event.getFrom().getWorld())) {
					event.setCancelled(true);
				}
			}
		}
	}

	private boolean checkRules(World world) {
		if (world.isGameRule(GAMERULE)) {
			if (world.getGameRuleValue(GAMERULE).equalsIgnoreCase("true")) {
				return true;
			}
		}
		return false;
	}

	private boolean playerDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			return !player.isOp();
		}
		if (event.getDamager() instanceof Projectile) {
			Projectile projectile = (Projectile) event.getDamager();
			if (projectile.getShooter() instanceof Player) {
				Player player = (Player) projectile.getShooter();
				return !player.isOp();
			}
		}
		return false;
	}

	@EventHandler
	private void preventDamage(EntityDamageByEntityEvent event) {
		if (checkRules(event.getEntity().getWorld())) {
			if (event.getEntity() instanceof Horse) {
				if (playerDamage(event)) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	private void preventRiding(PlayerInteractEntityEvent event) {
		if (checkRules(event.getPlayer().getWorld())) {
			if (event.getRightClicked() instanceof Horse) {
				Horse horse = (Horse) event.getRightClicked();
				if (horse.getOwner() != null) {
					if (horse.getOwner().getUniqueId().equals(event.getPlayer().getUniqueId()) == false) {
						event.getPlayer().sendMessage("This is not your horse!");
						event.setCancelled(!event.getPlayer().isOp());
					}
				}
			}
		}
	}
}
