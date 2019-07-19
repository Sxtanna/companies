package com.sxtanna.mc.companies.lang;

import com.sxtanna.mc.companies.lang.base.LangKey;

public enum Messages implements LangKey
{

	COMPANY_JOIN_STAFFER("You have joined {company_name}."),

	COMPANY_JOIN_COMPANY("{staffer_name} has joined the company.");


	private final String value;


	Messages(final String value)
	{
		this.value = value;
	}


	@Override
	public final String getName()
	{
		return name().toLowerCase();
	}

	@Override
	public final String getDefaultValue()
	{
		return value;
	}

}