package com.tobutilities.xarpus;

import com.tobutilities.TobUtilitiesConfig;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GroundObject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

@Slf4j
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
			for (GroundObject groundObject : xarpusHandler.getExhumeds())
			{
				modelOutlineRenderer.drawOutline(groundObject, config.getBorderWidth(), config.getHighlightColor(), config.getBorderFeather());
			}
			return null;
		}
	}


