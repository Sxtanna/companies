package com.sxtanna.mc.companies.data;

import com.sxtanna.mc.companies.data.base.DatabaseType;
import com.sxtanna.mc.companies.data.type.NameDatabase;
import com.sxtanna.mc.companies.util.Format;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

public final class NameDatabaseImpl implements NameDatabase
{

	private final Plugin plugin;
	private final File rootFile;


	public NameDatabaseImpl(final Plugin plugin)
	{
		this.plugin = plugin;
		this.rootFile = new File(plugin.getDataFolder(), "names");
	}


	@Override
	public String getName()
	{
		return "NameDatabase[" + getUUID() + "]";
	}

	@Override
	public DatabaseType getUUID()
	{
		return DatabaseType.FOLDER;
	}

	@Override
	public Plugin getPlugin()
	{
		return plugin;
	}


	@Override
	public void load()
	{
		if (!this.rootFile.exists() && !this.rootFile.mkdirs())
		{
			throw new IllegalStateException("Unable to create " + getName() + " Database folder.");
		}

		getPlugin().getLogger().log(Level.INFO, "Root is @" + rootFile);
	}

	@Override
	public void kill()
	{

	}


	@Override
	public void saveName(final UUID uuid, final String name)
	{
		if (!this.rootFile.exists())
		{
			databaseNotReady(Format.function("SaveName", "uuid", uuid, "name", name), "Root Folder doesn't exist");
		}

		try (FileWriter writer = new FileWriter(new File(this.rootFile, uuid.toString())))
		{
			writer.write(name);
		}
		catch (IOException ex)
		{
			getPlugin().getLogger().log(Level.SEVERE, "Failed to execute " + Format.function("SaveName", "uuid", uuid, "name", name), ex);
		}

	}

	@Override
	public Optional<String> loadName(final UUID uuid)
	{
		if (!this.rootFile.exists())
		{
			databaseNotReady(Format.function("LoadName", "uuid", uuid), "Root Folder doesn't exist");
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(new File(this.rootFile, uuid.toString()))))
		{
			return Optional.ofNullable(reader.readLine());
		}
		catch (IOException ex)
		{
			getPlugin().getLogger().log(Level.SEVERE, "Failed to execute " + Format.function("LoadName", "uuid", uuid), ex);
		}

		return Optional.empty();
	}

}
