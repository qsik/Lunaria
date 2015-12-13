package main;

import jakro.Jakro;
import mams.Mams;
import pigeon.Pigeon;
import sly.Sly;
import clock.Clock;

public enum Users {
	CLOCK (Clock.class),
	JAKRO (Jakro.class),
	MAMS (Mams.class),
	PIGEON (Pigeon.class),
	SLY (Sly.class);

	private final Class<?> user;

	private Users(Class<?> user) {
		this.user = user;
	}

	public void executeCommand() throws Exception {
		user.getMethod("command", null).invoke(null, null);
	}
}
