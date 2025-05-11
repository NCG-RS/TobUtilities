package com.tobutilities.verzik;

import com.tobutilities.TobUtilitiesConfig;
import com.tobutilities.TobUtilitiesPlugin;
import com.tobutilities.common.enums.Region;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

@Slf4j
public class LightbearerWarningOverlay extends OverlayPanel
{
	private final Client client;
	private final VerzikHandler verzikHandler;
	private final TobUtilitiesConfig config;
	private final TobUtilitiesPlugin plugin;

	@Inject
	private LightbearerWarningOverlay(Client client, VerzikHandler verzikHandler, TobUtilitiesConfig config, TobUtilitiesPlugin plugin)
	{
		this.client = client;
		this.verzikHandler = verzikHandler;
		this.config = config;
		this.plugin = plugin;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (verzikHandler.isLightbearerOverlayDisplayed() && Region.VERZIK.equals(plugin.region))
		{
			buildPanelComponent(graphics);
			setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
			return panelComponent.render(graphics);
		}
		return null;
	}

	private void buildPanelComponent(Graphics2D graphics)
	{

		String message = "You need to equip your lightbearer";
		panelComponent.getChildren().clear();


		panelComponent.getChildren().add(LineComponent.builder()
			.left(message)
			.build());


		panelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth(message) + 10, 0));
		panelComponent.setBackgroundColor(config.lightbearerOverlayColor());
	}
}