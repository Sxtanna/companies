package com.sxtanna.mc.companies.spigot.base;

import com.sxtanna.mc.companies.Companies;
import com.sxtanna.mc.companies.base.State;
import com.sxtanna.mc.companies.spigot.command.base.Command;
import com.sxtanna.mc.companies.spigot.command.impl.CommandCompanyAdmin;
import com.sxtanna.mc.companies.spigot.command.impl.CommandCompanyUsers;
import com.sxtanna.mc.companies.spigot.watcher.base.Watcher;
import com.sxtanna.mc.companies.spigot.watcher.impl.JoinWatcher;
import com.sxtanna.mc.companies.spigot.watcher.impl.TempWatcher;
import com.sxtanna.mc.companies.util.Helper;

import java.util.Set;

public final class Hooks implements State
{

	private final Set<Command> commands = Helper.newSet();
	private final Set<Watcher> watchers = Helper.newSet();

	public Hooks(final Companies companies)
	{
		// commands
		commands.add(new CommandCompanyUsers(companies));
		commands.add(new CommandCompanyAdmin(companies));

		// watchers
		watchers.add(new JoinWatcher(companies));
		watchers.add(new TempWatcher(companies));
	}


	@Override
	public void load()
	{
		// commands
		commands.forEach(Command::load);

		// watchers
		watchers.forEach(Watcher::load);
	}

	@Override
	public void kill()
	{
		// commands
		commands.forEach(Command::kill);
		commands.clear();

		// watchers
		watchers.forEach(Watcher::kill);
		watchers.clear();
	}

}
