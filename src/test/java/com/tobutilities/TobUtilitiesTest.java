package com.tobutilities;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TobUtilitiesTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TobUtilitiesPlugin.class);
		RuneLite.main(args);
	}
}