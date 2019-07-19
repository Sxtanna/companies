package com.sxtanna.mc.companies.mock;

import be.seeseemelk.mockbukkit.UnimplementedOperationException;
import com.sxtanna.mc.companies.Companies;
import com.sxtanna.mc.companies.conf.type.CompanyConfig;
import com.sxtanna.mc.companies.conf.type.MessageConfig;
import com.sxtanna.mc.companies.conf.type.MySQLDBConfig;
import com.sxtanna.mc.companies.core.Company;
import com.sxtanna.mc.companies.core.CompanyImpl;
import com.sxtanna.mc.companies.core.Product;
import com.sxtanna.mc.companies.core.Staffer;
import com.sxtanna.mc.companies.core.StafferImpl;
import com.sxtanna.mc.companies.data.type.DataDatabase;
import com.sxtanna.mc.companies.data.type.NameDatabase;
import com.sxtanna.mc.companies.util.Helper;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public final class MockCompanies implements Companies
{

	private final Plugin plugin;

	private final CompanyConfig companyConfig;
	private final MessageConfig messageConfig;

	MockCompanies(final Plugin plugin, final CompanyConfig companyConfig, final MessageConfig messageConfig)
	{
		this.plugin = plugin;

		this.companyConfig = companyConfig;
		this.messageConfig = messageConfig;
	}


	@Override
	public Plugin getPlugin()
	{
		return plugin;
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
	public void load()
	{

	}

	@Override
	public void kill()
	{

	}


	@Override
	public NameDatabase nameDatabase()
	{
		throw new UnimplementedOperationException("NameDatabase not available");
	}

	@Override
	public DataDatabase dataDatabase()
	{
		throw new UnimplementedOperationException("DataDatabase not available");
	}


	@Override
	public void getOrLoadCompany(final UUID uuid, final Consumer<Optional<Company>> consumer)
	{
		throw new UnimplementedOperationException("getOrLoadCompany not available");
	}

	@Override
	public void getOrLoadProduct(final UUID uuid, final Consumer<Optional<Product>> consumer)
	{
		throw new UnimplementedOperationException("getOrLoadProduct not available");
	}

	@Override
	public void getOrLoadStaffer(final UUID uuid, final Consumer<Optional<Staffer>> consumer)
	{
		throw new UnimplementedOperationException("getOrLoadStaffer not available");
	}


	@Override
	public Company createCompany(final UUID owner, final String name)
	{
		return Helper.apply(new CompanyImpl(this), company ->
		{
			company.setUUID(UUID.randomUUID());
			company.setOwner(owner);
			company.setName(name);
		});
	}

	@Override
	public Product createProduct(final UUID owner, final UUID companyUUID)
	{
		throw new UnimplementedOperationException("createProduct not available");
	}

	@Override
	public Staffer createStaffer(final UUID owner, final String name)
	{
		return Helper.apply(new StafferImpl(this), staffer ->
		{
			staffer.setUUID(owner);
			staffer.setName(name);
		});

		//throw new UnimplementedOperationException("createStaffer not available");
	}

	@Override
	public MySQLDBConfig createOrLoadMySQLDBConfig()
	{
		throw new UnimplementedOperationException("createProduct not available");
	}

}
