package com.sxtanna.mc.companies.spigot.command.base;

import org.bukkit.command.CommandSender;

import java.util.List;

public final class Context
{

	private final CommandSender sender;

	private final String alias;
	private final List<String> input;


	Context(final CommandSender sender, final String alias, final List<String> input)
	{
		this.sender = sender;
		this.alias = alias;
		this.input = input;
	}


	public CommandSender getSender()
	{
		return sender;
	}

	public String getAlias()
	{
		return alias;
	}

	public List<String> getInput()
	{
		return input;
	}

}
