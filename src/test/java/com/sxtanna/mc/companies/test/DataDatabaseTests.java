package com.sxtanna.mc.companies.test;

import com.sxtanna.mc.companies.base.MockedTest;
import com.sxtanna.mc.companies.core.Company;
import com.sxtanna.mc.companies.core.Staffer;
import com.sxtanna.mc.companies.data.DataDatabaseImpl;
import com.sxtanna.mc.companies.data.core.CompanyDatabase;
import com.sxtanna.mc.companies.data.core.CompanyDatabaseImpl;
import com.sxtanna.mc.companies.data.core.ProductDatabase;
import com.sxtanna.mc.companies.data.core.ProductDatabaseImpl;
import com.sxtanna.mc.companies.data.core.StafferDatabase;
import com.sxtanna.mc.companies.data.core.StafferDatabaseImpl;
import com.sxtanna.mc.companies.data.type.DataDatabase;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public final class DataDatabaseTests extends MockedTest
{

	private static final AtomicReference<UUID> companyUUID = new AtomicReference<>();


	private DataDatabase database;


	@Override
	protected void onLoad()
	{
		final CompanyDatabase company = createCompanyDatabase();
		final ProductDatabase product = createProductDatabase();
		final StafferDatabase staffer = createStafferDatabase();

		database = new DataDatabaseImpl(plugin, company, product, staffer);
		database.load();
	}

	@Override
	protected void onKill()
	{
		database.kill();
	}


	@Test
	@Order(0)
	void saveStafferTest()
	{
		database.saveStaffer(companies.createStaffer(Constants.SXTANNA_UUID, Constants.SXTANNA_NAME));
	}

	@Test
	@Order(1)
	void loadStafferTest()
	{
		database.loadStaffer(Constants.SXTANNA_UUID, staffer ->
		{
			assertThat(staffer).hasValueSatisfying(value ->
												   {
													   assertThat(value).extracting(Staffer::getUUID).isEqualTo(Constants.SXTANNA_UUID);
													   assertThat(value).extracting(Staffer::getName).isEqualTo(Constants.SXTANNA_NAME);
												   });
		});
	}


	@Test
	@Order(2)
	void saveCompanyTest()
	{
		final Company company = companies.createCompany(Constants.SXTANNA_UUID, "Test Company");
		database.saveCompany(company);

		companyUUID.set(company.getUUID());
	}

	@Test
	@Order(3)
	void loadCompanyTest()
	{
		database.loadCompany(companyUUID.get(), company ->
		{
			assertThat(company).hasValueSatisfying(value ->
												   {

													   assertThat(value).extracting(Company::getUUID).isEqualTo(companyUUID.get());
													   assertThat(value).extracting(Company::getName).isEqualTo("Test Company");

												   });
		});
	}


	private CompanyDatabase createCompanyDatabase()
	{
		return CompanyDatabaseImpl.get(companies, companies.companyConfig().getCompanyDatabaseType());
	}

	private ProductDatabase createProductDatabase()
	{
		return ProductDatabaseImpl.get(companies, companies.companyConfig().getProductDatabaseType());
	}

	private StafferDatabase createStafferDatabase()
	{
		return StafferDatabaseImpl.get(companies, companies.companyConfig().getStafferDatabaseType());
	}

}
