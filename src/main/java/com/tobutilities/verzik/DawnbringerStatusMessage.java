package com.tobutilities.verzik;

import lombok.EqualsAndHashCode;
import lombok.Value;
import net.runelite.client.party.messages.PartyMessage;

/**
 * Message sent between party members to share Dawnbringer status
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class DawnbringerStatusMessage extends PartyMessage
{
	String playerName;
	DawnbringerStatus dawnbringerStatus;


}