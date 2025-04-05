package com.tobutilities.xarpus;

import com.tobutilities.TobUtilitiesConfig;
import com.tobutilities.TobUtilitiesPlugin;
import com.tobutilities.common.RoomHandler;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GroundObject;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.client.eventbus.Subscribe;

@Singleton
@Slf4j
public class XarpusHandler extends RoomHandler

{
	@Inject
	protected XarpusHandler (TobUtilitiesPlugin plugin, TobUtilitiesConfig config, Client client){
		super(plugin, config, client);
	}
	@Getter
	private List<GroundObject> exhumeds = new ArrayList<>();

	@Subscribe
	public void onGroundObjectSpawned(GroundObjectSpawned event)
	{
		if (event.getGroundObject() != null && XarpusConstants.XARPUS_EXHUMED_ID == event.getGroundObject().getId())
		{
			exhumeds.add(event.getGroundObject());
		}
	}

	@Subscribe
	public void onGroundObjectDespawned(GroundObjectDespawned event)
	{
		if (event.getGroundObject() != null && XarpusConstants.XARPUS_EXHUMED_ID == event.getGroundObject().getId() && exhumeds.contains(event.getGroundObject()))
		{
			exhumeds.remove(event.getGroundObject());
		}
	}
}
