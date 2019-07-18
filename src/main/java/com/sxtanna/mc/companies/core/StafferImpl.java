package com.sxtanna.mc.companies.core;

import com.sxtanna.mc.companies.Companies;
import com.sxtanna.mc.companies.base.RequiresPlugin;
import com.sxtanna.mc.companies.util.Helper;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class StafferImpl implements Staffer, RequiresPlugin
{

	private transient final Companies companies;
	private transient final Set<Product> productCache = Helper.newSet();


	private UUID uuid;
	private String name;

	private UUID companyUUID;

	private final Set<UUID> product = Helper.newSet();


	private StafferImpl(final Companies companies, final UUID uuid, final String name)
	{
		this.companies = companies;

		this.uuid = uuid;
		this.name = name;
	}

	public StafferImpl(final Companies companies)
	{
		this(companies, Staffer.DEFAULT_UUID, Staffer.DEFAULT_NAME);
	}


	@Override
	public void load()
	{
		this.product.forEach(uuid -> companies.getOrLoadProduct(uuid, product -> product.ifPresent(productCache::add)));
	}

	@Override
	public void kill()
	{
		productCache.clear();
	}


	@Override
	public String getName()
	{
		if (name == null || name.isEmpty())
		{
			companies.nameDatabase().loadName(getUUID()).ifPresent(name -> this.name = name);
		}

		return name;
	}

	@Override
	public UUID getUUID()
	{
		return uuid;
	}


	@Override
	public Plugin getPlugin()
	{
		return companies.getPlugin();
	}


	@Override
	public Optional<UUID> getCompanyUUID()
	{
		return Optional.ofNullable(this.companyUUID);
	}


	@Override
	public Set<Product> products()
	{
		return productCache;
	}


	// implementation


	public void setUUID(final UUID uuid)
	{
		this.uuid = uuid;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public void setCompanyUUID(final UUID companyUUID)
	{
		this.companyUUID = companyUUID;
	}

}
