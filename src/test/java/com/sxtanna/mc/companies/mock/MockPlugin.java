package com.sxtanna.mc.companies.mock;

import com.sxtanna.mc.companies.Companies;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public final class MockPlugin extends JavaPlugin
{

	public MockPlugin()
	{
		super();
	}

	public MockPlugin(final JavaPluginLoader loader, final PluginDescriptionFile description, final File dataFolder, final File file)
	{
		super(loader, description, dataFolder, file);
	}


	public Companies createMockCompanies()
	{
		return new MockCompanies(this, new MockCompanyConfig(this));
	}

}
