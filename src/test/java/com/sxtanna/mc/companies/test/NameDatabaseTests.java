package com.sxtanna.mc.companies.test;

import com.sxtanna.mc.companies.base.MockedTest;
import com.sxtanna.mc.companies.data.NameDatabaseImpl;
import com.sxtanna.mc.companies.data.type.NameDatabase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

final class NameDatabaseTests extends MockedTest
{

	private NameDatabase database;


	@Override
	protected void onLoad()
	{
		database = new NameDatabaseImpl(plugin);
		database.load();
	}

	@Override
	protected void onKill()
	{
		database.kill();
	}


	@Test
	@Order(0)
	void saveNameTest()
	{
		database.saveName(Constants.SXTANNA_UUID, Constants.SXTANNA_NAME);
		database.saveName(Constants.SOTANNA_UUID, Constants.SOTANNA_NAME);
	}

	@Test
	@Order(1)
	void loadNameTest()
	{
		assertThat(database.loadName(Constants.SXTANNA_UUID)).isNotEmpty().contains(Constants.SXTANNA_NAME);
		assertThat(database.loadName(Constants.SOTANNA_UUID)).isNotEmpty().contains(Constants.SOTANNA_NAME);
	}




}
