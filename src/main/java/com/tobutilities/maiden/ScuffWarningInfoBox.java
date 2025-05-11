package com.tobutilities.maiden;


import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.infobox.InfoBox;

@Slf4j
public class ScuffWarningInfoBox extends InfoBox
{
	@Inject
	private MaidenHandler maidenHandler;


	public ScuffWarningInfoBox(BufferedImage image, Plugin plugin, MaidenHandler maidenHandler)
	{
		super(image, plugin);
		this.maidenHandler = maidenHandler;
	}

	@Override
	public String getText()
	{
		return Integer.toString(maidenHandler.getMaidenIndex());
	}

	@Override
	public Color getTextColor()
	{
		if (maidenHandler.getMaidenIndex() < 61000)
		{
			return Color.GREEN;
		}
		else if (maidenHandler.getMaidenIndex() < 63500)
		{
			return Color.ORANGE;
		}
		else
		{
			return Color.RED;
		}
	}

	@Override
	public String getTooltip()
	{
		if (maidenHandler.getMaidenIndex() < 58000)
		{
			return "There is no reasonable risk of a scuff.";
		}
		else if (maidenHandler.getMaidenIndex() < 60000)
		{
			return "A scuff is very unlikely (possible in small scales)";
		}
		else if (maidenHandler.getMaidenIndex() < 61000)
		{
			return "A scuff is unlikely.";
		}
		else if (maidenHandler.getMaidenIndex() < 63500)
		{
			return "A scuff is likely.";
		}
		else
		{
			return "A scuff is very likely.";
		}
	}

}