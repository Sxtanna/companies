package com.sxtanna.mc.companies.data.util;

import com.sxtanna.mc.companies.Companies;
import com.sxtanna.mc.companies.base.Kills;
import com.sxtanna.mc.companies.core.Company;
import com.sxtanna.mc.companies.core.Product;
import com.sxtanna.mc.companies.core.Staffer;
import com.sxtanna.mc.companies.util.Helper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Cache implements Kills
{

	private final Companies companies;

	private final Map<UUID, Company> companyCache = Helper.newMap();
	private final Map<UUID, Product> productCache = Helper.newMap();
	private final Map<UUID, Staffer> stafferCache = Helper.newMap();


	public Cache(final Companies companies)
	{
		this.companies = companies;
	}


	@Override
	public void kill()
	{
		companyCache.clear();
		productCache.clear();
		stafferCache.clear();
	}


	public Collection<Company> companyCache()
	{
		return companyCache.values();
	}

	public Collection<Product> productCache()
	{
		return productCache.values();
	}

	public Collection<Staffer> stafferCache()
	{
		return stafferCache.values();
	}


	public void cacheCompany(Company data)
	{
		companyCache.put(data.getUUID(), data);
	}

	public void cacheProduct(Product data)
	{
		productCache.put(data.getUUID(), data);
	}

	public void cacheStaffer(Staffer data)
	{
		stafferCache.put(data.getUUID(), data);
	}


	public void getOrLoadCompany(UUID uuid, Consumer<Optional<Company>> consumer)
	{
		getOrLoad(uuid, companyCache::get, consumer, companies.dataDatabase()::loadCompany, companyCache::put);
	}

	public void getOrLoadProduct(UUID uuid, Consumer<Optional<Product>> consumer)
	{
		getOrLoad(uuid, productCache::get, consumer, companies.dataDatabase()::loadProduct, productCache::put);
	}

	public void getOrLoadStaffer(UUID uuid, Consumer<Optional<Staffer>> consumer)
	{
		getOrLoad(uuid, stafferCache::get, consumer, companies.dataDatabase()::loadStaffer, stafferCache::put);
	}


	private <T> void getOrLoad(UUID uuid, Function<UUID, T> cachedSupplier, Consumer<Optional<T>> valueConsumer, BiConsumer<UUID, Consumer<Optional<T>>> valueLoader, BiConsumer<UUID, T> valueSaver)
	{
		final T cached = cachedSupplier.apply(uuid);

		if (cached != null)
		{
			valueConsumer.accept(Optional.of(cached));
			return;
		}

		valueLoader.accept(uuid, value ->
		{
			// cache
			value.ifPresent(t -> valueSaver.accept(uuid, t));

			// return
			valueConsumer.accept(value);
		});
	}

}