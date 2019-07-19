package com.sxtanna.mc.companies.core;

import com.sxtanna.mc.companies.Companies;
import com.sxtanna.mc.companies.util.Helper;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.UUID;

public final class CompanyImpl implements Company
{

	private transient final Companies companies;
	private transient final Set<Staffer> stafferCache = Helper.newSet();
	private transient final Set<Product> productCache = Helper.newSet();


	private UUID uuid;
	private UUID owner;

	private String name;

	private Material icon;

	private final Set<UUID> staffer = Helper.newSet();
	private final Set<UUID> product = Helper.newSet();


	private CompanyImpl(final Companies companies, final UUID uuid, final UUID owner, final String name, final Material icon)
	{
		this.companies = companies;

		this.uuid = uuid;
		this.owner = owner;
		this.name = name;
		this.icon = icon;
	}

	public CompanyImpl(final Companies companies)
	{
		this(companies, Company.DEFAULT_UUID, Staffer.DEFAULT_UUID, Company.DEFAULT_NAME, companies.companyConfig().getDefaultCompanyIcon());
	}


	@Override
	public void load()
	{
		this.staffer.forEach(uuid -> companies.getOrLoadStaffer(uuid, staffer -> staffer.ifPresent(stafferCache::add)));
		this.product.forEach(uuid -> companies.getOrLoadProduct(uuid, product -> product.ifPresent(productCache::add)));
	}

	@Override
	public void kill()
	{
		stafferCache.clear();
		productCache.clear();
	}


	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public UUID getOwner()
	{
		return owner;
	}

	@Override
	public UUID getUUID()
	{
		return uuid;
	}

	@Override
	public Material getIcon()
	{
		return icon;
	}


	@Override
	public Plugin getPlugin()
	{
		return companies.getPlugin();
	}


	@Override
	public Set<Staffer> staffers()
	{
		return stafferCache;
	}

	@Override
	public Set<Product> products()
	{
		return productCache;
	}


	// implementation
	public void setName(final String name)
	{
		this.name = name;
	}

	public void setUUID(final UUID uuid)
	{
		this.uuid = uuid;
	}

	public void setOwner(final UUID owner)
	{
		this.owner = owner;
	}

}
