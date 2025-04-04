package com.tobutilities.common.player;

import lombok.Getter;

@Getter
public enum TobPlayerOrb
{

	PLAYER_1(1, 330, 1835026, 1835027),
	PLAYER_2(2, 331, 1835030,  1835031),
	PLAYER_3(3, 332, 1835034 , 1835035),
	PLAYER_4(4, 333, 1835038, 1835039),
	PLAYER_5(5, 334, 1835042, 1835043),
	UNKNOWN(0,0,0,0);

	private final int orbPosition;

	/**
	 * the Varc ID which contains the name of the player the orb belongs to
	 */
	private final int nameVarc;

	private final int orbId;

	private final int orbBackgroundId;


	TobPlayerOrb(int orbPosition, int nameVarc, int orbId, int orbBackgroundId)
	{
		this.orbPosition = orbPosition;
		this.nameVarc = nameVarc;
		this.orbId = orbId;
		this.orbBackgroundId = orbBackgroundId;
	}

	@Override
	public String toString()
	{
		return "TobPlayerOrb{" +
			"orbPosition=" + orbPosition +
			", nameVarc=" + nameVarc +
			", orbId=" + orbId +
			", orbBackgroundId=" + orbBackgroundId +
			'}';
	}
}
