package com.sxtanna.mc.companies.data.core;

import com.sxtanna.mc.companies.Companies;
import com.sxtanna.mc.companies.conf.type.MySQLDBConfig;
//import com.sxtanna.mc.companies.conf.type.RedisDBConfig;
import com.sxtanna.mc.companies.core.Company;
import com.sxtanna.mc.companies.core.CompanyImpl;
import com.sxtanna.mc.companies.data.base.DatabaseType;
import com.sxtanna.mc.companies.util.Helper;
import com.sxtanna.mc.companies.util.SQLite;
import com.sxtanna.mc.companies.util.base.MySQLProvider;
//import com.sxtanna.mc.companies.util.base.RedisProvider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.Plugin;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class CompanyDatabaseImpl implements CompanyDatabase
{

	protected final Companies companies;

	CompanyDatabaseImpl(final Companies companies)
	{
		this.companies = companies;
	}


	@Override
	public final String getName()
	{
		return "CompanyDatabase[" + getUUID() + "]";
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


	private static final class LOCALCompanyDatabaseImpl extends CompanyDatabaseImpl
	{

		private final SQLite database;

		private LOCALCompanyDatabaseImpl(final Companies companies)
		{
			super(companies);

			this.database = new SQLite(companies, "company-db");
		}


		@Override
		public void load()
		{
			database.load();

			database.push(SQLConstants.CREATE_STATEMENT, statement ->
			{
			});
		}

		@Override
		public void kill()
		{
			database.kill();
		}


		@Override
		public void saveCompany(final Company data)
		{
			database.push(SQLConstants.INSERT_STATEMENT, statement ->
			{
				statement.setObject(1, data.getUUID());
				statement.setObject(2, data.getName());
			});
		}

		@Override
		public void loadCompany(final UUID uuid, final Consumer<Optional<Company>> consumer)
		{
			database.pull(SQLConstants.SELECT_ONE_STATEMENT,
						  statement ->
						  {
							  statement.setObject(1, uuid);
						  },
						  resultSet ->
						  {
							  consumer.accept(SQLConstants.recreateCompany(resultSet, companies));
						  });
		}

		@Override
		public void bulkLoadCompanies(final Consumer<Collection<Company>> consumer)
		{
			database.pull(SQLConstants.SELECT_ALL_STATEMENT,
						  statement ->
						  {
						  },
						  resultSet ->
						  {
							  consumer.accept(SQLConstants.recreateCompanies(resultSet, companies));
						  });
		}


		@Override
		public DatabaseType getUUID()
		{
			return DatabaseType.FOLDER;
		}

	}

	private static final class MYSQLCompanyDatabaseImpl extends CompanyDatabaseImpl implements MySQLProvider
	{

		private final HikariDataSource database;

		private MYSQLCompanyDatabaseImpl(final Companies companies)
		{
			super(companies);

			final HikariConfig config = createConfig();
			final MySQLDBConfig mysql = companies.createOrLoadMySQLDBConfig();

			config.setDriverClassName("org.mariadb.jdbc.Driver");
			config.setJdbcUrl("jdbc:mariadb://" + mysql.getHost() + ":" + Helper.toInt(mysql.getPort(), 3306) + "/" + mysql.getDatabase() + "?useSSL=false");

			config.setUsername(mysql.getUsername());
			config.setPassword(mysql.getPassword());


			this.database = new HikariDataSource(config);
		}


		@Override
		public void load()
		{

		}

		@Override
		public void kill()
		{
			database.close();
		}


		@Override
		public void saveCompany(final Company data)
		{
			push(SQLConstants.INSERT_STATEMENT, statement ->
			{
				statement.setObject(1, data.getUUID());
				statement.setObject(2, data.getName());
			});
		}

		@Override
		public void loadCompany(final UUID uuid, final Consumer<Optional<Company>> consumer)
		{
			pull(SQLConstants.SELECT_ONE_STATEMENT,
				 statement ->
				 {
					 statement.setObject(1, uuid);
				 },
				 resultSet ->
				 {
					 consumer.accept(SQLConstants.recreateCompany(resultSet, companies));
				 });
		}

		@Override
		public void bulkLoadCompanies(final Consumer<Collection<Company>> consumer)
		{
			pull(SQLConstants.SELECT_ALL_STATEMENT,
				 statement ->
				 {
				 },
				 resultSet ->
				 {
					 consumer.accept(SQLConstants.recreateCompanies(resultSet, companies));
				 });
		}


		@Override
		public DatabaseType getUUID()
		{
			return DatabaseType.REMOTE_MYSQL;
		}


		@Override
		public boolean autoCloseResource()
		{
			return true;
		}

		@Override
		public Optional<Connection> getConnection()
		{
			return Helper.ignoreException(() -> database.getConnection());
		}


		private HikariConfig createConfig()
		{
			final HikariConfig config = new HikariConfig();

			config.addDataSourceProperty("cachePrepStmts", true);
			config.addDataSourceProperty("prepStmtCacheSize", 250);
			config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
			config.addDataSourceProperty("useServerPrepStmts", true);
			config.addDataSourceProperty("cacheCallableStmts", true);
			config.addDataSourceProperty("elideSetAutoCommits", true);
			config.addDataSourceProperty("useLocalSessionState", true);
			config.addDataSourceProperty("alwaysSendSetIsolation", true);
			config.addDataSourceProperty("cacheResultSetMetadata", true);
			config.addDataSourceProperty("cacheServerConfiguration", true);

			return config;
		}

	}

	/*private static final class REDISCompanyDatabaseImpl extends CompanyDatabaseImpl implements RedisProvider
	{

		private final JedisPool database;

		private REDISCompanyDatabaseImpl(final Companies companies)
		{
			super(companies);

			final JedisPoolConfig config = createConfig();
			final RedisDBConfig redis = companies.createOrLoadRedisDBConfig();

			this.database = new JedisPool(config, redis.getHost(), Helper.toInt(redis.getPort(), 6379), 0, redis.getPassword(), Helper.toInt(redis.getDatabase(), 0));
		}


		@Override
		public void load()
		{

		}

		@Override
		public void kill()
		{
			database.close();
		}


		@Override
		public void saveCompany(final Company data)
		{

		}

		@Override
		public void loadCompany(final UUID uuid, final Consumer<Optional<Company>> consumer)
		{

		}

		@Override
		public void bulkLoadCompanies(final Consumer<Collection<Company>> consumer)
		{

		}


		@Override
		public DatabaseType getUUID()
		{
			return DatabaseType.REMOTE_REDIS;
		}


		@Override
		public boolean autoCloseResource()
		{
			return true;
		}

		@Override
		public Optional<Jedis> getConnection()
		{
			return Helper.ignoreException(() -> database.getResource());
		}


		private JedisPoolConfig createConfig()
		{
			final JedisPoolConfig config = new JedisPoolConfig();

			config.setMaxTotal(8);
			config.setMaxIdle(8);
			config.setMinIdle(0);
			config.setTestOnBorrow(true);

			return config;
		}

	}*/


	public static CompanyDatabase get(Companies companies, DatabaseType type)
	{
		CompanyDatabase database;

		switch (type)
		{
			case FOLDER:
				database = new LOCALCompanyDatabaseImpl(companies);
				break;
			case REMOTE_MYSQL:
				database = new MYSQLCompanyDatabaseImpl(companies);
				break;
			/*case REMOTE_REDIS:
				database = new REDISCompanyDatabaseImpl(companies);
				break;*/
			default:
				throw new IllegalArgumentException("Cannot produce a database for type: " + type);
		}

		return database;
	}


	private interface SQLConstants
	{

		String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS `company`(`uuid` CHAR(32), `name` VARCHAR(32)) PRIMARY KEY `uuid`";
		String INSERT_STATEMENT = "INSERT INTO `company`(`uuid`, `name`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `uuid`=VALUES(`uuid`), `name`=VALUES(`name`)";


		String SELECT_ALL_STATEMENT = "SELECT * FROM `company`";
		String SELECT_ONE_STATEMENT = "SELECT * FROM `company` WHERE `uuid`=?";


		static Optional<Company> recreateCompany(ResultSet resultSet, Companies companies) throws SQLException
		{
			if (!resultSet.next())
			{
				return Optional.empty();
			}
			else
			{
				final String uuidString = resultSet.getString("uuid");
				final String nameString = resultSet.getString("name");

				final CompanyImpl company = new CompanyImpl(companies);

				company.setName(nameString);
				company.setUUID(UUID.fromString(uuidString));

				return Optional.of(company);
			}
		}

		static Collection<Company> recreateCompanies(ResultSet resultSet, Companies companies) throws SQLException
		{
			final Set<Company> recreated = Helper.newSet();

			while (true)
			{
				final Optional<Company> company = SQLConstants.recreateCompany(resultSet, companies);

				if (!company.isPresent())
				{
					break;
				}

				recreated.add(company.get());
			}

			return recreated;
		}


	}

}
