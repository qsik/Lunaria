package util;

import io.netty.util.internal.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.meta.FireworkMeta;

import util.CustomFirework.EffectColor;

public class FireworkData {
	public static final int ALL_COLORS = EffectColor.values().length;
	public static final int MATCH_COLORS = -1;
	private final ThreadLocalRandom random = ThreadLocalRandom.current();
	private final List<Color> colors = new ArrayList<Color>();
	private final List<Color> fades = new ArrayList<Color>();
	private FireworkEffect.Type effectType;
	private boolean flicker = random.nextBoolean();
	private boolean trail = random.nextBoolean();

	public FireworkData() {
		setRandomColors(ALL_COLORS);
		setRandomFades(MATCH_COLORS, false);
		setRandomEffectType();
	}

	public FireworkMeta buildMeta(FireworkMeta meta) {
		FireworkEffect.Builder effects = FireworkEffect.builder();
		effects.flicker(flicker).trail(trail);
		effects.with(effectType);
		effects.withColor(colors);
		effects.withFade(fades);
		meta.addEffects(effects.build());
		return meta;
	}

	public FireworkData setColors(EffectColor... ec) {
		colors.clear();
		for (EffectColor c : ec) {
			colors.add(c.color);
		}
		return this;
	}

	public FireworkData setEffectType(FireworkEffect.Type effectType) {
		this.effectType = effectType;
		return this;
	}

	public FireworkData setFades(EffectColor... ec) {
		fades.clear();
		for (EffectColor c : ec) {
			fades.add(c.color);
		}
		return this;
	}

	public FireworkData setFlicker(boolean flicker) {
		this.flicker = flicker;
		return this;
	}

	public FireworkData setRandomColors(int maxColors) {
		colors.clear();
		for (int i = 0; i < maxColors; i++) {
			colors.add(EffectColor.values()[random.nextInt(ALL_COLORS)].color);
		}
		return this;
	}

	public FireworkData setRandomEffectType() {
		effectType = FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().length)];
		return this;
	}

	public FireworkData setRandomFades(int maxFades, boolean limitedToColors) {
		fades.clear();
		for (int i = 0; i < (maxFades == MATCH_COLORS? colors.size() : ALL_COLORS); i++) {
			Color color = colors.get(i);
			while (colors.get(i).equals(color)) {
				if (limitedToColors) {
					color = colors.get(random.nextInt(colors.size()));
				}
				else {
					color = EffectColor.values()[random.nextInt(ALL_COLORS)].color;
				}
			}
			fades.add(color);
		}
		return this;
	}

	public FireworkData setTrail(boolean trail) {
		this.trail = trail;
		return this;
	}
}
