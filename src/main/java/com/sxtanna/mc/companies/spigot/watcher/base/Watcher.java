package com.sxtanna.mc.companies.spigot.watcher.base;

import com.sxtanna.mc.companies.Companies;
import com.sxtanna.mc.companies.base.RequiresPlugin;
import com.sxtanna.mc.companies.base.State;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class Watcher implements Listener, State, RequiresPlugin
{

	protected final Companies companies;

	public Watcher(final Companies companies)
	{
		this.companies = companies;
	}


	@Override
	public final Plugin getPlugin()
	{
		return companies.getPlugin();
	}

	@Override
	public final void load()
	{
		getServer().getPluginManager().registerEvents(this, getPlugin());
	}

	@Override
	public final void kill()
	{
		HandlerList.unregisterAll(this);
	}

}
