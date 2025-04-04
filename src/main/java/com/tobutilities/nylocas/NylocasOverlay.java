package com.tobutilities.nylocas;

import com.tobutilities.TobUtilitiesConfig;
import com.tobutilities.maiden.MaidenHandler;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

public class NylocasOverlay extends Overlay
{
	private final Client client;
	private final NylocasHandler nylocasHandler;
	private final TobUtilitiesConfig config;
	private final ModelOutlineRenderer modelOutlineRenderer;

	@Inject
	private NylocasOverlay(Client client, NylocasHandler nylocasHandler, TobUtilitiesConfig config, ModelOutlineRenderer modelOutlineRenderer)
	{
		this.client = client;
		this.nylocasHandler = nylocasHandler;
		this.config = config;
		this.modelOutlineRenderer = modelOutlineRenderer;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		// First, set up the outlines for rendering
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

			Color highlight = config.getHighlightColor();
			modelOutlineRenderer.drawOutline(npc, config.getBorderWidth(), highlight, config.getBorderFeather());

		}

		return null;
	}
}
