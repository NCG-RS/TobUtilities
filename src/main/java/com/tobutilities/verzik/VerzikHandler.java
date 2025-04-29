package com.tobutilities.verzik;

import com.tobutilities.common.RoomHandler;


import com.tobutilities.common.player.PlayerOrb;
import com.tobutilities.common.player.TobPlayerOrb;
import com.tobutilities.TobUtilitiesPlugin;
import com.tobutilities.TobUtilitiesConfig;

import static com.tobutilities.verzik.VerzikConstants.VERZIK_NAME;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.inject.Inject;

import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Renderable;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import static net.runelite.api.kit.KitType.WEAPON;

import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.party.PartyMember;
import net.runelite.client.party.PartyService;
import net.runelite.client.party.WSClient;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.StringUtils;


@Slf4j
@Singleton
public class VerzikHandler extends RoomHandler

{
	@Getter
	private TobPlayerOrb tobPlayerOrb = TobPlayerOrb.UNKNOWN;
	@Getter
	private DawnbringerStatus dawnbringerStatus = DawnbringerStatus.UNKNOWN;
	private boolean isVerzikHidden = false;

	private static final int DAWNBRINGER_ID = ItemID.BRONZE_AXE;

	@Inject
	private PartyService partyService;
	@Inject
	private WSClient wsClient;


	// Track which party members have Dawnbringer
	private final Map<String, Boolean> memberDawnbringerStatus = new HashMap<>();



	@Inject
	protected VerzikHandler(TobUtilitiesPlugin plugin, TobUtilitiesConfig config, Client client)
	{
		super(plugin, config, client);
	}


	@Subscribe
	public void onGameTick(GameTick tick)
	{
		if (!config.enableDawnbringerOverlay()) {
			return;
		}

		if (config.enableDawnbringerParty()) {
			handleUnequippedDawnBringer();

			// Only check equipped items if Dawnbringer wasn't found in inventory
			if (TobPlayerOrb.UNKNOWN.equals(tobPlayerOrb)) {
				handleEquippedDawnBringer();
			}
		} else {
			handleEquippedDawnBringer();
		}
	}

	private void handleEquippedDawnBringer()
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
					dawnbringerStatus = DawnbringerStatus.EQUIPPED;
					return;
				}
			}
		}
		tobPlayerOrb = TobPlayerOrb.UNKNOWN;
	}

	private void handleUnequippedDawnBringer()
	{
		String dawnbringerHolderName = removeNonAlphanumeric(getPlayerWithDawnbringer());
		if (dawnbringerHolderName != null)
		{
			for (TobPlayerOrb tobPlayer : TobPlayerOrb.values())
			{
				String playerName = removeNonAlphanumeric(client.getVarcStrValue(tobPlayer.getNameVarc()));
				if (playerName.equals(dawnbringerHolderName))
				{
					tobPlayerOrb = tobPlayer;
					dawnbringerStatus = DawnbringerStatus.IN_INVENTORY;
					return;
				}
			}
		}

		handleEquippedDawnBringer();
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
		if (config.hideVerzikHotkey().matches(e) && config.enableHideVerzik())
			{
				isVerzikHidden = !isVerzikHidden;
				e.consume();
			}

	}

	public boolean shouldDraw(Renderable renderable, boolean drawingUI)
	{
		if (renderable instanceof NPC)
		{
			NPC npc = (NPC) renderable;
			return !VERZIK_NAME.equals(npc.getName()) || !isVerzikHidden || !config.enableHideVerzik();
		}
		return true;
	}

	/**
	 * Gets the name of the player holding the Dawnbringer
	 *
	 * @return The name of the player with Dawnbringer, or null if no one has it
	 */
	@Nullable
	public String getPlayerWithDawnbringer()
	{
		return memberDawnbringerStatus.entrySet().stream()
			.filter(Map.Entry::getValue)
			.findFirst()
			.map(Map.Entry::getKey).orElse(null);
	}



	public void startUp()
	{
		wsClient.registerMessage(DawnbringerStatusMessage.class);
		// Clear status map
		memberDawnbringerStatus.clear();
		// Send initial inventory state
		checkInventoryForDawnbringer();
	}


	public void shutDown()
	{
		memberDawnbringerStatus.clear();
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		log.info("ITEM CONTAINER CHANGED");
		// Only check inventory changes
		if (event.getContainerId() == InventoryID.INVENTORY.getId())
		{
			checkInventoryForDawnbringer();
		}
	}

	private void checkInventoryForDawnbringer()
	{
		// Make sure we're in a party
		if (!partyService.isInParty())
		{
			return;
		}

		// Check if we have Dawnbringer in inventory
		boolean hasDawnbringer = false;
		ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);

		if (inventory != null)
		{
			hasDawnbringer = inventory.contains(DAWNBRINGER_ID);
			if (hasDawnbringer){
				log.info("Dawnbringer in local players inventory, sending info to party");
			}
		}
		String localPlayerName = client.getLocalPlayer().getName();
		// Store our status
		memberDawnbringerStatus.put(localPlayerName, hasDawnbringer);

		// Send status to party
		partyService.send(new DawnbringerStatusMessage(localPlayerName, hasDawnbringer));

	}

	/**
	 * Handles incoming Dawnbringer status messages from party members
	 */
	@Subscribe
	public void onDawnbringerStatusMessage(DawnbringerStatusMessage message)
	{
		log.info("Message received: {}", message);
		if (client.getLocalPlayer() != null && StringUtils.isNotBlank(client.getLocalPlayer().getName()) && client.getLocalPlayer().getName().equals(message.getPlayerName()))
		{
			return;
		}
		// Update status map
		memberDawnbringerStatus.put(message.getPlayerName(), message.isHasDawnbringer());
	}


}
