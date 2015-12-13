package runnables;

import java.util.UUID;

import funitems.FunItem;

public class Mail {
	public final UUID player;
	public final FunItem funItem;

	public Mail(UUID player, FunItem funItem) {
		this.player = player;
		this.funItem = funItem;
	}
}
