/*
 * Copyright (c) 2026, Arun <chat-space-blocker-plugin.vlsth@dralias.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
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

package org.asundr;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(ChatSpaceBlockerConfig.CONFIG_GROUP)
public interface ChatSpaceBlockerConfig extends Config
{
	String CONFIG_GROUP = "ChatSpaceBlocker";
	String KEY_CUSTOM_WIDGET_IDS = "customWidgetPairs";
	@ConfigSection(
		name = "General", description = "General settings", position = -1
	)
	String SECTION_GENERAL = "General";

	@ConfigSection(
			name = "Advanced", description = "Advanced settings", position = 1000
	)
	String SECTION_ADVANCED = "advanced";

	////

	@ConfigItem(
			keyName = "bypassWithShift",
			name = "Bypass with Shift",
			description = "If enabled, spaces can be entered at the start of a chat message by holding shift",
			section = SECTION_GENERAL
	)
	default boolean bypassWithShift() { return true; }

	@ConfigItem(
			keyName = "enableForBlank",
			name = "Enable for blank chat",
			description = "If enabled, treats entirely whitespace chat input as an empty prefix",
			section = SECTION_GENERAL
	)
	default boolean enableForBlank() { return true; }

	@ConfigItem(
			keyName = KEY_CUSTOM_WIDGET_IDS,
			name = "Disable for Widget ID",
			description = "Allows spaces to be entered when widgets with the specified IDs are open.<br>Enter each ID on a separate line in the format: group,child<br><br>" +
					"<span style=\"color:#9090FF\">If these widgets should be handled by default, please create an issue on the plugin's GitHub page including the group,child:<br>" +
					"https://github.com/asundr/runelite-chat-space-blocker/issues</span>",
			section = SECTION_ADVANCED
	)
	default String customWidgetPairs() { return ""; }

}
