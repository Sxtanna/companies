package com.sxtanna.mc.companies.data.core;

import com.sxtanna.mc.companies.Companies;
import com.sxtanna.mc.companies.core.Product;
import com.sxtanna.mc.companies.data.DatabaseType;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class ProductDatabaseImpl implements ProductDatabase
{

	private final Companies companies;

	ProductDatabaseImpl(final Companies companies)
	{
		this.companies = companies;
	}


	@Override
	public final String getName()
	{
		return "ProductDatabase[" + getUUID() + "]";
	}

	@Override
	public final Plugin getPlugin()
	{
		return companies.getPlugin();
	}

	@Override
	public final String toString()
	{
		return getName();
	}


	private static final class LOCALProductDatabaseImpl extends ProductDatabaseImpl
	{

		private LOCALProductDatabaseImpl(final Companies companies)
		{
			super(companies);
		}


		@Override
		public void saveProduct(final Product data)
		{

		}

		@Override
		public void loadProduct(final UUID uuid, final Consumer<Optional<Product>> consumer)
		{

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
		public DatabaseType getUUID()
		{
			return DatabaseType.FOLDER;
		}

	}

	private static final class MYSQLProductDatabaseImpl extends ProductDatabaseImpl
	{

		private MYSQLProductDatabaseImpl(final Companies companies)
		{
			super(companies);
		}


		@Override
		public void saveProduct(final Product data)
		{

		}

		@Override
		public void loadProduct(final UUID uuid, final Consumer<Optional<Product>> consumer)
		{

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
		public DatabaseType getUUID()
		{
			return DatabaseType.REMOTE_MYSQL;
		}

	}

	/*private static final class REDISProductDatabaseImpl extends ProductDatabaseImpl
	{

		private REDISProductDatabaseImpl(final Companies companies)
		{
			super(companies);
		}


		@Override
		public void saveProduct(final Product data)
		{

		}

		@Override
		public void loadProduct(final UUID uuid, final Consumer<Optional<Product>> consumer)
		{

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
		public DatabaseType getUUID()
		{
			return DatabaseType.REMOTE_REDIS;
		}

	}*/


	public static ProductDatabase get(Companies companies, DatabaseType type)
	{
		ProductDatabase database;

		switch (type)
		{
			case FOLDER:
				database = new LOCALProductDatabaseImpl(companies);
				break;
			case REMOTE_MYSQL:
				database = new MYSQLProductDatabaseImpl(companies);
				break;
			/*case REMOTE_REDIS:
				database = new REDISProductDatabaseImpl(companies);
				break;*/
			default:
				throw new IllegalArgumentException("Cannot produce a database for type: " + type);
		}

		return database;
	}

}
