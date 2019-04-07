package com.ldtteam.blockout.management.client.network.messages;

import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.network.message.core.IBlockOutClientToServerMessage;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.blockout.util.mouse.MouseButton;
import com.ldtteam.jvoxelizer.entity.living.player.IMultiplayerPlayerEntity;
import com.ldtteam.jvoxelizer.networking.messaging.IMessageContext;
import org.jetbrains.annotations.NotNull;

public class OnMouseClickEndMessage implements IBlockOutClientToServerMessage
{
    @NotNull
    private final int         localX;
    @NotNull
    private final int         localY;
    @NotNull
    private final MouseButton button;

    public OnMouseClickEndMessage(@NotNull final int localX, @NotNull final int localY, @NotNull final MouseButton button)
    {
        this.localX = localX;
        this.localY = localY;
        this.button = button;
    }

    @Override
    public void onMessageArrivalAtServer(@NotNull final IMessageContext ctx)
    {
        final IMultiplayerPlayerEntity player = ctx.getServerHandler().getPlayer();
        final IGuiKey key = ProxyHolder.getInstance().getGuiController().getOpenUI(player);
        if (key == null)
        {
            Log.getLogger().error("Player is not watching a BlockOut guitemp.");
            return;
        }

        final RootGuiElement rootGuiElement = (RootGuiElement) ProxyHolder.getInstance().getGuiController().getRoot(key);
        if (rootGuiElement == null)
        {
            Log.getLogger().error("Player seems to be watching an unknown Gui.");
            return;
        }

        rootGuiElement.getUiManager().getClickManager().onMouseClickEnd(localX, localY, button);
    }
}
