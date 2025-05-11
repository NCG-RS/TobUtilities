package com.tobutilities.maiden;

import com.tobutilities.TobUtilitiesConfig;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;

import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

public class ScuffWarningOverlay extends OverlayPanel
{
	private final Client client;
	private final MaidenHandler maidenHandler;
	private final TobUtilitiesConfig config;

	@Inject
	private ScuffWarningOverlay(Client client, MaidenHandler maidenHandler, TobUtilitiesConfig config, ModelOutlineRenderer modelOutlineRenderer)
	{
		this.client = client;
		this.maidenHandler = maidenHandler;
		this.config = config;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (maidenHandler.isOverlayDisplayed())
		{
			buildPanelComponent(graphics);
			setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
			return panelComponent.render(graphics);
		}
		return null;
	}

	private void buildPanelComponent(Graphics2D graphics)
	{
		int maidenIndex = maidenHandler.getMaidenIndex();
		String message;
		String indexInfo = "Maiden index: " + maidenIndex;
		Color color = Color.GREEN;
		panelComponent.getChildren().clear();

		if (maidenIndex < 58000)
		{
			message = "There is no reasonable risk of a scuff";
		}
		else if (maidenIndex < 60000)
		{
			message = "A scuff is very unlikely";
		}
		else if (maidenIndex < 61000)
		{
			message = "A scuff is unlikely (possible in small scales)";
		}
		else if (maidenIndex < 63500)
		{
			message = "A scuff is likely, especially in small scales";
			color = Color.ORANGE;
		}
		else
		{
			message = "A scuff is very likely";
			color = Color.RED;
		}

		panelComponent.getChildren().add(LineComponent.builder()
			.left(message)
			.leftColor(color)
			.build());

		panelComponent.getChildren().add(LineComponent.builder()
			.left(indexInfo)
			.leftColor(color)
			.build());

		panelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth(message) + 10, 0));
		panelComponent.setBackgroundColor(new Color(100, 85, 85, 100));
	}
}