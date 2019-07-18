package com.sxtanna.mc.companies.base;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.sxtanna.mc.companies.Companies;
import com.sxtanna.mc.companies.mock.MockPlugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class MockedTest
{

	protected ServerMock server;
	protected MockPlugin plugin;

	protected Companies companies;


	@BeforeAll
	public void mockLoad()
	{
		server = MockBukkit.mock();
		plugin = MockBukkit.load(MockPlugin.class);

		companies = plugin.createMockCompanies();
		companies.load();

		onLoad();
	}

	@AfterAll
	public void mockKill()
	{
		onKill();

		companies.kill();

		MockBukkit.unload();
	}


	protected abstract void onLoad();

	protected abstract void onKill();



	protected interface Constants
	{

		UUID SXTANNA_UUID = UUID.fromString("41d1fed5-aa44-432c-ab1b-2810001f3270");
		UUID SOTANNA_UUID = UUID.fromString("bed76f02-fdd7-4cdd-aa20-0d96e2cd267b");

		String SXTANNA_NAME = "Sxtanna";
		String SOTANNA_NAME = "Sotanna";

	}

}
