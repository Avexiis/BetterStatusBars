/*
 * Copyright (c) 2019, Jos <Malevolentdev@gmail.com>
 * Copyright (c) 2019, Rheon <https://github.com/Rheon-D>
 * Copyright (c) 2026, Xeon <https://github.com/Avexiis>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *	list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *	this list of conditions and the following disclaimer in the documentation
 *	and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.moreSB;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.components.TextComponent;

class BarRenderer
{
	static final int DISABLED_FLASH_THRESHOLD = -1;
	private static final int SKILL_ICON_HEIGHT = 35;
	private static final int COUNTER_ICON_HEIGHT = 18;
	private static final int BORDER_SIZE = 1;
	private static final int MIN_ICON_AND_COUNTER_WIDTH = 16;
	private static final int UNSET_TICK = Integer.MIN_VALUE;

	static final int DEFAULT_WIDTH = 20;
	static final int MIN_WIDTH = 3;
	static final int MAX_WIDTH = 40;

	private final Supplier<Integer> maxValueSupplier;
	private final Supplier<Integer> currentValueSupplier;
	private final Supplier<Integer> healSupplier;
	private final Supplier<Color> colorSupplier;
	private final Supplier<Color> healColorSupplier;
	private final Supplier<Image> iconSupplier;
	private final Supplier<Integer> flashThresholdSupplier;
	private final IntSupplier tickCountSupplier;

	private int maxValue;
	private int currentValue;
	private int thresholdFlashStartTick = UNSET_TICK;

	BarRenderer(
		Supplier<Integer> maxValueSupplier,
		Supplier<Integer> currentValueSupplier,
		Supplier<Integer> healSupplier,
		Supplier<Color> colorSupplier,
		Supplier<Color> healColorSupplier,
		Supplier<Image> iconSupplier,
		Supplier<Integer> flashThresholdSupplier,
		IntSupplier tickCountSupplier
	)
	{
		this.maxValueSupplier = maxValueSupplier;
		this.currentValueSupplier = currentValueSupplier;
		this.healSupplier = healSupplier;
		this.colorSupplier = colorSupplier;
		this.healColorSupplier = healColorSupplier;
		this.iconSupplier = iconSupplier;
		this.flashThresholdSupplier = flashThresholdSupplier;
		this.tickCountSupplier = tickCountSupplier;
	}

	private void refreshSkills()
	{
		maxValue = maxValueSupplier.get();
		currentValue = currentValueSupplier.get();
	}

	void onGameTick(MoreStatusBarsConfig config)
	{
		refreshSkills();
		updateThresholdFlash(config);
	}

	void resetFlashState()
	{
		thresholdFlashStartTick = UNSET_TICK;
	}

	void renderBar(MoreStatusBarsConfig config, Graphics2D graphics, int x, int y, int width, int height)
	{
		refreshSkills();
		updateThresholdFlash(config);

		final int filledHeight = getBarHeight(maxValue, currentValue, height);
		final Color fill = colorSupplier.get();

		graphics.setColor(config.backgroundColor());
		graphics.drawRect(x, y, width - BORDER_SIZE, height - BORDER_SIZE);
		graphics.fillRect(x, y, width, height);

		graphics.setColor(fill);
		graphics.fillRect(
			x + BORDER_SIZE,
			y + BORDER_SIZE + (height - filledHeight),
			width - BORDER_SIZE * 2,
			filledHeight - BORDER_SIZE * 2
		);

		if (config.enableRestorationBars())
		{
			renderRestore(config, graphics, x, y, width, height);
		}

		if (isThresholdFlashActive(config))
		{
			graphics.setColor(config.thresholdFlashColor());
			graphics.fillRect(x, y, width, height);
		}

		if (config.enableSkillIcon() || config.enableCounter())
		{
			renderIconsAndCounters(config, graphics, x, y, width);
		}
	}

	private void renderIconsAndCounters(MoreStatusBarsConfig config, Graphics2D graphics, int x, int y, int width)
	{
		if (width < MIN_ICON_AND_COUNTER_WIDTH)
		{
			return;
		}

		final boolean skillIconEnabled = config.enableSkillIcon();

		if (skillIconEnabled)
		{
			final Image icon = iconSupplier.get();
			if (icon != null)
			{
				final int xDraw = x + (width / 2) - (icon.getWidth(null) / 2);
				graphics.drawImage(icon, xDraw, y + 4, null);
			}
		}

		if (config.enableCounter())
		{
			graphics.setFont(FontManager.getRunescapeSmallFont());

			final String counterText = Integer.toString(currentValue);
			final int widthOfCounter = graphics.getFontMetrics().stringWidth(counterText);
			final int centerText = (width / 2) - (widthOfCounter / 2);
			final int yOffset = skillIconEnabled ? SKILL_ICON_HEIGHT : COUNTER_ICON_HEIGHT;

			final TextComponent textComponent = new TextComponent();
			textComponent.setText(counterText);
			textComponent.setPosition(new Point(x + centerText, y + yOffset));
			textComponent.render(graphics);
		}
	}

	private void updateThresholdFlash(MoreStatusBarsConfig config)
	{
		final int flashTicks = getThresholdFlashTicks(config);
		final Integer flashThreshold = flashThresholdSupplier.get();
		if (flashTicks <= 0 || flashThreshold == null || flashThreshold < 0 || currentValue > flashThreshold)
		{
			thresholdFlashStartTick = UNSET_TICK;
			return;
		}

		if (thresholdFlashStartTick == UNSET_TICK)
		{
			thresholdFlashStartTick = tickCountSupplier.getAsInt();
		}
	}

	private boolean isThresholdFlashActive(MoreStatusBarsConfig config)
	{
		final int flashTicks = getThresholdFlashTicks(config);
		if (flashTicks <= 0 || thresholdFlashStartTick == UNSET_TICK)
		{
			return false;
		}

		final int elapsedTicks = Math.max(0, tickCountSupplier.getAsInt() - thresholdFlashStartTick);
		final int flashCycleTicks = flashTicks + 1;
		return elapsedTicks % flashCycleTicks < flashTicks;
	}

	private static int getThresholdFlashTicks(MoreStatusBarsConfig config)
	{
		final MoreStatusBarsConfig.FlashDuration flashDuration = config.thresholdFlashDuration();
		return flashDuration == null ? 0 : flashDuration.getTicks();
	}

	private void renderRestore(MoreStatusBarsConfig config, Graphics2D graphics, int x, int y, int width, int height)
	{
		final int heal = healSupplier.get();
		if (heal <= 0)
		{
			return;
		}

		final int filledCurrentHeight = getBarHeight(maxValue, currentValue, height);
		final int filledHealHeight = getBarHeight(maxValue, heal, height);

		final int fillY;
		final int fillHeight;

		final Color color = healColorSupplier.get();
		if (color == null)
		{
			return;
		}

		if (filledHealHeight + filledCurrentHeight > height)
		{
			graphics.setColor(config.overhealColor());
			fillY = y + BORDER_SIZE;
			fillHeight = height - filledCurrentHeight - BORDER_SIZE;
		}
		else
		{
			graphics.setColor(color);
			fillY = y + BORDER_SIZE + height - (filledCurrentHeight + filledHealHeight);
			fillHeight = filledHealHeight;
		}

		graphics.fillRect(
			x + BORDER_SIZE,
			fillY,
			width - BORDER_SIZE * 2,
			fillHeight
		);
	}

	private static int getBarHeight(int base, int current, int size)
	{
		final double ratio = (double) current / (double) base;

		if (ratio >= 1.0d)
		{
			return size;
		}

		return (int) Math.round(ratio * size);
	}
}
