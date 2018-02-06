package com.minecolonies.blockout.management;

import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.management.IUIManager;
import com.minecolonies.blockout.core.management.focus.IFocusManager;
import com.minecolonies.blockout.core.management.input.IClickManager;
import com.minecolonies.blockout.core.management.input.IKeyManager;
import com.minecolonies.blockout.core.management.input.IScrollManager;
import com.minecolonies.blockout.core.management.network.INetworkManager;
import com.minecolonies.blockout.management.network.ServerNetworkManager;
import org.jetbrains.annotations.NotNull;

public class UIManager implements IUIManager
{

    @NotNull
    private final IUIElementHost host;
    @NotNull
    private final INetworkManager networkManager = new ServerNetworkManager();


    public UIManager(@NotNull final IUIElementHost host) {
        this.host = host;
    }

    @NotNull
    @Override
    public IUIElementHost getHost()
    {
        return host;
    }

    @NotNull
    @Override
    public INetworkManager getNetworkManager()
    {
        return networkManager;
    }

    @NotNull
    @Override
    public IFocusManager getFocusManager()
    {
        return null;
    }

    @NotNull
    @Override
    public IClickManager getClickManager()
    {
        return null;
    }

    @NotNull
    @Override
    public IKeyManager getKeyManager()
    {
        return null;
    }

    @NotNull
    @Override
    public IScrollManager getScrollManager()
    {
        return null;
    }
}