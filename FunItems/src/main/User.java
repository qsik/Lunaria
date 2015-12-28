package main;

import jakro.Jakro;
import mams.Mams;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import sly.Sly;
import clock.Clock;

public abstract class User implements Listener {
	enum UserList {
		CLOCK (new Clock()),
		SLY (new Sly()),
		MAMS (new Mams()),
		JAKRO (new Jakro());

		public final User user;

		private UserList(User user) {
			this.user = user;
		}
	}

	public void initialize() {
		Bukkit.getPluginManager().registerEvents(this, FunItems.plugin);
	}
}
