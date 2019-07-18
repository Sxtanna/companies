package com.sxtanna.mc.companies.spigot.watcher.impl;

import com.sxtanna.mc.companies.Companies;
import com.sxtanna.mc.companies.spigot.watcher.base.Watcher;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public final class JoinWatcher extends Watcher
{

	public JoinWatcher(final Companies companies)
	{
		super(companies);
	}


	@EventHandler
	public void onJoin(final PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();

		// cache name
		nameJoinProcess(player);

		// cache data
		dataJoinProcess(player);
	}


	private void nameJoinProcess(final Player player)
	{
		companies.nameDatabase().saveName(player.getUniqueId(), player.getName());
	}

	private void dataJoinProcess(final Player player)
	{
		companies.getOrLoadStaffer(player.getUniqueId(), staffer ->
		{
			// create staffer if they don't have one
			if (!staffer.isPresent())
			{
				companies.createStaffer(player.getUniqueId(), player.getName());
			}
		});
	}

}
