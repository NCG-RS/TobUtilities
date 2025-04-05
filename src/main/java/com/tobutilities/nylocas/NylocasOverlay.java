package com.tobutilities.nylocas;

import com.tobutilities.TobUtilitiesConfig;
import com.tobutilities.maiden.MaidenHandler;

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
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

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
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics)
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
			if (localPoint != null )
			{
				tilePolygon = Perspective.getCanvasTileAreaPoly(client, localPoint, npcComposition.getSize());
				if (tilePolygon != null)
				{
					renderPoly(graphics, Color.red, tilePolygon, config.getBorderWidth(), true);
				}
			}
		}
		return null;
	}
	private void renderPoly(Graphics2D graphics, Color outlineColor, Shape polygon, double width, boolean antiAlias)
	{
		if (polygon != null)
		{
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setColor(outlineColor);
			graphics.setStroke(new BasicStroke((float) width));
			graphics.draw(polygon);
		}
	}
}
