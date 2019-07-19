package com.sxtanna.mc.companies.spigot.watcher.impl;

import com.sxtanna.mc.companies.Companies;
import com.sxtanna.mc.companies.lang.Lang;
import com.sxtanna.mc.companies.lang.Messages;
import com.sxtanna.mc.companies.spigot.watcher.base.Watcher;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class TempWatcher extends Watcher
{

	public TempWatcher(final Companies companies)
	{
		super(companies);
	}


	@EventHandler
	public void onJoin(final PlayerJoinEvent event)
	{
		event.setJoinMessage(Lang.make(Messages.PLAYER_JOIN_SERVER, "player_name", event.getPlayer().getName()));
	}

	@EventHandler
	public void onQuit(final PlayerQuitEvent event)
	{
		event.setQuitMessage(Lang.make(Messages.PLAYER_QUIT_SERVER, "player_name", event.getPlayer().getName()));
	}

}
