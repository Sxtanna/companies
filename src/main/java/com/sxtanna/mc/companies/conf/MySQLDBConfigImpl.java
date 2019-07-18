package com.sxtanna.mc.companies.conf;

import com.sxtanna.mc.companies.conf.type.MySQLDBConfig;
import com.sxtanna.mc.companies.util.Helper;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class MySQLDBConfigImpl implements MySQLDBConfig
{

	private final Plugin plugin;
	private final File confFile;

	private FileConfiguration confData;


	public MySQLDBConfigImpl(final Plugin plugin)
	{
		this.plugin = plugin;
		this.confFile = new File(plugin.getDataFolder(), getName());
	}


	@Override
	public void load()
	{
		if (!getFile().exists())
		{
			save();
			return;
		}

		YamlConfiguration config = new YamlConfiguration();

		try
		{
			config.load(getFile());
		}
		catch (IOException ex)
		{
			getPlugin().getLogger().log(Level.SEVERE, "Failed to read MySQLDBConfig[" + getFile() + "]", ex);
			return;
		}
		catch (InvalidConfigurationException ex)
		{
			getPlugin().getLogger().log(Level.SEVERE, "Failed to load MySQLDBConfig[" + getFile() + "]", ex);
			return;
		}

		this.confData = config;
	}

	@Override
	public void save()
	{
		getPlugin().getLogger().info("Saving default MySQLDBConfig");

		this.confData = Paths.DEFAULTS;

		Helper.ignoreException(() -> this.confData.save(Helper.ensureExists(getFile())));
	}


	@Override
	public File getFile()
	{
		return confFile;
	}

	@Override
	public String getName()
	{
		return "mysql_config.yml";
	}

	@Override
	public Plugin getPlugin()
	{
		return plugin;
	}


	@Override
	public String getHost()
	{
		return getValueAtPath(confData, Paths.DATABASE_HOST).orElse(Paths.DEFAULTS.getString(Paths.DATABASE_HOST));
	}

	@Override
	public String getPort()
	{
		return getValueAtPath(confData, Paths.DATABASE_PORT).orElse(Paths.DEFAULTS.getString(Paths.DATABASE_PORT));
	}

	@Override
	public String getDatabase()
	{
		return getValueAtPath(confData, Paths.DATABASE_DATABASE).orElse(Paths.DEFAULTS.getString(Paths.DATABASE_DATABASE));
	}

	@Override
	public String getUsername()
	{
		return getValueAtPath(confData, Paths.DATABASE_USERNAME).orElse(Paths.DEFAULTS.getString(Paths.DATABASE_USERNAME));
	}

	@Override
	public String getPassword()
	{
		return getValueAtPath(confData, Paths.DATABASE_PASSWORD).orElse(Paths.DEFAULTS.getString(Paths.DATABASE_PASSWORD));
	}


	private interface Paths
	{

		String DATABASE_HOST = "database.host";
		String DATABASE_PORT = "database.port";

		String DATABASE_DATABASE = "database.database";
		String DATABASE_USERNAME = "database.username";
		String DATABASE_PASSWORD = "database.password";


		FileConfiguration DEFAULTS = new YamlConfiguration() {

			{
				set(DATABASE_HOST, "localhost");
				set(DATABASE_PORT, "3306");

				set(DATABASE_DATABASE, "companies");
				set(DATABASE_USERNAME, "username");
				set(DATABASE_PASSWORD, "password");
			}

		};

	}

}
