package com.tobutilities.bloat;

import com.tobutilities.TobUtilitiesConfig;
import com.tobutilities.TobUtilitiesPlugin;
import com.tobutilities.common.enums.Region;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

public class BloatPlayerOverlay extends Overlay
{
	private final Client client;
	private final TobUtilitiesConfig config;
	private final ModelOutlineRenderer modelOutlineRenderer;
	private final TobUtilitiesPlugin plugin;

	@Inject
	private BloatPlayerOverlay(Client client, TobUtilitiesConfig config, ModelOutlineRenderer modelOutlineRenderer, TobUtilitiesPlugin plugin)
	{
		this.client = client;
		this.config = config;
		this.modelOutlineRenderer = modelOutlineRenderer;
		this.plugin = plugin;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (Region.BLOAT.equals(plugin.region) && config.enableOutlinePlayers())
		{
			for (Player player : client.getWorldView(-1).players())
			{
				if (player == null || player.isDead())
				{
					continue;
				}

				Color highlight = config.getHighlightColor();
				modelOutlineRenderer.drawOutline(player, config.getBorderWidth(), highlight, 1);

			}
		}
		return null;
	}
}