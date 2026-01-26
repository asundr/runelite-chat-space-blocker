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

import net.runelite.api.Client;
import net.runelite.api.events.ClientTick;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.VarClientID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyListener;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

public class ChatSpaceBlockerKeyListener implements KeyListener
{
    private static final int WIDGET_CHILD_MESSAGE = 38;

    static class WidgetPairId
    {
        int group;
        int child = 0;
        WidgetPairId(int group, int child) { this.group = group; this.child = child;}
        WidgetPairId(int group) { this.group = group; }
    }

    private static Client client;
    private static ChatSpaceBlockerConfig config;

    private static final ArrayList<WidgetPairId> tickWidgetIds = new ArrayList<>(Arrays.asList(
            new WidgetPairId(InterfaceID.CHATBOX, WIDGET_CHILD_MESSAGE)          // prompt box
    ));
    private static final ArrayList<WidgetPairId> userAddedWidgetIds = new ArrayList<>();

    private boolean textEntryOpen = false;

    public ChatSpaceBlockerKeyListener(Client client, ChatSpaceBlockerConfig config)
    {
        ChatSpaceBlockerKeyListener.client = client;
        ChatSpaceBlockerKeyListener.config = config;
        updateUserWidgetIDs();
    }

    @Subscribe
    private void onClientTick(ClientTick e)
    {
        for (final WidgetPairId widgetId : tickWidgetIds)
        {
            if (isWidgetOpen(widgetId.group, widgetId.child))
            {
                textEntryOpen = true;
                return;
            }
        }
        for (final WidgetPairId widgetId : userAddedWidgetIds)
        {
            if (isWidgetOpen(widgetId.group, widgetId.child))
            {
                textEntryOpen = true;
                return;
            }
        }
        textEntryOpen = false;
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged evt)
    {
        if (evt.getGroup().equals(ChatSpaceBlockerConfig.CONFIG_GROUP))
        {
            if (evt.getKey().equals(ChatSpaceBlockerConfig.KEY_CUSTOM_WIDGET_IDS))
            {
                updateUserWidgetIDs();
            }
        }
    }

    private void handleTyped(KeyEvent e)
    {
        if (config.bypassWithShift() && e.isShiftDown() || isTextInputOpen())
        {
            return;
        }
        final String input = client.getVarcStrValue(VarClientID.CHATINPUT);
        if (Character.isWhitespace(e.getKeyChar()) && (config.enableForBlank() ? input.isBlank() : input.isEmpty()))
        {
            e.consume();
        }
    }
    @Override public void keyTyped(KeyEvent e) { handleTyped(e); }
    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    private boolean isTextInputOpen()
    {
        return textEntryOpen;
    }

    private static boolean isWidgetOpen(int group, int child)
    {
        final Widget textEntryWidget = client.getWidget(group, child);
        return textEntryWidget!= null && !textEntryWidget.isHidden();
    }

    private static void updateUserWidgetIDs()
    {
        userAddedWidgetIds.clear();
        final String widgetIdString = config.customWidgetPairs();
        final BufferedReader reader = new BufferedReader(new StringReader(widgetIdString));
        String nextLine;
        try
        {
            while ((nextLine = reader.readLine()) != null)
            {
                final String[] ids = nextLine.split(",");
                if (ids.length == 1)
                {
                    final int group = widgetIdFromString(ids[0]);
                    if (group != -1)
                    {
                        userAddedWidgetIds.add(new WidgetPairId(group, 0));
                    }
                }
                else if (ids.length == 2)
                {
                    final int group = widgetIdFromString(ids[0]);
                    if (group == -1)
                    {
                        continue;
                    }
                    final int child = widgetIdFromString(ids[1]);
                    if (child == -1)
                    {
                        continue;
                    }
                    userAddedWidgetIds.add(new WidgetPairId(group, child));
                }
            }
            reader.close();
        }
        catch (Exception ignored) { }
    }

    private static int widgetIdFromString(final String s)
    {
        try
        {
            return Integer.parseInt(s.trim());
        }
        catch (Exception e)
        {
            return -1;
        }
    }
}
