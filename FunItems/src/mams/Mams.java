package mams;

import main.User;

public class Mams extends User {
	public static final String UUID = "099ab4272eba43ee98f009818ac5433d";
	public final String item = "Mamz's MLG No-Scoper Kitty Cannon";

	//	@EventHandler
	//	public void kittyCannon(PlayerInteractEvent event) {
	//		switch (event.getAction()) {
	//		case RIGHT_CLICK_AIR:
	//		case RIGHT_CLICK_BLOCK:
	//			if (event.hasItem()) {
	//				if (FunItem.getFunItem(event.getItem()).alias.equals("Mamz")) {
	//					Location location = event.getPlayer().getLocation();
	//					Vector velocity = location.getDirection().multiply(3);
	//					Ocelot cat = (Ocelot) location.getWorld().spawnEntity(location, EntityType.OCELOT);
	//					cat.setBaby();
	//					ThreadLocalRandom r = ThreadLocalRandom.current();
	//					int i = r.nextInt(Ocelot.Type.values().length);
	//					cat.setCatType(Ocelot.Type.values()[i]);
	//					cat.setVelocity(velocity);
	//					new KittyCannon(cat);
	//				}
	//			}
	//			break;
	//		default:
	//			break;
	//		}
	//	}
}
