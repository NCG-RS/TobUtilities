package com.tobutilities;

import com.tobutilities.common.enums.FontType;
import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("tobutilities")
public interface TobUtilitiesConfig extends Config
{
	@ConfigSection(
		name = "Metronome",
		description = "Config for visual Metronome",
		position = 6
)
	String Metronome = "Metronome";
	@ConfigItem(
			position = 1,
			keyName = "enableMetronome",
			name = "Visual Metronome",
			description = "Enable visual metronome",
			section = Metronome
	)
	default boolean enableMetronome()
	{
		return true;
	}
	@ConfigItem(
		position = 2,
		keyName = "enableNyloMetronome",
		name = "Enable Nylo Metronome",
		description = "Enable visual metronome in Nylo room - Enter on 0, waves spawn on 1",
		section = Metronome
	)
	default boolean enableNyloMetronome()
	{
		return true;
	}
	@ConfigItem(
		position = 3,
		keyName = "enableSoteMetronome",
		name = "Visual Sote Metronome",
		description = "Enable visual metronome in Sotetseg room",
		section = Metronome
	)
	default boolean enableSoteMetronome()
	{
		return true;
	}
	@ConfigItem(
		position = 4,
		keyName = "enableXarpusMetronome",
		name = "Visual Xarpus Metronome",
		description = "Enable visual metronome in Xarpus room",
		section = Metronome
	)
	default boolean enableXarpusMetronome()
	{
		return true;
	}
	@ConfigItem(
		position = 5,
		keyName = "enableVerzikMetronome",
		name = "Visual Verik Metronome",
		description = "Enable visual metronome in Verzik room",
		section = Metronome
	)
	default boolean enableVerzikMetronome()
	{
		return true;
	}

	@ConfigItem(
			position = 6,
			keyName = "countColor",
			name = "Tick Number Color",
			description = "Configures the color of tick number",
			section = Metronome
	)
	default Color NumberColor()
	{
		return Color.CYAN;
	}
	@ConfigItem(
		position = 7,
		keyName = "metronomeBorderWidth",
		name = "MetronomeBorderWidth",
		description = "Configures the width of the colored metronome border",
		section = Metronome
	)
	default int metronomeBorderWidth()
	{
		return 2;
	}


	@ConfigItem(
			position = 7,
			keyName = "fontType",
			name = "Font Type",
			description = "Change the font of the Tick Number",
			section = Metronome
	)
	default FontType fontType() { return FontType.REGULAR; }

	@Alpha
	@ConfigItem(
			position = 8,
			keyName = "tick1Color",
			name = "1st Tick Color",
			description = "Configures the color of 1st tick",
			section = Metronome
	)
	default Color getTick1Color()
	{
		return Color.WHITE;
	}

	@Alpha
	@ConfigItem(
			position = 9,
			keyName = "tick2Color",
			name = "2nd Tick Color",
			description = "Configures the color of 2nd tick",
			section = Metronome
	)
	default Color getTick2Color()
	{
		return Color.GRAY;
	}

	@Alpha
	@ConfigItem(
			position = 10,
			keyName = "tick3Color",
			name = "3rd Tick Color",
			description = "Configures the color of 3rd tick if enabled",
			section = Metronome
	)
	default Color getTick3Color()
	{
		return Color.DARK_GRAY;
	}
	@Alpha
	@ConfigItem(
			position = 11,
			keyName = "tick4Color",
			name = "4th Tick Color",
			description = "Configures the color of the 4rd tick if enabled",
			section = Metronome
	)
	default Color getTick4Color()
	{
		return Color.BLACK;
	}
	@Alpha
	@ConfigItem(
			position = 12,
			keyName = "tick5Color",
			name = "5th Tick Color",
			description = "Configures the color of the 5th tick if enabled",
			section = Metronome
	)
	default Color getTick5Color()
	{
		return new Color(112, 131, 255);
	}
	@Alpha
	@ConfigItem(
			position = 13,
			keyName = "tick6Color",
			name = "6th Tick Color",
			description = "Configures the color of the 6th tick if enabled",
			section = Metronome
	)
	default Color getTick6Color()
	{
		return new Color(0, 23, 171);
	}
	@Alpha
	@ConfigItem(
			position = 14,
			keyName = "tick7Color",
			name = "7th Tick Color",
			description = "Configures the color of the 7th tick if enabled",
			section = Metronome
	)
	default Color getTick7Color()
	{
		return new Color(107, 255, 124);
	}

	@ConfigSection(
		name = "Maiden",
		description = "Change Maiden settings",
		position = 1
	)
	String Maiden = "Maiden Settings";

	@ConfigItem(
		position = 0,
		keyName = "maidenScuffWarning",
		name = "Maiden Scuff Warning",
		description = "Sends a message with Maidens index, lets you know if you should expect scuffed nylos",
		section = Maiden
	)
	default boolean enableMaidenIndexMessage()
	{
		return true;
	}
	@ConfigItem(
		position = 1,
		keyName = "scuffedSpawnHighlight",
		name = "Highlight scuffed spawns",
		description = "Highlights scuffed nylocas matomenos ",
		section = Maiden
	)
	default boolean enabledScuffedSpawnHighlight()
	{
		return false;
	}
	@ConfigItem(
		position = 2,
		keyName = "highlightColor",
		name = "Highlight Color",
		description = "The color to highlight Nylocas Matomenos",
		section = Maiden
	)
	default Color getHighlightColor()
	{
		return new Color(0, 0, 0, 255);
	}

	@ConfigItem(
		position = 3,
		keyName = "borderWidth",
		name = "Border Width",
		description = "Width of the highlighted border",
		section = Maiden
	)
	default int getBorderWidth()
	{
		return 2;
	}

	@ConfigItem(
		position = 4,
		keyName = "borderFeather",
		name = "Border Feather",
		description = "Feather of the highlighted border",
		section = Maiden
	)
	default int getBorderFeather()
	{
		return 2;
	}


	@ConfigSection(
		name = "Bloat",
		description = "Change Bloat settings",
		position = 2
	)
	String Bloat = "Bloat Settings";

	@ConfigItem(
		keyName = "hideSelfDuringBloat",
		name = "Hide self",
		description = "Enable hiding of local player while bloat is alive",
		position = 1,
		section = Bloat
	)
	default boolean hideLocalPlayerDuringBloat()
	{
		return true;
	}

	@ConfigItem(
		keyName = "hideOthersDuringBloat",
		name = "Hide others",
		description = "Enable hiding of other players while bloat is alive",
		position = 2,
		section = Bloat
	)
	default boolean hideOtherPlayersDuringBloat()
	{
		return true;
	}

	@ConfigSection(
		name = "Nylocas",
		description = "Change Nylocas settings",
		position = 3
	)
	String Nylocas = "Nylocas Settings";

	@ConfigItem(
		keyName = "enableHighlightAggressiveNylos",
		name = "Highlight aggressive nylos",
		description = "Enables highlighting aggressive nylos",
		position = 1,
		section = Nylocas
	)
	default boolean enableHighlightAggressiveNylos()
	{
		return false;
	}

	@ConfigSection(
		name = "Xarpus",
		description = "Change Xarpus settings",
		position = 4
	)
	String Xarpus = "Xarpus Settings";

	@ConfigItem(
		keyName = "enableHighlightExhumes",
		name = "Highlight exhumes",
		description = "Enables highlighting exhumes",
		position = 1,
		section = Xarpus
	)
	default boolean enableHighlightExhumes()
	{
		return false;
	}

	@ConfigSection(
		name = "Verzik",
		description = "Change Verzik settings",
		position = 5
	)
	String Verzik = "Verzik Settings";
	@ConfigItem(
		keyName = "enableHideVerzikHotkey",
		name = "Enable hide Verzik ",
		description = "Enable ability to hide P3 Verzik using a hotkey ",
		position = 1,
		section = Verzik
	)
	default boolean enableHideVerzikHmt()
	{
		return true;
	}

	@ConfigItem(
		keyName = "hideVerzikHotkey",
		name = "Hide Verzik Hotkey",
		description = "Key to hide verzik ",
		position = 2,
		section = Verzik
	)
	default Keybind hideVerzikHotkey()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "enableDawnbringerOverlay",
		name = "Enable dawnbringer overlay ",
		description = "Enable overlay over player orb to show who is currently holding dawnbringer",
		position = 3,
		section = Verzik
	)
	default boolean enableDawnbringerOverlay()
	{
		return true;
	}

	@ConfigItem(
		keyName = "dawnbringerOverlayColor",
		name = "Dawnbringer overlay color",
		description = "Color for dawnbringer holders orb",
		position = 4,
		section = Verzik
	)
	default Color dawnbringerOverlayColor()
	{
		return new Color(0, 82, 87);
	}

}

