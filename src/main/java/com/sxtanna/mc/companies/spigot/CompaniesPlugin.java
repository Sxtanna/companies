package com.sxtanna.mc.companies.spigot;

import com.sxtanna.mc.companies.Companies;
import com.sxtanna.mc.companies.CompaniesImpl;
import com.sxtanna.mc.companies.base.State;
import com.sxtanna.mc.companies.conf.CompanyConfigImpl;
import com.sxtanna.mc.companies.conf.type.CompanyConfig;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class CompaniesPlugin extends JavaPlugin
{

	private Companies companies;


	@Override
	public void onLoad()
	{
		CompanyConfig conf;

		try
		{
			conf = new CompanyConfigImpl(this);
			conf.load();
		}
		catch (Exception ex)
		{
			getLogger().log(Level.SEVERE, "Failed to load CompanyConfig", ex);
			return;
		}

		this.companies = new CompaniesImpl(this, conf);
	}

	@Override
	public void onEnable()
	{
		State.attemptLoad(this.companies);

		if (this.companies != null)
		{
			getServer().getServicesManager().register(Companies.class, this.companies, this, ServicePriority.Highest);
		}
	}

	@Override
	public void onDisable()
	{
		State.attemptKill(this.companies);

		getServer().getServicesManager().unregisterAll(this);
	}

}
