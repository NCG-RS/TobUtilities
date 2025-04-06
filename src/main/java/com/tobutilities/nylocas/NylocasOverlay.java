package com.tobutilities.nylocas;

import com.tobutilities.TobUtilitiesConfig;

import java.awt.*;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class NylocasOverlay extends Overlay
{
	private final Client client;
	private final NylocasHandler nylocasHandler;
	private final TobUtilitiesConfig config;
	private Polygon tilePolygon;

	@Inject
	private NylocasOverlay(Client client, NylocasHandler nylocasHandler, TobUtilitiesConfig config)
	{
		this.client = client;
		this.nylocasHandler = nylocasHandler;
		this.config = config;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ALWAYS_ON_TOP);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (config.enableHighlightAggressiveNylos())
		{
			for (NPC npc : nylocasHandler.getAggressiveNylocas())
			{
				if (npc == null || npc.isDead())
				{
					continue;
				}

				NPCComposition npcComposition = npc.getTransformedComposition();
				if (npcComposition == null)
				{
					continue;
				}

				LocalPoint localPoint = npc.getLocalLocation();
				if (localPoint != null)
				{
					tilePolygon = Perspective.getCanvasTileAreaPoly(client, localPoint, npcComposition.getSize());
					if (tilePolygon != null)
					{
						renderPoly(graphics, tilePolygon, config.getBorderWidth());
					}
				}
			}
		}
		return null;
	}
	private void renderPoly(Graphics2D graphics,  Shape polygon, double width)
	{
		if (polygon != null)
		{
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setColor(Color.RED);
			graphics.setStroke(new BasicStroke((float) width));
			graphics.draw(polygon);
		}
	}
}
