package com.sxtanna.mc.companies.conf;

import com.sxtanna.mc.companies.conf.type.MessageConfig;
import com.sxtanna.mc.companies.lang.Messages;
import com.sxtanna.mc.companies.lang.base.LangKey;
import com.sxtanna.mc.companies.util.Helper;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;

public final class MessageConfigImpl implements MessageConfig
{

	private final Plugin plugin;
	private final File confFile;


	private FileConfiguration confData;


	public MessageConfigImpl(final Plugin plugin)
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
			getPlugin().getLogger().log(Level.SEVERE, "Failed to read MessageConfig[" + getFile() + "]", ex);
			return;
		}
		catch (InvalidConfigurationException ex)
		{
			getPlugin().getLogger().log(Level.SEVERE, "Failed to load MessageConfig[" + getFile() + "]", ex);
			return;
		}

		this.confData = config;
	}

	@Override
	public void save()
	{
		getPlugin().getLogger().info("Saving default MessageConfig");

		if (confData == null)
		{
			confData = new YamlConfiguration();
		}

		for (final Messages key : Messages.values())
		{
			confData.set(key.getName().toLowerCase().replace('_', '.'), key.getDefaultValue());
		}

		Helper.ignoreException(() -> this.confData.save(Helper.ensureExists(getFile())));
	}


	@Override
	public File getFile()
	{
		return this.confFile;
	}

	@Override
	public String getName()
	{
		return "messages.yml";
	}


	@Override
	public Plugin getPlugin()
	{
		return this.plugin;
	}


	@Override
	public Optional<String> getCustomLangValue(final LangKey key)
	{
		return getValueAtPath(confData, key.getName().toLowerCase().replace('_', '.'));
	}

}
