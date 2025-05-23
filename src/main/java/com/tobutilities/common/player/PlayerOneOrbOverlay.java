package com.tobutilities.common.player;

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

public class PlayerOneOrbOverlay extends Overlay implements PlayerOrb
{
	private final Client client;
	private final VerzikHandler verzikHandler;
	private final TobUtilitiesConfig config;

	@Inject
	protected PlayerOneOrbOverlay(Client client, VerzikHandler verzikHandler, TobUtilitiesConfig config)
	{
		this.client = client;
		this.verzikHandler = verzikHandler;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.MANUAL);
		drawAfterLayer(TobPlayerOrb.PLAYER_1.getOrbBackgroundId());
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		Color overlayColor = getOverlayColor(verzikHandler, config);
		if (!TobPlayerOrb.PLAYER_1.equals(verzikHandler.getTobPlayerOrb()))
		{
			return null;
		}
		return getDimension(graphics, overlayColor, client, verzikHandler);
	}
}
