package rpg.heroes;

import java.lang.reflect.Constructor;

import org.bukkit.entity.Player;

import rpg.heroes.assassin.Assassin;
import rpg.heroes.berserker.Berserker;
import rpg.heroes.shaman.Shaman;

public enum HeroType {
	ASSASSIN (Assassin.class),
	SHAMAN (Shaman.class),
	BERSERKER (Berserker.class);

	private final Constructor<? extends Hero> ctor;


	private HeroType(final Class<? extends Hero> classType) {
		try {
			this.ctor = classType.getConstructor(Player.class);
		} catch (NoSuchMethodException e) {
			throw new AssertionError();
		}
	}

	public Hero getHero(Player player) {
		try {
			return ctor.newInstance(player);
		} catch (Exception e) {
			throw new AssertionError();
		}
	}

	public static HeroType getHeroType(String classType) {
		for (HeroType heroType : HeroType.values()) {
			if (heroType.name().equalsIgnoreCase(classType)) {
				return heroType;
			}
		}
		return null;
	}
}