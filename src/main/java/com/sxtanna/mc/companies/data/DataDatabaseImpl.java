package com.sxtanna.mc.companies.data;

import com.sxtanna.mc.companies.core.Company;
import com.sxtanna.mc.companies.core.Product;
import com.sxtanna.mc.companies.core.Staffer;
import com.sxtanna.mc.companies.data.base.DatabaseType;
import com.sxtanna.mc.companies.data.core.CompanyDatabase;
import com.sxtanna.mc.companies.data.core.ProductDatabase;
import com.sxtanna.mc.companies.data.core.StafferDatabase;
import com.sxtanna.mc.companies.data.type.DataDatabase;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public final class DataDatabaseImpl implements DataDatabase
{

	private final Plugin plugin;

	private final CompanyDatabase companyDatabase;
	private final ProductDatabase productDatabase;
	private final StafferDatabase stafferDatabase;


	public DataDatabaseImpl(final Plugin plugin, final CompanyDatabase companyDatabase, final ProductDatabase productDatabase, final StafferDatabase stafferDatabase)
	{
		this.plugin = plugin;

		this.companyDatabase = companyDatabase;
		this.productDatabase = productDatabase;
		this.stafferDatabase = stafferDatabase;
	}


	@Override
	public String getName()
	{
		return "DataDatabase[" + companyDatabase + ", " + productDatabase + ", " + stafferDatabase + "]";
	}

	@Override
	public DatabaseType getUUID()
	{
		return DatabaseType.MAPPED;
	}


	@Override
	public Plugin getPlugin()
	{
		return this.plugin;
	}


	@Override
	public void load()
	{
		companyDatabase.load();
		productDatabase.load();
		stafferDatabase.load();
	}

	@Override
	public void kill()
	{
		companyDatabase.kill();
		productDatabase.kill();
		stafferDatabase.kill();
	}


	// CompanyDatabase
	@Override
	public void saveCompany(final Company data)
	{
		companyDatabase.saveCompany(data);
	}

	@Override
	public void loadCompany(final UUID uuid, final Consumer<Optional<Company>> consumer)
	{
		companyDatabase.loadCompany(uuid, consumer);
	}

	@Override
	public void bulkLoadCompanies(final Consumer<Collection<Company>> consumer)
	{
		companyDatabase.bulkLoadCompanies(consumer);
	}


	// ProductDatabase
	@Override
	public void saveProduct(final Product data)
	{
		productDatabase.saveProduct(data);
	}

	@Override
	public void loadProduct(final UUID uuid, final Consumer<Optional<Product>> consumer)
	{
		productDatabase.loadProduct(uuid, consumer);
	}


	// StafferDatabase
	@Override
	public void saveStaffer(final Staffer data)
	{
		stafferDatabase.saveStaffer(data);
	}

	@Override
	public void loadStaffer(final UUID uuid, final Consumer<Optional<Staffer>> consumer)
	{
		stafferDatabase.loadStaffer(uuid, consumer);
	}


	@Override
	public void bulkSaveStaffers(final Collection<Staffer> data)
	{
		stafferDatabase.bulkSaveStaffers(data);
	}

	@Override
	public void bulkLoadStaffers(final Collection<UUID> uuids, final Consumer<Map<UUID, Optional<Staffer>>> consumer)
	{
		stafferDatabase.bulkLoadStaffers(uuids, consumer);
	}

}
