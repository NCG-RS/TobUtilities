package com.tobutilities.common.util;

import com.tobutilities.common.enums.Region;

public class CommonUtils
{

	public static Region getRegionByRegionId(int regionId)
	{
		switch (regionId)
		{
			case 12613:
				return Region.MAIDEN;
			case 13125:
				return Region.BLOAT;
			case 13122:
				return Region.NYLOCAS;
			case 13123:
			case 13379:
				return Region.SOTETSEG;
			case 12612:
				return Region.XARPUS;
			case 12611:
				return Region.VERZIK;
			default:
				return Region.UNKNOWN;
		}
	}
}
