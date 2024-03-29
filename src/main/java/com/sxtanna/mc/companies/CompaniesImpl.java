package com.sxtanna.mc.companies;

import com.sxtanna.mc.companies.base.State;
import com.sxtanna.mc.companies.conf.type.CompanyConfig;
import com.sxtanna.mc.companies.conf.type.MessageConfig;
import com.sxtanna.mc.companies.conf.type.MySQLDBConfig;
//import com.sxtanna.mc.companies.conf.type.RedisDBConfig;
import com.sxtanna.mc.companies.core.Company;
import com.sxtanna.mc.companies.core.CompanyImpl;
import com.sxtanna.mc.companies.core.Product;
import com.sxtanna.mc.companies.core.ProductImpl;
import com.sxtanna.mc.companies.core.Staffer;
import com.sxtanna.mc.companies.core.StafferImpl;
import com.sxtanna.mc.companies.data.DataDatabaseImpl;
import com.sxtanna.mc.companies.data.NameDatabaseImpl;
import com.sxtanna.mc.companies.data.core.CompanyDatabase;
import com.sxtanna.mc.companies.data.core.CompanyDatabaseImpl;
import com.sxtanna.mc.companies.data.core.ProductDatabase;
import com.sxtanna.mc.companies.data.core.ProductDatabaseImpl;
import com.sxtanna.mc.companies.data.core.StafferDatabase;
import com.sxtanna.mc.companies.data.core.StafferDatabaseImpl;
import com.sxtanna.mc.companies.data.type.DataDatabase;
import com.sxtanna.mc.companies.data.type.NameDatabase;
import com.sxtanna.mc.companies.data.util.Cache;
import com.sxtanna.mc.companies.lang.Lang;
import com.sxtanna.mc.companies.lang.Messages;
import com.sxtanna.mc.companies.spigot.base.Hooks;
import com.sxtanna.mc.companies.util.Helper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;

public final class CompaniesImpl implements Companies
{

	private final Plugin plugin;


	private final CompanyConfig companyConfig;
	private final MessageConfig messageConfig;


	private final NameDatabase names;
	private final DataDatabase datas;


	private final Cache cache = new Cache(this);
	private final Hooks hooks = new Hooks(this);


	public CompaniesImpl(final Plugin plugin, final CompanyConfig companyConfig, final MessageConfig messageConfig)
	{
		this.plugin = plugin;

		this.companyConfig = companyConfig;
		this.messageConfig = messageConfig;


		this.names = new NameDatabaseImpl(plugin);

		final CompanyDatabase company = CompanyDatabaseImpl.get(this, companyConfig.getCompanyDatabaseType());
		final ProductDatabase product = ProductDatabaseImpl.get(this, companyConfig.getProductDatabaseType());
		final StafferDatabase staffer = StafferDatabaseImpl.get(this, companyConfig.getStafferDatabaseType());

		this.datas = new DataDatabaseImpl(plugin, company, product, staffer);
	}


	@Override
	public Plugin getPlugin()
	{
		return plugin;
	}


	@Override
	public void load()
	{
		for (final Messages key : Messages.values())
		{
			messageConfig().getCustomLangValue(key).ifPresent(val -> Lang.load(key, val));
		}

		State.attemptLoad(this.names, exception -> getPlugin().getLogger().log(Level.SEVERE, "Failed to load Database[" + this.names + "]", exception));
		State.attemptLoad(this.datas, exception -> getPlugin().getLogger().log(Level.SEVERE, "Failed to load Database[" + this.datas + "]", exception));

		hooks.load();

		loadStafferData();
		loadCompanyData();

		cache.stafferCache().forEach(Staffer::load);
		cache.companyCache().forEach(Company::load);
	}

	@Override
	public void kill()
	{
		Lang.kill();

		cache.stafferCache().forEach(Staffer::kill);
		cache.companyCache().forEach(Company::kill);

		State.attemptKill(this.names);
		State.attemptKill(this.datas);

		hooks.kill();
		cache.kill();
	}


	@Override
	public CompanyConfig companyConfig()
	{
		return companyConfig;
	}

	@Override
	public MessageConfig messageConfig()
	{
		return messageConfig;
	}


	@Override
	public MySQLDBConfig createOrLoadMySQLDBConfig()
	{
		return null;
	}

	/*@Override
	public RedisDBConfig createOrLoadRedisDBConfig()
	{
		return null;
	}*/


	@Override
	public NameDatabase nameDatabase()
	{
		return this.names;
	}

	@Override
	public DataDatabase dataDatabase()
	{
		return this.datas;
	}


	@Override
	public void getOrLoadCompany(final UUID uuid, final Consumer<Optional<Company>> consumer)
	{
		cache.getOrLoadCompany(uuid, consumer);
	}

	@Override
	public void getOrLoadProduct(final UUID uuid, final Consumer<Optional<Product>> consumer)
	{
		cache.getOrLoadProduct(uuid, consumer);
	}

	@Override
	public void getOrLoadStaffer(final UUID uuid, final Consumer<Optional<Staffer>> consumer)
	{
		cache.getOrLoadStaffer(uuid, consumer);
	}


	@Override
	public Company createCompany(final UUID owner, final String name)
	{
		final CompanyImpl company = new CompanyImpl(this);

		// init
		company.setUUID(UUID.randomUUID());

		company.setName(name);
		company.setOwner(owner);

		// cache
		cache.cacheCompany(company);

		return company;
	}

	@Override
	public Product createProduct(final UUID owner, final UUID companyUUID)
	{
		final ProductImpl product = new ProductImpl(this);

		// init
		product.setUUID(UUID.randomUUID());

		product.setStafferUUID(owner);
		product.setCompanyUUID(companyUUID);

		// cache
		cache.cacheProduct(product);

		return product;
	}

	@Override
	public Staffer createStaffer(final UUID owner, final String name)
	{
		final StafferImpl staffer = new StafferImpl(this);

		// init
		staffer.setUUID(owner);

		staffer.setName(name);

		// cache
		cache.cacheStaffer(staffer);

		return staffer;
	}


	private void loadStafferData()
	{
		dataDatabase().bulkLoadStaffers(Helper.map(getOnlinePlayers(), Player::getUniqueId), data ->
		{
			for (final Map.Entry<UUID, Optional<Staffer>> entry : data.entrySet())
			{
				if (entry.getValue().isPresent())
				{
					cache.cacheStaffer(entry.getValue().get());
				}
				else
				{
					getPlayer(entry.getKey()).ifPresent(player -> cache.cacheStaffer(createStaffer(player.getUniqueId(), player.getName())));
				}
			}
		});
	}

	private void loadCompanyData()
	{
		dataDatabase().bulkLoadCompanies(data -> data.forEach(cache::cacheCompany));
	}

}
