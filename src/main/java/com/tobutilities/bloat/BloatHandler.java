package com.tobutilities.bloat;

import static com.tobutilities.bloat.BloatConstants.PESTILENT_BLOAT;
import com.tobutilities.common.RoomHandler;
import com.tobutilities.TobUtilitiesPlugin;
import com.tobutilities.TobUtilitiesConfig;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Renderable;

import net.runelite.api.events.GameTick;

import net.runelite.client.eventbus.Subscribe;

@Slf4j
public class BloatHandler extends RoomHandler
{
	boolean isBloatAlive;

	@Inject
	protected BloatHandler(TobUtilitiesPlugin plugin, TobUtilitiesConfig config, Client client)
	{
		super(plugin, config, client);
	}

	public boolean shouldDraw(Renderable renderable, boolean drawingUi)
	{
		if (renderable instanceof Player && isBloatAlive)
		{
			if (drawingUi){
				return true;
			}
			Player player = (Player) renderable;
			if (player.equals(client.getLocalPlayer()))
			{
				return !config.hideLocalPlayerDuringBloat();
			}
			else
			{
				return !config.hideOtherPlayersDuringBloat();
			}
		}
		return true;
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		for (NPC npc : client.getWorldView(-1).npcs())
		{
			if (PESTILENT_BLOAT.equals(npc.getName()))
			{
				isBloatAlive = !npc.isDead();
			}
		}
	}
}

