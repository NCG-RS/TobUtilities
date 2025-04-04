package com.tobutilities.nylocas;

import com.tobutilities.TobUtilitiesConfig;
import com.tobutilities.TobUtilitiesPlugin;
import com.tobutilities.common.RoomHandler;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.eventbus.Subscribe;

@Singleton
public class NylocasHandler extends RoomHandler
{
	private final List<NPC> aggressiveNylocas = new ArrayList<>();

	public List<NPC> getAggressiveNylocas()
	{
		return aggressiveNylocas;
	}
	@Inject
	protected NylocasHandler(TobUtilitiesPlugin plugin, TobUtilitiesConfig config, Client client)
	{
		super(plugin, config, client);
	}
	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		NPC npc = event.getNpc();
		if (npc != null)
		{
			if (config.enableHighlightAggressiveNylos())
			{
				if (NylocasConstants.AGGRESSIVE_NYLOCAS_IDS.contains(npc.getId()))
				{
					if (!aggressiveNylocas.contains(npc))
					{
						aggressiveNylocas.add(npc);
					}
				}
			}
		}
	}
}
