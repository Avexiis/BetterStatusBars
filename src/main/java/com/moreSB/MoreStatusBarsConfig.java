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
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup(MoreStatusBarsConfig.GROUP)
public interface MoreStatusBarsConfig extends Config
{
	String GROUP = "morestatusbars";
	int MAX_FLASH_THRESHOLD = 100;

	@ConfigSection(
		name = "Colors",
		description = "Configures status bar colors.",
		position = 100,
		closedByDefault = true
	)
	String colorsSection = "colors";

	@ConfigSection(
		name = "Warning flashes",
		description = "Configures status bar warning flashes.",
		position = 200,
		closedByDefault = true
	)
	String thresholdFlashSection = "thresholdFlash";

	@ConfigItem(
		keyName = "enableCounter",
		name = "Show counters",
		description = "Shows current value of the status on the bar."
	)
	default boolean enableCounter()
	{
		return false;
	}

	@ConfigItem(
		keyName = "enableSkillIcon",
		name = "Show icons",
		description = "Adds skill icons at the top of the bars."
	)
	default boolean enableSkillIcon()
	{
		return true;
	}

	@ConfigItem(
		keyName = "enableRestorationBars",
		name = "Show restores",
		description = "Visually shows how much will be restored to your status bar."
	)
	default boolean enableRestorationBars()
	{
		return true;
	}

	@ConfigItem(
		keyName = "leftBarMode",
		name = "Bar #1",
		description = "Configures the primary left status bar."
	)
	default BarMode leftBarMode()
	{
		return BarMode.HITPOINTS;
	}

	@ConfigItem(
		keyName = "rightBarMode",
		name = "Bar #2",
		description = "Configures the primary right status bar."
	)
	default BarMode rightBarMode()
	{
		return BarMode.PRAYER;
	}

	@ConfigItem(
		keyName = "leftSecondaryBarMode",
		name = "Bar #3",
		description = "Configures the secondary left status bar."
	)
	default BarMode leftSecondaryBarMode()
	{
		return BarMode.DISABLED;
	}

	@ConfigItem(
		keyName = "rightSecondaryBarMode",
		name = "Bar #4",
		description = "Configures the secondary right status bar."
	)
	default BarMode rightSecondaryBarMode()
	{
		return BarMode.DISABLED;
	}

	@ConfigItem(
		keyName = "hideAfterCombatDelay",
		name = "Hide after combat delay",
		description = "Amount of ticks before hiding status bars after no longer in combat. 0 = always show status bars."
	)
	@Units(Units.TICKS)
	default int hideAfterCombatDelay()
	{
		return 0;
	}

	@Range(
		min = BarRenderer.MIN_WIDTH,
		max = BarRenderer.MAX_WIDTH
	)
	@ConfigItem(
		keyName = "barWidth",
		name = "Bar width",
		description = "The width of the status bars in the modern/resizable mode."
	)
	default int barWidth()
	{
		return BarRenderer.DEFAULT_WIDTH;
	}

	@ConfigItem(
		keyName = "stackBarsInModern",
		name = "Stack bars in modern mode",
		description = "When enabled, modern/resizable mode stacks bars on top of one another, like classic mode."
	)
	default boolean stackBarsInModern()
	{
		return false;
	}

	@Range(
		min = 0,
		max = 20
	)
	@ConfigItem(
		keyName = "modernBarGap",
		name = "Modern bar spacing",
		description = "Horizontal spacing between bars in modern/resizable mode."
	)
	default int modernBarGap()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
		keyName = "backgroundColor",
		name = "Background",
		description = "Configures the status bar background color.",
		position = 0,
		section = colorsSection
	)
	default Color backgroundColor()
	{
		return new Color(0, 0, 0, 150);
	}

	@Alpha
	@ConfigItem(
		keyName = "hitpointsColor",
		name = "Hitpoints",
		description = "Configures the normal hitpoints bar color.",
		position = 1,
		section = colorsSection
	)
	default Color hitpointsColor()
	{
		return new Color(225, 35, 0, 125);
	}

	@Alpha
	@ConfigItem(
		keyName = "poisonedColor",
		name = "Poisoned",
		description = "Configures the hitpoints bar color while poisoned.",
		position = 2,
		section = colorsSection
	)
	default Color poisonedColor()
	{
		return new Color(0, 145, 0, 150);
	}

	@Alpha
	@ConfigItem(
		keyName = "venomedColor",
		name = "Venomed",
		description = "Configures the hitpoints bar color while venomed.",
		position = 3,
		section = colorsSection
	)
	default Color venomedColor()
	{
		return new Color(0, 65, 0, 150);
	}

	@Alpha
	@ConfigItem(
		keyName = "diseaseColor",
		name = "Disease",
		description = "Configures the hitpoints bar color while diseased.",
		position = 4,
		section = colorsSection
	)
	default Color diseaseColor()
	{
		return new Color(255, 193, 75, 181);
	}

	@Alpha
	@ConfigItem(
		keyName = "parasiteColor",
		name = "Parasite",
		description = "Configures the hitpoints bar color while infected by a parasite.",
		position = 5,
		section = colorsSection
	)
	default Color parasiteColor()
	{
		return new Color(196, 62, 109, 181);
	}

	@Alpha
	@ConfigItem(
		keyName = "hitpointsRestoreColor",
		name = "Hitpoints restore",
		description = "Configures the hitpoints restore color shown while hovering items.",
		position = 6,
		section = colorsSection
	)
	default Color hitpointsRestoreColor()
	{
		return new Color(255, 112, 6, 150);
	}

	@Alpha
	@ConfigItem(
		keyName = "prayerColor",
		name = "Prayer",
		description = "Configures the normal prayer bar color.",
		position = 7,
		section = colorsSection
	)
	default Color prayerColor()
	{
		return new Color(50, 200, 200, 175);
	}

	@Alpha
	@ConfigItem(
		keyName = "activePrayerColor",
		name = "Active prayer",
		description = "Configures the prayer bar color while a prayer is active.",
		position = 8,
		section = colorsSection
	)
	default Color activePrayerColor()
	{
		return new Color(57, 255, 186, 225);
	}

	@Alpha
	@ConfigItem(
		keyName = "prayerRestoreColor",
		name = "Prayer restore",
		description = "Configures the prayer restore color shown while hovering items.",
		position = 9,
		section = colorsSection
	)
	default Color prayerRestoreColor()
	{
		return new Color(57, 255, 186, 75);
	}

	@Alpha
	@ConfigItem(
		keyName = "runEnergyColor",
		name = "Run energy",
		description = "Configures the normal run energy bar color.",
		position = 10,
		section = colorsSection
	)
	default Color runEnergyColor()
	{
		return new Color(199, 174, 0, 220);
	}

	@Alpha
	@ConfigItem(
		keyName = "staminaColor",
		name = "Stamina",
		description = "Configures the run energy bar color while stamina is active.",
		position = 11,
		section = colorsSection
	)
	default Color staminaColor()
	{
		return new Color(160, 124, 72, 255);
	}

	@Alpha
	@ConfigItem(
		keyName = "runEnergyRestoreColor",
		name = "Run restore",
		description = "Configures the run energy restore color shown while hovering items.",
		position = 12,
		section = colorsSection
	)
	default Color runEnergyRestoreColor()
	{
		return new Color(199, 118, 0, 218);
	}

	@Alpha
	@ConfigItem(
		keyName = "specialAttackColor",
		name = "Special attack",
		description = "Configures the special attack bar color.",
		position = 13,
		section = colorsSection
	)
	default Color specialAttackColor()
	{
		return new Color(3, 153, 0, 195);
	}

	@Alpha
	@ConfigItem(
		keyName = "warmthColor",
		name = "Warmth",
		description = "Configures the warmth bar color.",
		position = 14,
		section = colorsSection
	)
	default Color warmthColor()
	{
		return new Color(244, 97, 0, 255);
	}

	@Alpha
	@ConfigItem(
		keyName = "overhealColor",
		name = "Overrestore",
		description = "Configures the color shown when a hovered item would restore past the maximum.",
		position = 15,
		section = colorsSection
	)
	default Color overhealColor()
	{
		return new Color(216, 255, 139, 150);
	}

	@ConfigItem(
		keyName = "thresholdFlashDuration",
		name = "Flash duration",
		description = "Darkens hitpoints, prayer, and run energy bars when they drop to or below their configured thresholds.",
		position = 0,
		section = thresholdFlashSection
	)
	default FlashDuration thresholdFlashDuration()
	{
		return FlashDuration.DISABLED;
	}

	@Alpha
	@ConfigItem(
		keyName = "thresholdFlashColor",
		name = "Flash color",
		description = "Configures the color used to darken bars during a threshold flash.",
		position = 1,
		section = thresholdFlashSection
	)
	default Color thresholdFlashColor()
	{
		return new Color(0, 0, 0, 120);
	}

	@Range(
		min = 0,
		max = MAX_FLASH_THRESHOLD
	)
	@ConfigItem(
		keyName = "hitpointsFlashThreshold",
		name = "Hitpoints threshold",
		description = "Flashes the hitpoints bar when hitpoints drop to or below this value.",
		position = 2,
		section = thresholdFlashSection
	)
	default int hitpointsFlashThreshold()
	{
		return 50;
	}

	@Range(
		min = 0,
		max = MAX_FLASH_THRESHOLD
	)
	@ConfigItem(
		keyName = "prayerFlashThreshold",
		name = "Prayer threshold",
		description = "Flashes the prayer bar when prayer points drop to or below this value.",
		position = 3,
		section = thresholdFlashSection
	)
	default int prayerFlashThreshold()
	{
		return 50;
	}

	@Range(
		min = 0,
		max = MAX_FLASH_THRESHOLD
	)
	@ConfigItem(
		keyName = "runEnergyFlashThreshold",
		name = "Run threshold",
		description = "Flashes the run energy bar when run energy drops to or below this value.",
		position = 4,
		section = thresholdFlashSection
	)
	default int runEnergyFlashThreshold()
	{
		return 50;
	}

	enum BarMode
	{
		DISABLED,
		HITPOINTS,
		PRAYER,
		RUN_ENERGY,
		SPECIAL_ATTACK,
		WARMTH
	}

	enum FlashDuration
	{
		DISABLED(0, "Disabled"),
		ONE_TICK(1, "1 tick"),
		TWO_TICKS(2, "2 ticks");

		private final int ticks;
		private final String name;

		FlashDuration(int ticks, String name)
		{
			this.ticks = ticks;
			this.name = name;
		}

		int getTicks()
		{
			return ticks;
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}
