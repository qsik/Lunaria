package rpg.heroes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import rpg.Main;

public abstract class Hero {
	public static final Map<UUID, Hero> LIST = new HashMap<UUID, Hero>();
	public static final Map<UUID, PermissionAttachment> PERMS = new HashMap<UUID, PermissionAttachment>();
	protected final Player player;

	public Hero(Player player) {
		this.player = player;
		LIST.put(player.getUniqueId(), this);
		if (!PERMS.containsKey(player.getUniqueId())) {
			PERMS.put(player.getUniqueId(), new PermissionAttachment(Main.plugin, player));
		}
	}

	public abstract void cleanup();
}
