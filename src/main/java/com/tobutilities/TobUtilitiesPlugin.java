package com.tobutilities;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Provides;
import com.tobutilities.bloat.BloatHandler;
import com.tobutilities.common.metronome.MetronomeService;
import com.tobutilities.common.metronome.MetronomeOverlay;
import com.tobutilities.common.enums.Region;
import com.tobutilities.maiden.MaidenHandler;
import com.tobutilities.maiden.MaidenOverlay;
import com.tobutilities.common.player.PlayerOneOrbOverlay;
import com.tobutilities.common.player.PlayerTwoOrbOverlay;
import com.tobutilities.common.player.PlayerThreeOrbOverlay;
import com.tobutilities.common.player.PlayerFourOrbOverlay;
import com.tobutilities.common.player.PlayerFiveOrbOverlay;
import com.tobutilities.nylocas.NylocasHandler;
import com.tobutilities.nylocas.NylocasOverlay;
import com.tobutilities.verzik.VerzikHandler;
import com.tobutilities.verzik.VerzikOverlay;
import com.tobutilities.xarpus.XarpusHandler;
import com.tobutilities.xarpus.XarpusOverlay;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;

import net.runelite.client.callback.Hooks;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;
import java.awt.event.KeyEvent;

@PluginDescriptor(
	name = "ToB Utilities",
	description = "Various tools for the theatre of blood",
	tags = {"timers", "overlays", "tick", "theatre", "metronome", "tob", "maiden", "bloat", "nylo", "xarpus", "verzik"}
)
@Slf4j
public class TobUtilitiesPlugin extends Plugin
{
	@Inject
	private OverlayManager overlayManager;
	@Inject
	public ConfigManager configManager;
	@Inject
	private Client client;
	@Inject
	private MetronomeOverlay metronomeOverlay;
	@Inject
	private PlayerOneOrbOverlay playerOneOrbOverlay;
	@Inject
	private PlayerTwoOrbOverlay playerTwoOrbOverlay;
	@Inject
	private PlayerThreeOrbOverlay playerThreeOrbOverlay;
	@Inject
	private PlayerFourOrbOverlay playerFourOrbOverlay;
	@Inject
	private PlayerFiveOrbOverlay playerFiveOrbOverlay;
	@Inject
	private MaidenOverlay maidenOverlay;
	@Inject
	private NylocasOverlay nylocasOverlay;
	@Inject
	private XarpusOverlay xarpusOverlay;
	@Inject
	private VerzikOverlay verzikOverlay;
	@Inject
	private TobUtilitiesConfig config;
	@Inject
	private KeyManager keyManager;
	@Inject
	private Hooks hooks;
	@Inject
	private MaidenHandler maidenHandler;
	@Inject
	private BloatHandler bloatHandler;
	@Inject
	private NylocasHandler nylocasHandler;
	@Inject
	private XarpusHandler xarpusHandler;
	@Inject
	private VerzikHandler verzikHandler;
	@Inject
	private MetronomeService metronomeService;

	public Region region = Region.UNKNOWN;
	private final Hooks.RenderableDrawListener drawListener = this::shouldDraw;

	@Provides
	TobUtilitiesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TobUtilitiesConfig.class);
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{

		if (region.equals(Region.VERZIK))
		{
			verzikHandler.onGameTick(tick);
		} else if (region.equals(Region.BLOAT))
		{
			bloatHandler.onGameTick(tick);
		} else if (region.equals(Region.NYLOCAS))
		{
			nylocasHandler.onGameTick(tick);
		}
		metronomeService.onGameTick(tick);
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		metronomeService.onChatMessage(event);
	}

	private final HotkeyListener hideVerzikHotkeyListener = new HotkeyListener(() -> config.hideVerzikHotkey())
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			if (Region.VERZIK.equals(region))
			{
			verzikHandler.keyPressed(e);
			}
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			hotkeyReleased();
		}
	};


	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		if (Region.MAIDEN.equals(region))
		{
			maidenHandler.onNpcSpawned(event);
		} else if (Region.VERZIK.equals(region))
		{
			verzikHandler.onNpcSpawned(event);
		}
	}

	@Subscribe
	public void onGroundObjectSpawned(GroundObjectSpawned event)
	{
		if (Region.XARPUS.equals(region))
		{
			xarpusHandler.onGroundObjectSpawned(event);
		}
	}

	@Subscribe
	public void onGroundObjectDespawned(GroundObjectDespawned event)
	{
		if (Region.XARPUS.equals(region))
		{
			xarpusHandler.onGroundObjectDespawned(event);
		}
	}


	@VisibleForTesting
	boolean shouldDraw(Renderable renderable, boolean drawingUI)
	{
		if (Region.BLOAT.equals(region)){
			return bloatHandler.shouldDraw(renderable);
		}

		if (Region.VERZIK.equals(region)){
			return verzikHandler.shouldDraw(renderable, drawingUI);
		}
		return true;
	}

	@Subscribe
	void onActorDeath(ActorDeath event){
		nylocasHandler.onActorDeath(event);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(metronomeOverlay);
		overlayManager.add(playerOneOrbOverlay);
		overlayManager.add(playerTwoOrbOverlay);
		overlayManager.add(playerThreeOrbOverlay);
		overlayManager.add(playerFourOrbOverlay);
		overlayManager.add(playerFiveOrbOverlay);
		overlayManager.add(maidenOverlay);
		overlayManager.add(nylocasOverlay);
		overlayManager.add(xarpusOverlay);
		overlayManager.add(verzikOverlay);
		maidenHandler.startUp();
		keyManager.registerKeyListener(hideVerzikHotkeyListener);
		hooks.registerRenderableDrawListener(drawListener);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(metronomeOverlay);
		overlayManager.remove(playerOneOrbOverlay);
		overlayManager.remove(playerTwoOrbOverlay);
		overlayManager.remove(playerThreeOrbOverlay);
		overlayManager.remove(playerFourOrbOverlay);
		overlayManager.remove(playerFiveOrbOverlay);
		overlayManager.remove(maidenOverlay);
		overlayManager.remove(nylocasOverlay);
		overlayManager.remove(xarpusOverlay);
		overlayManager.remove(verzikOverlay);
		keyManager.unregisterKeyListener(hideVerzikHotkeyListener);
		hooks.unregisterRenderableDrawListener(drawListener);
	}
}
