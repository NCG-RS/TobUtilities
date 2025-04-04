package com.tobutilities.xarpus;

import com.tobutilities.TobUtilitiesConfig;
import com.tobutilities.xarpus.XarpusHandler;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GroundObject;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import java.awt.Shape;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

public class XarpusOverlay extends Overlay
{
		private final Client client;
		private final XarpusHandler xarpusHandler;
		private final TobUtilitiesConfig config;
		private final ModelOutlineRenderer modelOutlineRenderer;

	@Inject
	private XarpusOverlay (Client client, XarpusHandler xarpusHandler, TobUtilitiesConfig config, ModelOutlineRenderer modelOutlineRenderer)
		{
			this.client = client;
			this.xarpusHandler = xarpusHandler;
			this.config = config;
			this.modelOutlineRenderer = modelOutlineRenderer;

			setPosition(OverlayPosition.DYNAMIC);
			setLayer(OverlayLayer.ABOVE_SCENE);
		}

		@Override
		public Dimension render (Graphics2D graphics)
		{
			// First, set up the outlines for rendering
			for (GroundObject groundObject : xarpusHandler.getExhumeds())
			{
				renderObjectOutline(graphics, groundObject, Color.BLACK);

			}

			return null;
		}

	private void renderObjectOutline(Graphics2D graphics, GroundObject groundObject, Color color)
	{
		Shape objectClickbox = groundObject.getConvexHull();
		if (objectClickbox != null)
		{
			graphics.setColor(color);
			graphics.draw(objectClickbox);
		}
	}

}
