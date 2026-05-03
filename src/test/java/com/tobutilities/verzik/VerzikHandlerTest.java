package com.tobutilities.verzik;

import com.tobutilities.TobUtilitiesConfig;
import com.tobutilities.TobUtilitiesPlugin;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.party.PartyService;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VerzikHandlerTest
{
	private VerzikHandler handler;
	private Client client;
	private TobUtilitiesConfig config;

	@Before
	public void setUp()
	{
		client = mock(Client.class);
		config = mock(TobUtilitiesConfig.class);
		when(config.preserveVerzikEntryCamera()).thenReturn(true);
		when(config.enableDawnbringerOverlay()).thenReturn(false);
		when(config.enableLightbearerOverlay()).thenReturn(false);

		handler = new VerzikHandler(mock(TobUtilitiesPlugin.class), config, client);
		com.tobutilities.TestUtils.setField(handler, "partyService", mock(PartyService.class));
	}

	@Test
	public void preservesEntryCameraForThreeRestoreTicks()
	{
		when(client.getGameState()).thenReturn(GameState.LOGGED_IN);
		when(client.getCameraYawTarget()).thenReturn(512);
		when(client.getCameraPitchTarget()).thenReturn(240);

		handler.captureEntryCameraTargets();
		handler.onRoomEntry();
		handler.onGameTick(null);
		handler.onGameTick(null);
		handler.onGameTick(null);

		verify(client, org.mockito.Mockito.times(3)).setCameraYawTarget(512);
		verify(client, org.mockito.Mockito.times(3)).setCameraPitchTarget(240);
	}

	@Test
	public void loginScreenClearsSavedCameraSnapshot()
	{
		when(client.getGameState()).thenReturn(GameState.LOGGED_IN);
		when(client.getCameraYawTarget()).thenReturn(256);
		when(client.getCameraPitchTarget()).thenReturn(128);

		handler.captureEntryCameraTargets();

		GameStateChanged event = new GameStateChanged();
		event.setGameState(GameState.LOGIN_SCREEN);
		handler.onGameStateChanged(event);

		clearInvocations(client);
		handler.onRoomEntry();
		handler.onGameTick(null);

		verify(client, never()).setCameraYawTarget(256);
		verify(client, never()).setCameraPitchTarget(128);
	}
}
