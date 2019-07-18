package com.sxtanna.mc.companies.mock;

import com.sxtanna.mc.companies.conf.type.CompanyConfig;
import com.sxtanna.mc.companies.data.DatabaseType;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.io.File;

public final class MockCompanyConfig implements CompanyConfig
{

	private final Plugin plugin;

	MockCompanyConfig(final Plugin plugin)
	{
		this.plugin = plugin;
	}


	@Override
	public File getFile()
	{
		return new File(plugin.getDataFolder(), getName());
	}


	@Override
	public void load()
	{

	}

	@Override
	public void save()
	{

	}


	@Override
	public String getName()
	{
		return "MockCompanyConfig";
	}

	@Override
	public Plugin getPlugin()
	{
		return plugin;
	}


	@Override
	public Material getDefaultCompanyIcon()
	{
		return Material.DIORITE;
	}

	@Override
	public DatabaseType getCompanyDatabaseType()
	{
		return DatabaseType.FOLDER;
	}

	@Override
	public DatabaseType getProductDatabaseType()
	{
		return DatabaseType.FOLDER;
	}

	@Override
	public DatabaseType getStafferDatabaseType()
	{
		return DatabaseType.FOLDER;
	}

}
