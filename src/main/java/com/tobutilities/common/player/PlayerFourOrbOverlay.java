package com.tobutilities.common.player;

import com.tobutilities.TobUtilitiesPlugin;
import com.tobutilities.TobUtilitiesConfig;
import com.tobutilities.verzik.VerzikHandler;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class PlayerFourOrbOverlay extends Overlay implements PlayerOrb
{
	private final Client client;
	private final VerzikHandler verzikHandler;
	private final TobUtilitiesConfig config;

	@Inject
	protected PlayerFourOrbOverlay(Client client, VerzikHandler verzikHandler, TobUtilitiesConfig config)
	{
		this.client = client;
		this.verzikHandler = verzikHandler;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.MANUAL);
		drawAfterLayer(TobPlayerOrb.PLAYER_4.getOrbBackgroundId());
	}

	@Override
	public Dimension render(Graphics2D graphics) {
		Color overlayColor = config.dawnbringerOverlayColor();
		if (!TobPlayerOrb.PLAYER_4.equals(verzikHandler.getTobPlayerOrb())){
			return null;
		}
		return getDimension(graphics, overlayColor, client, verzikHandler);
	}
}
