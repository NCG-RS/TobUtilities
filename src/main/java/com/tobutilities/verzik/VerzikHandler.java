package com.tobutilities.verzik;

import com.tobutilities.common.RoomHandler;


import com.tobutilities.common.player.TobPlayerOrb;
import com.tobutilities.TobUtilitiesPlugin;
import com.tobutilities.TobUtilitiesConfig;

import static com.tobutilities.verzik.VerzikConstants.VERZIK_NAME;
import java.awt.event.KeyEvent;
import javax.inject.Inject;

import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Renderable;
import net.runelite.api.events.GameTick;
import static net.runelite.api.kit.KitType.WEAPON;
import net.runelite.client.eventbus.Subscribe;


@Slf4j
@Singleton
public class VerzikHandler extends RoomHandler

{
	@Getter
	private TobPlayerOrb tobPlayerOrb = TobPlayerOrb.UNKNOWN;
	private boolean isVerzikHidden = false;


	@Inject
	protected VerzikHandler(TobUtilitiesPlugin plugin, TobUtilitiesConfig config, Client client)
	{
		super(plugin, config, client);
	}


	@Subscribe
	public void onGameTick(GameTick tick)
	{
		if (config.enableDawnbringerOverlay())
		{
			String dawnbringerHolderName = removeNonAlphanumeric(getDawnbringerHolderName());
			if (dawnbringerHolderName != null)
			{
				for (TobPlayerOrb tobPlayer : TobPlayerOrb.values())
				{
					String playerName = removeNonAlphanumeric(client.getVarcStrValue(tobPlayer.getNameVarc()));
					if (playerName.equals(dawnbringerHolderName))
					{
						tobPlayerOrb = tobPlayer;
						return;
					}
				}
			}
			tobPlayerOrb = TobPlayerOrb.UNKNOWN;
		}
	}


	private String getDawnbringerHolderName()
	{

		for (Player player : client.getWorldView(-1).players())
		{
			if (player == null || player.getName() == null)
			{
				log.info("Invalid/Null player");
				continue; // Skip null or invalid players
			}
			int weaponId = player.getPlayerComposition().getEquipmentId(WEAPON);
			if (weaponId == ItemID.DAWNBRINGER)
			{
				return player.getName(); // Stop once we find a match
			}
		}
		return null;
	}

	private static String removeNonAlphanumeric(String str)
	{
		if (str == null)
		{
			return null;
		}
		str = str.replaceAll(
			"[^a-zA-Z0-9]", "");
		return str;
	}

	public void keyPressed(KeyEvent e)
	{
		if (config.hideVerzikHotkey().matches(e))
		{
			if (config.enableHideVerzikHmt())
			{
				isVerzikHidden = !isVerzikHidden;
				e.consume();
			}
		}
	}

	public boolean shouldDraw(Renderable renderable, boolean drawingUI)
	{
		/*
			Verzik P3 - hide when enabled and hotkey pressed
	 	*/
		//Notes : may have to make this generic entity hider not verzik specific. - May change to using ID instead
		//Either way - make it so with hotkey press can hide verzik so you don't encounter situation where you cant see yellows in p3 hmt
		if (renderable instanceof NPC)
		{
			NPC npc = (NPC) renderable;
			if (VERZIK_NAME.equals(npc.getName()) && isVerzikHidden && config.enableHideVerzikHmt())
			{
				return false;
			}
		}
		return true;
	}

}
