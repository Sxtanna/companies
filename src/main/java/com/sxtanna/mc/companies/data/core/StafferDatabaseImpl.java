package com.sxtanna.mc.companies.data.core;

import com.sxtanna.mc.companies.Companies;
import com.sxtanna.mc.companies.core.Staffer;
import com.sxtanna.mc.companies.data.DatabaseType;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class StafferDatabaseImpl implements StafferDatabase
{

	private final Companies companies;

	StafferDatabaseImpl(final Companies companies)
	{
		this.companies = companies;
	}


	@Override
	public final String getName()
	{
		return "StafferDatabase[" + getUUID() + "]";
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


	private static final class LOCALStafferDatabaseImpl extends StafferDatabaseImpl
	{

		private LOCALStafferDatabaseImpl(final Companies companies)
		{
			super(companies);
		}


		@Override
		public void saveStaffer(final Staffer data)
		{

		}

		@Override
		public void loadStaffer(final UUID uuid, final Consumer<Optional<Staffer>> consumer)
		{

		}


		@Override
		public void bulkSaveStaffers(final Collection<Staffer> data)
		{

		}

		@Override
		public void bulkLoadStaffers(final Collection<UUID> uuids, final Consumer<Map<UUID, Optional<Staffer>>> consumer)
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

	private static final class MYSQLStafferDatabaseImpl extends StafferDatabaseImpl
	{

		private MYSQLStafferDatabaseImpl(final Companies companies)
		{
			super(companies);
		}


		@Override
		public void saveStaffer(final Staffer data)
		{

		}

		@Override
		public void loadStaffer(final UUID uuid, final Consumer<Optional<Staffer>> consumer)
		{

		}


		@Override
		public void bulkSaveStaffers(final Collection<Staffer> data)
		{

		}

		@Override
		public void bulkLoadStaffers(final Collection<UUID> uuids, final Consumer<Map<UUID, Optional<Staffer>>> consumer)
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

	/*private static final class REDISStafferDatabaseImpl extends StafferDatabaseImpl
	{

		private REDISStafferDatabaseImpl(final Companies companies)
		{
			super(companies);
		}


		@Override
		public void saveStaffer(final Staffer data)
		{

		}

		@Override
		public void loadStaffer(final UUID uuid, final Consumer<Optional<Staffer>> consumer)
		{

		}


		@Override
		public void bulkSaveStaffers(final Collection<Staffer> data)
		{

		}

		@Override
		public void bulkLoadStaffers(final Collection<UUID> uuids, final Consumer<Map<UUID, Optional<Staffer>>> consumer)
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


	public static StafferDatabase get(Companies companies, DatabaseType type)
	{
		StafferDatabase database;

		switch (type)
		{
			case FOLDER:
				database = new LOCALStafferDatabaseImpl(companies);
				break;
			case REMOTE_MYSQL:
				database = new MYSQLStafferDatabaseImpl(companies);
				break;
			/*case REMOTE_REDIS:
				database = new REDISStafferDatabaseImpl(companies);
				break;*/
			default:
				throw new IllegalArgumentException("Cannot produce a database for type: " + type);
		}

		return database;
	}

}
