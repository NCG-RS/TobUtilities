package com.tobutilities.common.metronome;

import com.tobutilities.TobUtilitiesConfig;
import com.tobutilities.common.enums.FontType;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import java.awt.*;
@Slf4j
public class MetronomeOverlay extends Overlay
{
private final MetronomeService metronomeService;
	private final TobUtilitiesConfig config;


	private static int TITLE_PADDING = 10;
	private static final int MINIMUM_SIZE = 16;
	private Dimension DEFAULT_SIZE = new Dimension(25, 25);

	@Inject
	protected MetronomeOverlay(MetronomeService metronomeService, TobUtilitiesConfig config)
	{
		this.metronomeService = metronomeService;
		this.config = config;
		setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
		setMinimumSize(MINIMUM_SIZE);
		setResizable(true);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		Dimension preferredSize = getPreferredSize();
		if (preferredSize == null) {
			preferredSize = DEFAULT_SIZE;
			setPreferredSize(preferredSize);
		}

		if (metronomeService.isMetronomeDisplayed() && metronomeService.isCurrentRegionMetronomeEnabled()) {
			// Background
			graphics.setColor(new Color(0, 0, 0, 155)); // Semi-transparent black
			graphics.fillRect(0, 0, preferredSize.width, preferredSize.height);

			// Draw border
			graphics.setColor(metronomeService.getCurrentColor());
			graphics.setStroke(new BasicStroke(config.metronomeBorderWidth()));
			graphics.drawRect(0, 0, preferredSize.width - 1, preferredSize.height - 1);


			TITLE_PADDING = (Math.min(preferredSize.width, preferredSize.height) / 2 - 4);

			if (config.fontType() == FontType.REGULAR) {
				graphics.setFont(new Font(FontManager.getRunescapeFont().getName(), Font.BOLD,
					Math.min(preferredSize.width, preferredSize.height) / 2));
			} else {
				graphics.setFont(new Font(config.fontType().toString(), Font.BOLD,
					Math.min(preferredSize.width, preferredSize.height) / 2));
			}

			String tickText = String.valueOf(metronomeService.getCurrentColorIndex());
			FontMetrics fm = graphics.getFontMetrics();
			int textWidth = fm.stringWidth(tickText);

			final Point tickCounterPoint = new Point(
				preferredSize.width / 2 - textWidth / 2,
				preferredSize.height / 2 + fm.getAscent() / 2
			);

			// Draw text with a subtle shadow for better visibility
			OverlayUtil.renderTextLocation(graphics,
				new Point(tickCounterPoint.getX() + 1, tickCounterPoint.getY() + 1),
				tickText, Color.BLACK);
			OverlayUtil.renderTextLocation(graphics, tickCounterPoint, tickText, config.NumberColor());
		}
		return preferredSize;
	}
}