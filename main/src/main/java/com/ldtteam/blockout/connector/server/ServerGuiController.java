package com.ldtteam.blockout.connector.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.connector.common.CommonGuiInstantiationController;
import com.ldtteam.blockout.connector.common.builder.CommonGuiKeyBuilder;
import com.ldtteam.blockout.connector.core.IGuiController;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.connector.core.builder.IGuiKeyBuilder;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.network.NetworkManager;
import com.ldtteam.blockout.network.message.CloseGuiCommandMessage;
import com.ldtteam.blockout.network.message.OpenGuiCommandMessage;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.jvoxelizer.IGameEngine;
import com.ldtteam.jvoxelizer.common.gameevent.event.player.IPlayerEvent;
import com.ldtteam.jvoxelizer.entity.living.player.IFakePlayer;
import com.ldtteam.jvoxelizer.entity.living.player.IMultiplayerPlayerEntity;
import com.ldtteam.jvoxelizer.entity.living.player.IPlayerEntity;
import com.ldtteam.jvoxelizer.event.manager.IEventManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class ServerGuiController implements IGuiController
{

    private final Map<IGuiKey, RootGuiElement> openUis        = new HashMap<>();
    private final Map<IGuiKey, List<UUID>>     watchers       = new HashMap<>();
    private final Map<UUID, IGuiKey>           playerWatching = new HashMap<>();

    @Override
    public void openUI(
      @NotNull final IPlayerEntity player, @NotNull final Consumer<IGuiKeyBuilder>... guiKeyBuilderConsumer)
    {
        final CommonGuiKeyBuilder builder = new CommonGuiKeyBuilder();
        Arrays.stream(guiKeyBuilderConsumer).forEach(iGuiKeyBuilderConsumer -> iGuiKeyBuilderConsumer.accept(builder));

        openUI(player, builder.build());
    }

    @Override
    public void openUI(@NotNull final IPlayerEntity player, @NotNull final IGuiKey key)
    {
        openUI(player.getId(), key);
    }

    @Override
    public void openUI(@NotNull final UUID playerId, @NotNull final Consumer<IGuiKeyBuilder>... guiKeyBuilderConsumer)
    {
        final CommonGuiKeyBuilder builder = new CommonGuiKeyBuilder();
        Arrays.stream(guiKeyBuilderConsumer).forEach(iGuiKeyBuilderConsumer -> iGuiKeyBuilderConsumer.accept(builder));

        openUI(playerId, builder.build());
    }

    @Override
    public void openUI(@NotNull final UUID playerId, @NotNull final IGuiKey key)
    {
        closeUI(playerId);

        final IMultiplayerPlayerEntity player = IGameEngine.getInstance().getCurrentServerInstance().getPlayerManager().getById(playerId);
        if (player == null)
        {
            Log.getLogger().warn("Failed to open UI for: " + playerId.toString() + ". Could not Identify player.");
            return;
        }

        if (player instanceof IFakePlayer)
        {
            //NOOP Return.
            return;
        }

        RootGuiElement host;

        if (!openUis.containsKey(key))
        {
            try
            {
                host = CommonGuiInstantiationController.getInstance().instantiateNewGui(key);
            }
            catch (IllegalArgumentException ex)
            {
                Log.getLogger().error("Failed to build guitemp for: " + playerId, ex);
                return;
            }

            openUis.put(key, host);
        }
        else
        {
            host = openUis.get(key);
        }

        watchers.putIfAbsent(key, new ArrayList<>());
        watchers.get(key).add(playerId);
        playerWatching.put(playerId, key);

        openGui(key, host, player);
    }

    @Override
    public void closeUI(@NotNull final IPlayerEntity player)
    {
        closeUI(player.getId());
    }

    @Override
    public void closeUI(@NotNull final UUID playerId)
    {
        final IMultiplayerPlayerEntity player = IGameEngine.getInstance().getCurrentServerInstance().getPlayerManager().getById(playerId);
        if (player == null)
        {
            Log.getLogger().warn("Failed to close UI for: " + playerId.toString() + ". Could not Identify player.");
            return;
        }

        if (player instanceof IFakePlayer)
        {
            //NOOP Return.
            return;
        }

        if (playerWatching.containsKey(playerId))
        {
            final IGuiKey currentlyWatching = playerWatching.get(playerId);
            watchers.get(currentlyWatching).remove(playerId);
            playerWatching.remove(playerId);

            if (watchers.get(currentlyWatching).isEmpty())
            {
                watchers.remove(currentlyWatching);
                openUis.remove(currentlyWatching);
            }
        }

        try
        {
            NetworkManager.sendTo(new CloseGuiCommandMessage(), player);
        }
        catch (Exception ex)
        {
            Log.getLogger().info("Could not send close message. Player might have logged out when UI was open.");
        }
    }

    @Nullable
    @Override
    public IGuiKey getOpenUI(@NotNull final IPlayerEntity player)
    {
        return getOpenUI(player.getId());
    }

    @Nullable
    @Override
    public IGuiKey getOpenUI(@NotNull final UUID player)
    {
        return playerWatching.get(player);
    }

    @Nullable
    @Override
    public RootGuiElement getRoot(@NotNull final IGuiKey guiKey)
    {
        return openUis.get(guiKey);
    }

    private void openGui(@NotNull final IGuiKey key, @NotNull final RootGuiElement rootGuiElement, @NotNull final IMultiplayerPlayerEntity playerMP)
    {
        playerMP.incrementWindowId();
        playerMP.closeOpenContainer();

        NetworkManager.sendTo(new OpenGuiCommandMessage(key, BlockOut.getBlockOut().getProxy().getFactoryController().getDataFromElement(rootGuiElement), playerMP.getCurrentWindowId()),
          playerMP);

        playerMP.setOpenContainer(new BlockOutContainer(key, openUis.get(key), playerMP.getCurrentWindowId()));
        playerMP.getOpenContainer().addListener(playerMP);

        IEventManager.post(IPlayerEvent.IPlayerContainerEvent.IOpen.create(playerMP, playerMP.getOpenContainer()));
    }

    /**
     * Returns a list of all UUIDs that are watching this UI.
     *
     * @param key The key for the guitemp.
     * @return The watching players.
     */
    public ImmutableList<UUID> getUUIDsOfPlayersWatching(@NotNull final IGuiKey key)
    {
        if (watchers.get(key) == null)
        {
            return ImmutableList.of();
        }

        return ImmutableList.copyOf(watchers.get(key));
    }

    public ImmutableMap<IGuiKey, RootGuiElement> getOpenUis()
    {
        return ImmutableMap.copyOf(openUis);
    }

    public ImmutableList<RootGuiElement> getOpenRoots()
    {
        return ImmutableList.copyOf(openUis.values());
    }
}
