package com.tobutilities.common.player;

import com.tobutilities.TobUtilitiesPlugin;
import com.tobutilities.verzik.VerzikHandler;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;

public interface PlayerOrb {

	default Dimension getDimension(Graphics2D graphics, Color overlayColor, Client client, VerzikHandler verzikHandler)
	{
		Widget playerOrbWidget = client.getWidget(verzikHandler.getTobPlayerOrb().getOrbId());
		if (playerOrbWidget != null)
		{
			Rectangle bounds = playerOrbWidget.getBounds();
			graphics.setColor(overlayColor);
			graphics.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
		}
		return null;
	}
}
