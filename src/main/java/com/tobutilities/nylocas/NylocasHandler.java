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
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.eventbus.Subscribe;

import static com.tobutilities.nylocas.NylocasConstants.AGGRESSIVE_NYLOCAS_IDS;

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
	public void onGameTick(GameTick tick)
	{
		for (NPC npc : client.getWorldView(-1).npcs()){
			if (AGGRESSIVE_NYLOCAS_IDS.contains(npc.getId()) && !aggressiveNylocas.contains(npc) && !npc.isDead()){
				aggressiveNylocas.add(npc);
			}
		}
	}

	@Subscribe
	public void onActorDeath(ActorDeath event){
		if (event.getActor() instanceof NPC){
            aggressiveNylocas.remove((NPC) event.getActor());
		}
	}
}
