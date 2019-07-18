package com.sxtanna.mc.companies.core;

import com.sxtanna.mc.companies.Companies;
import com.sxtanna.mc.companies.util.Serial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public final class ProductImpl implements Product
{

	private transient final Companies companies;


	private UUID productUUID;
	private UUID stafferUUID;
	private UUID companyUUID;

	private String asBase64;


	private ProductImpl(final Companies companies, final ItemStack itemStack, final UUID productUUID, final UUID stafferUUID, final UUID companyUUID)
	{
		this.companies = companies;

		this.productUUID = productUUID;
		this.stafferUUID = stafferUUID;
		this.companyUUID = companyUUID;

		this.asBase64 = Serial.itemStackToBase64(itemStack).orElse("");
	}

	public ProductImpl(final Companies companies)
	{
		this(companies, new ItemStack(Material.AIR), Product.DEFAULT_UUID, Staffer.DEFAULT_UUID, Company.DEFAULT_UUID);
	}


	@Override
	public String asBase64()
	{
		return asBase64;
	}


	@Override
	public UUID getUUID()
	{
		return productUUID;
	}

	@Override
	public UUID getOwner()
	{
		return stafferUUID;
	}

	@Override
	public UUID getCompanyUUID()
	{
		return companyUUID;
	}


	// implementation
	public void setUUID(final UUID uuid)
	{
		this.productUUID = uuid;
	}

	public void setStafferUUID(final UUID stafferUUID)
	{
		this.stafferUUID = stafferUUID;
	}

	public void setCompanyUUID(final UUID companyUUID)
	{
		this.companyUUID = companyUUID;
	}

}
