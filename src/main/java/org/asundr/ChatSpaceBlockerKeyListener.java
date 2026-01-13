package org.asundr;

import net.runelite.api.Client;
import net.runelite.api.gameval.VarClientID;
import net.runelite.client.input.KeyListener;

import java.awt.event.KeyEvent;

public class ChatSpaceBlockerKeyListener implements KeyListener
{
    private static Client client;
    private static ChatSpaceBlockerConfig config;

    public ChatSpaceBlockerKeyListener(Client client, ChatSpaceBlockerConfig config)
    {
        ChatSpaceBlockerKeyListener.client = client;
        ChatSpaceBlockerKeyListener.config = config;
    }

    private void handleTyped(KeyEvent e)
    {
        if (config.bypassWithShift() && e.isShiftDown())
        {
            return;
        }
        final String input = client.getVarcStrValue(VarClientID.CHATINPUT);
        if (Character.isWhitespace(e.getKeyChar()) && (config.enableForBlank() ? input.isBlank() : input.isEmpty()))
        {
            e.consume();
        }
    }
    @Override public void keyTyped(KeyEvent e) {
        handleTyped(e);
    }

    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
