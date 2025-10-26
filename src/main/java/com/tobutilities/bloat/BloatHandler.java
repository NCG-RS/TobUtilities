package com.tobutilities.bloat;

import static com.tobutilities.bloat.BloatConstants.BLOAT_FLOOR_IDS;
import static com.tobutilities.bloat.BloatConstants.PESTILENT_BLOAT;
import com.tobutilities.common.RoomHandler;
import com.tobutilities.TobUtilitiesPlugin;
import com.tobutilities.TobUtilitiesConfig;
import javax.inject.Inject;

import com.tobutilities.common.enums.Region;
import lombok.extern.slf4j.Slf4j;

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.BeforeRender;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;

import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BloatHandler extends RoomHandler
{
	private boolean isBloatAlive = false;
    private final Map<LocalPoint, GroundObject> hiddenObjects = new HashMap<>();
    private int bloatSkyboxColor;
    private boolean bloatSkyboxOverride;
    private boolean hideBloatFloor;
    private String originalHdSkyColorConfig;
    private String originalHdSkyConfig;
    private boolean hdConfigApplied;

    @Inject
    private ConfigManager configManager;

	@Inject
	protected BloatHandler(TobUtilitiesPlugin plugin, TobUtilitiesConfig config, Client client)
	{
		super(plugin, config, client);
	}

	public boolean shouldDraw(Renderable renderable, boolean drawingUi)
	{
		if (renderable instanceof Player && isBloatAlive)
		{
			if (drawingUi){
				return true;
			}
			Player player = (Player) renderable;
			if (player.equals(client.getLocalPlayer()))
			{
				return !config.hideLocalPlayerDuringBloat();
			}
			else
			{
				return !config.hideOtherPlayersDuringBloat();
			}
		}
		return true;
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		for (NPC npc : client.getWorldView(-1).npcs())
		{
			if (PESTILENT_BLOAT.equals(npc.getName()))
			{
				isBloatAlive = !npc.isDead();
                break;
			}
		}
        updateHdConfig();
        updateGroundObjects();
	}

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        switch (event.getGameState())
        {
            case LOADING:
                hiddenObjects.clear();
                break;

            case LOGGED_IN:
                if (plugin.region == Region.BLOAT)
                {
                    updateGroundObjects();
                    updateHdConfig();
                }
                break;

            case LOGIN_SCREEN:
                hiddenObjects.clear();
                restoreHdConfig();
                break;
        }
    }

    @Subscribe(priority = -1.0f)
    public void onBeforeRender(BeforeRender r)
    {
        if (bloatSkyboxOverride && client.getGameState() == GameState.LOGGED_IN)
        {
            client.setSkyboxColor(bloatSkyboxColor);
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event)
    {
        if (event.getGroup().equals("tobutilities"))
        {
            switch (event.getKey())
            {
                case "hideBloatFloor":
                    hideBloatFloor = config.hideBloatFloor();
                    updateGroundObjects();
                    break;

                case "bloatSkyboxOverride":
                    bloatSkyboxOverride = config.enableBloatSkyboxOverride();
                    updateHdConfig();
                    break;

                case "bloatSkyboxColor":
                    bloatSkyboxColor = config.bloatSkyboxColor().getRGB();
                    break;
            }
        }
    }

    private void updateGroundObjects()
    {
        if (hideBloatFloor)
        {
            hideGroundObjects();
        }
        else
        {
            unhideGroundObjects();
        }
    }

    private void hideGroundObjects()
    {
        Scene scene = client.getTopLevelWorldView().getScene();

        if (scene == null)
        {
            return;
        }

        Tile[][][] tiles = scene.getTiles();

        for (int z = 0; z < Constants.MAX_Z; z++)
        {
            if (tiles[z] == null) continue;

            for (int x = 0; x < Constants.SCENE_SIZE; x++)
            {
                if (tiles[z][x] == null) continue;

                for (int y = 0; y < Constants.SCENE_SIZE; y++)
                {
                    Tile tile = tiles[z][x][y];
                    if (tile == null)
                    {
                        continue;
                    }

                    GroundObject groundObject = tile.getGroundObject();
                    if (groundObject != null && BLOAT_FLOOR_IDS.contains(groundObject.getId()))
                    {
                        LocalPoint lp = tile.getLocalLocation();

                        // Skip if already hidden
                        if (hiddenObjects.containsKey(lp)) continue;

                        // Cache original object
                        hiddenObjects.put(lp, groundObject);

                        // Remove it from the scene
                        tile.setGroundObject(null);
                    }
                }
            }
        }
    }

    private void unhideGroundObjects()
    {
        Scene scene = client.getTopLevelWorldView().getScene();
        if (scene == null)
        {
            hiddenObjects.clear();
            return;
        }

        for (Map.Entry<LocalPoint, GroundObject> entry : hiddenObjects.entrySet())
        {
            LocalPoint lp = entry.getKey();
            Tile tile = getTileAtLocalPoint(lp);
            if (tile == null)
                continue;

            // Restore ground object if not already present
            if (tile.getGroundObject() == null)
            {
                tile.setGroundObject(entry.getValue());
            }
        }
        hiddenObjects.clear();
    }

    private Tile getTileAtLocalPoint(LocalPoint point)
    {
        WorldView wv = client.getTopLevelWorldView();
        Scene scene = wv.getScene();
        Tile[][] tiles = scene.getTiles()[wv.getPlane()];
        int sceneX = point.getSceneX();
        int sceneY = point.getSceneY();

        if (sceneX < 0 || sceneY < 0 || sceneX >= Constants.SCENE_SIZE || sceneY >= Constants.SCENE_SIZE)
        {
            return null;
        }

        return tiles[sceneX][sceneY];
    }

    private void updateHdConfig() {
        if (bloatSkyboxOverride && !hdConfigApplied)
        {
            overrideHdConfig();
        }
        else if (!bloatSkyboxOverride && hdConfigApplied)
        {
            restoreHdConfig();
        }
    }

    private void overrideHdConfig() {
        final String group = "hd";
        // Save original hd config
        if (originalHdSkyColorConfig == null)
        {
            originalHdSkyColorConfig = configManager.getConfiguration(group, "defaultSkyColor");
        }
        if (originalHdSkyConfig == null)
        {
            originalHdSkyConfig = configManager.getConfiguration(group, "overrideSky");
        }

        // Apply temporary overrides
        configManager.setConfiguration(group, "defaultSkyColor", "RUNELITE");
        configManager.setConfiguration(group, "overrideSky", "true");

        hdConfigApplied = true;
        log.debug("Applied 117 HD config overrides for Bloat");
    }

    private void restoreHdConfig() {
        final String group = "hd";
        // Restore original hd config
        if (originalHdSkyColorConfig != null)
        {
            configManager.setConfiguration(group, "defaultSkyColor", originalHdSkyColorConfig);
        }
        if (originalHdSkyConfig != null)
        {
            configManager.setConfiguration(group, "overrideSky", originalHdSkyConfig);
        }

        hdConfigApplied = false;
        originalHdSkyColorConfig = null;
        originalHdSkyConfig = null;
        log.debug("Restored original 117 HD config");
    }

    public void onRoomExit()
    {
        restoreHdConfig();
        unhideGroundObjects();
    }

    public void startUp()
    {
        hideBloatFloor = config.hideBloatFloor();
        bloatSkyboxColor = config.bloatSkyboxColor().getRGB();
        bloatSkyboxOverride = config.enableBloatSkyboxOverride();
        if (client.getGameState() == GameState.LOGGED_IN)
        {
            updateGroundObjects();
            updateHdConfig();
        }
    }

    public void shutDown()
    {
        onRoomExit();
    }
}
