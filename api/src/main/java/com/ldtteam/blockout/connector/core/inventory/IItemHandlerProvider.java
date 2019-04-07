package com.ldtteam.blockout.connector.core.inventory;

import com.ldtteam.jvoxelizer.item.handling.IItemHandler;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public interface IItemHandlerProvider extends Serializable
{

    /**
     * The id of the provider.
     *
     * @return The id.
     */
    @NotNull
    IIdentifier getId();

    /**
     *
     */
    @Nullable
    IItemHandler get(IItemHandlerManager manager);
}
