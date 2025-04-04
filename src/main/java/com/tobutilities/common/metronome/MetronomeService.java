package com.tobutilities.common.metronome;

import com.tobutilities.TobUtilitiesConfig;
import com.tobutilities.TobUtilitiesPlugin;
import com.tobutilities.common.enums.Region;
import static com.tobutilities.common.util.CommonUtils.getRegionByRegionId;
import java.awt.Color;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.util.Text;

@Slf4j
@Singleton
public class MetronomeService

{
	@Inject
	private Client client;

	@Inject
	private TobUtilitiesPlugin plugin;

	@Inject
	private TobUtilitiesConfig config;

	private int tickCounter = 0;

	@Getter
	@Setter
	private boolean metronomeDisplayed = true;
	@Getter
	@Setter
	private Color currentColor = Color.WHITE;
	@Getter
	@Setter
	private int currentColorIndex = 0;


	@Inject
	public MetronomeService(Client client, TobUtilitiesPlugin plugin, TobUtilitiesConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		int regionTickCount;
		Player player = client.getLocalPlayer();
		final LocalPoint playerLocation = player.getLocalLocation();
		WorldPoint playerLocationPoint = WorldPoint.fromLocalInstance(client, playerLocation);
		final int regionId = playerLocationPoint.getRegionID();
		Region newTickRegion = getRegionByRegionId(regionId);

		if (newTickRegion != plugin.region)
		{
			if (newTickRegion.getWaveNumber() > plugin.region.getWaveNumber())
			{
				// raid has advanced to next room
				plugin.region = newTickRegion;
				if (isCurrentRegionMetronomeEnabled(plugin.region))
				{
					//enable metronome if applicable
					setMetronomeDisplayed(true);
				}
			}
			else
			{
				//In event of either wipe or after treasure room
				plugin.region = newTickRegion;
			}
		}

		if (plugin.region.getTickCount() == 0)
		{
			//Player is in a region where metronome isn't used
			setMetronomeDisplayed(false);
			return;
		}

//		setMetronomeDisplayed(true);
		regionTickCount = plugin.region.getTickCount();

		if (getCurrentColorIndex() >= regionTickCount)
		{
			tickCounter = 0;
			setCurrentColorIndex(0);
		}

		int colorIndex = getCurrentColorIndex() + 1;
		setCurrentColorIndex(colorIndex);

		switch (colorIndex)
		{
			case 1:
				setCurrentColor(config.getTick1Color());
				break;
			case 2:
				setCurrentColor(config.getTick2Color());
				break;
			case 3:
				setCurrentColor(config.getTick3Color());
				break;
			case 4:
				setCurrentColor(config.getTick4Color());
				break;
			case 5:
				setCurrentColor(config.getTick5Color());
				break;
			case 6:
				setCurrentColor(config.getTick6Color());
				break;
			case 7:
				setCurrentColor(config.getTick7Color());
				break;
		}
		tickCounter++;
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		try
		{
			if (event.getType().equals(ChatMessageType.GAMEMESSAGE))
			{
				String message = Text.removeTags(event.getMessage());

				//Use teammates entering room to begin instance timer
				if (message.contains("Wave 3") && config.enableNyloMetronome())
				{
					log.info("Teammate advanced to nylocas room - starting instance timer");
					plugin.region = Region.NYLOCAS;
					tickCounter = 0;
					setCurrentColorIndex(0);
					setMetronomeDisplayed(true);
				}
				else if (message.contains("Wave 4") && config.enableSoteMetronome())
				{
					log.info("Teammate advanced to sotetseg room - starting instance timer");
					plugin.region = Region.SOTETSEG;
					tickCounter = 0;
					setCurrentColorIndex(0);
					setMetronomeDisplayed(true);
				}
				else if (message.contains("Wave 5") && config.enableXarpusMetronome())
				{
					log.info("Teammate advanced to xarpus room - starting instance timer");
					plugin.region = Region.XARPUS;
					tickCounter = 0;
					setCurrentColorIndex(0);
					setMetronomeDisplayed(true);
				}
			}
		}
		catch (Exception ex)
		{
			log.error(ex.getMessage(), ex);
		}
	}

	private boolean isCurrentRegionMetronomeEnabled(Region region)
	{
		if (Region.NYLOCAS.equals(region) && config.enableNyloMetronome())
		{
			return true;
		}
		else if (Region.SOTETSEG.equals(region) && config.enableSoteMetronome())
		{
			return true;
		}
		else if (Region.XARPUS.equals(region) && config.enableXarpusMetronome())
		{
			return true;
		}
		else if (Region.VERZIK.equals(region) && config.enableVerzikMetronome())
		{
			return true;
		}
		return false;
	}

}
