package com.sxtanna.mc.companies.spigot.command.base;

import com.sxtanna.mc.companies.Companies;
import com.sxtanna.mc.companies.base.Named;
import com.sxtanna.mc.companies.base.RequiresPlugin;
import com.sxtanna.mc.companies.base.State;
import com.sxtanna.mc.companies.util.Helper;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command implements CommandExecutor, TabCompleter, Named, State, RequiresPlugin
{

	private final String name;
	private final Companies companies;


	protected Command(final Companies companies, final String name)
	{
		this.name = name;
		this.companies = companies;
	}


	@Override
	public final String getName()
	{
		return name;
	}

	@Override
	public final Plugin getPlugin()
	{
		return companies.getPlugin();
	}


	@Override
	public void load()
	{
		final PluginCommand command = getServer().getPluginCommand(getName());
		if (command == null)
		{
			return; // probably log?
		}

		command.setExecutor(this);
		command.setTabCompleter(this);
	}

	@Override
	public void kill()
	{

	}


	@Override
	public final boolean onCommand(final CommandSender sender, final org.bukkit.command.Command command, final String label, final String[] args)
	{
		Context context = new Context(sender, label, Arrays.asList(args));

		evaluate(context);

		return true;
	}

	@Override
	public final List<String> onTabComplete(final CommandSender sender, final org.bukkit.command.Command command, final String alias, final String[] args)
	{
		Context context = new Context(sender, alias, Arrays.asList(args));

		List<String> output = Helper.newList();
		complete(context, output);

		return output;
	}


	public abstract void evaluate(Context context);

	public abstract void complete(Context context, List<String> output);

}
