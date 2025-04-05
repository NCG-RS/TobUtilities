package com.tobutilities.verzik;

import com.tobutilities.TobUtilitiesConfig;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import static net.runelite.api.Perspective.LOCAL_TILE_SIZE;


import javax.inject.Inject;
import java.awt.*;
import java.awt.geom.GeneralPath;

@Slf4j
public class VerzikOverlay extends Overlay {

    private final VerzikHandler verzikHandler;
    private final Client client;
    private final TobUtilitiesConfig config;

    @Inject
    private VerzikOverlay(VerzikHandler verzikHandler, Client client, TobUtilitiesConfig config) {
        this.verzikHandler = verzikHandler;
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }


    @Override
    public Dimension render (Graphics2D graphics)
    {
        if (config.highlightExplodingNylocas()) {
            for (NPC npc : verzikHandler.getExplodingNylocas()) {
                // Define the border stroke and color
                Stroke stroke = new BasicStroke(4.0f); //TODO make this configurable
                drawBox(graphics, 1, config.verzikNylocasColor(), stroke, 2, npc);
            }
        }

        return null;
    }

    private void drawBox(Graphics2D graphics, int radius, Color borderColour, Stroke borderStroke, int size, NPC npc)
    {
        graphics.setStroke(borderStroke);
        graphics.setColor(borderColour);
        graphics.draw(getSquareAroundPlayerLocation(npc, radius, size));
    }

    private GeneralPath getSquareAroundPlayerLocation(NPC npc, final int radius, final int size)
    {
        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);


        // Get the player's current local location
        LocalPoint npcLocation = LocalPoint.fromWorld(client.getWorldView(-1), npc.getWorldLocation());
        if (npcLocation == null)
        {
            return null; // Player location not available, skip render
        }

        // Get world position
        WorldPoint worldPoint = WorldPoint.fromLocalInstance(client, npcLocation);
        if (worldPoint == null)
        {
            return null; // World position not available, skip render
        }

        // Calculate the corners of the square based on player's world point
        final int startX = worldPoint.getX() - radius;
        final int startY = worldPoint.getY() - radius;
        final int z = worldPoint.getPlane();
        final int diameter = 2 * radius + size;

        // Corner 1: (startX, startY)
        moveTo(path, startX, startY, z);

        // Corner 2: (startX + diameter, startY)
        for (int i = 1; i <= diameter; i++)
        {
            lineTo(path, startX + i, startY, z);
        }

        // Corner 3: (startX + diameter, startY + diameter)
        for (int i = 1; i <= diameter; i++)
        {
            lineTo(path, startX + diameter, startY + i, z);
        }

        // Corner 4: (startX, startY + diameter)
        for (int i = 1; i <= diameter; i++)
        {
            lineTo(path, startX + diameter - i, startY + diameter, z);
        }

        // Close the square back to Corner 1
        for (int i = 1; i <= diameter-1; i++)
        {
            lineTo(path, startX, startY + diameter - i, z);
        }
        path.closePath();

        return path;
    }

    private boolean moveTo(GeneralPath path, final int x, final int y, final int z)
    {
        Point point = XYToPoint(x, y, z);
        if (point != null)
        {
            path.moveTo(point.getX(), point.getY());
            return true;
        }
        return false;
    }

    private void lineTo(GeneralPath path, final int x, final int y, final int z)
    {
        Point point = XYToPoint(x, y, z);
        if (point != null)
        {
            path.lineTo(point.getX(), point.getY());
        }
    }

    private Point XYToPoint(int x, int y, int z)
    {
        LocalPoint localPoint = LocalPoint.fromWorld(client, x, y);
        if (localPoint == null)
        {
            return null;
        }
        return Perspective.localToCanvas(
                client,
                new LocalPoint(localPoint.getX() - LOCAL_TILE_SIZE / 2, localPoint.getY() - LOCAL_TILE_SIZE / 2),
                z);
    }
}
