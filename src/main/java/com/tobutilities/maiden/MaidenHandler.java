package com.tobutilities.maiden;

import com.tobutilities.TobUtilitiesConfig;
import com.tobutilities.TobUtilitiesPlugin;
import com.tobutilities.common.RoomHandler;
import static com.tobutilities.maiden.MaidenConstants.NYLOCAS_MATOMENOS;
import static com.tobutilities.maiden.MaidenConstants.THE_MAIDEN_OF_SUGADINTI;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.eventbus.Subscribe;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@Singleton
public class MaidenHandler extends RoomHandler
{
	private final List<NPC> nylocasMatomenosSpawns = new ArrayList<>();

	public List<NPC> getNylocasMatomenosSpawns()
	{
		return nylocasMatomenosSpawns;
	}

	@Inject
	protected MaidenHandler(TobUtilitiesPlugin plugin, TobUtilitiesConfig config, Client client)
	{
		super(plugin, config, client);
	}

	@Inject
	private ChatMessageManager chatMessageManager;

	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		NPC npc = event.getNpc();
		if (npc != null)
		{

			if (StringUtils.isNotBlank(npc.getName()) && THE_MAIDEN_OF_SUGADINTI.equals(npc.getName()))
			{
				nylocasMatomenosSpawns.clear();
				if (config.enableMaidenIndexMessage() )
				{
					handleScuffedSpawnChatMessage(npc);
				}
			}
			if (config.enabledScuffedSpawnHighlight())
			{
				int x = npc.getWorldLocation().getRegionX();
				int y = npc.getWorldLocation().getRegionY();
				Point p = new Point(x, y);
				if (MaidenConstants.SCUFFED_SPAWNS.contains(p) && NYLOCAS_MATOMENOS.equals(npc.getName()))
				{
					if (!nylocasMatomenosSpawns.contains(npc))
					{
						nylocasMatomenosSpawns.add(npc);
					}
				}
			}
		}
	}

	private void handleScuffedSpawnChatMessage(NPC npc)
	{
		int maidenIndex = npc.getIndex();

		String chatMessage;
		if (maidenIndex < 58000)
		{
			chatMessage = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(Color.GREEN, String.format("There is no reasonable risk of a scuff. Maiden index: %d", maidenIndex))
				.build();
		}
		else if (maidenIndex < 60000)
		{
			chatMessage = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(Color.GREEN, String.format("A scuff is very unlikely (possible in small scales). Maiden index: %d", maidenIndex))
				.build();
		}
		else if (maidenIndex < 62000)
		{
			chatMessage = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(Color.GREEN, String.format("A scuff is unlikely. Maiden index: %d", maidenIndex))
				.build();
		}
		else if (maidenIndex < 64000)
		{
			chatMessage = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(Color.ORANGE, String.format("A scuff is likely, especially in small scales. Maiden index: %d", maidenIndex))
				.build();
		}
		else
		{
			chatMessage = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(Color.RED, String.format("A scuff is very likely. Maiden index: %d", maidenIndex))
				.build();
		}

		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.CONSOLE)
			.name("ToB Utilities")
			.sender("ToB Utilities")
			.runeLiteFormattedMessage(chatMessage)
			.build());
	}


	@Subscribe
	public void onGameTick(GameTick event)
	{
		nylocasMatomenosSpawns.removeIf(npc -> npc.isDead() || !NYLOCAS_MATOMENOS.equals(npc.getName()));
	}

	public void startUp(){
		nylocasMatomenosSpawns.clear();
	}

}
