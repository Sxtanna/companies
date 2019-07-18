package com.sxtanna.mc.companies.conf;

import com.sxtanna.mc.companies.conf.type.CompanyConfig;
import com.sxtanna.mc.companies.data.DatabaseType;
import com.sxtanna.mc.companies.util.Helper;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

@SuppressWarnings("Duplicates")
public final class CompanyConfigImpl implements CompanyConfig
{

	private final Plugin plugin;
	private final File confFile;


	private FileConfiguration confData;


	public CompanyConfigImpl(final Plugin plugin)
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
			getPlugin().getLogger().log(Level.SEVERE, "Failed to read CompanyConfig[" + getFile() + "]", ex);
			return;
		}
		catch (InvalidConfigurationException ex)
		{
			getPlugin().getLogger().log(Level.SEVERE, "Failed to load CompanyConfig[" + getFile() + "]", ex);
			return;
		}

		this.confData = config;
	}

	@Override
	public void save()
	{
		getPlugin().getLogger().info("Saving default CompanyConfig");

		this.confData = Paths.DEFAULTS;

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
		return "config.yml";
	}


	@Override
	public Plugin getPlugin()
	{
		return this.plugin;
	}


	@Override
	public Material getDefaultCompanyIcon()
	{
		return getValueAtPath(confData, Paths.DEFAULTS_COMPANY_ICON).map(Material::getMaterial).orElse(Material.DIORITE);
	}


	@Override
	public DatabaseType getCompanyDatabaseType()
	{
		return getValueAtPath(confData, Paths.DATABASE_TYPE_COMPANY).flatMap(DatabaseType::byName).orElse(DatabaseType.FOLDER);
	}

	@Override
	public DatabaseType getProductDatabaseType()
	{
		return getValueAtPath(confData, Paths.DATABASE_TYPE_PRODUCT).flatMap(DatabaseType::byName).orElse(DatabaseType.FOLDER);
	}

	@Override
	public DatabaseType getStafferDatabaseType()
	{
		return getValueAtPath(confData, Paths.DATABASE_TYPE_STAFFER).flatMap(DatabaseType::byName).orElse(DatabaseType.FOLDER);
	}



	private interface Paths
	{

		String DATABASE_TYPE_COMPANY = "database.company.type";
		String DATABASE_TYPE_PRODUCT = "database.product.type";
		String DATABASE_TYPE_STAFFER = "database.staffer.type";


		String DEFAULTS_COMPANY_ICON = "defaults.company.icon";


		FileConfiguration DEFAULTS = new YamlConfiguration() {

			{
				set(DATABASE_TYPE_COMPANY, DatabaseType.FOLDER.toString());
				set(DATABASE_TYPE_PRODUCT, DatabaseType.FOLDER.toString());
				set(DATABASE_TYPE_STAFFER, DatabaseType.FOLDER.toString());


				set(DEFAULTS_COMPANY_ICON, Material.DIORITE.toString());
			}

		};

	}

}