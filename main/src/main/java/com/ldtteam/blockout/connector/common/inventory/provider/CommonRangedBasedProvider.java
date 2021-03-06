package com.ldtteam.blockout.connector.common.inventory.provider;

import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerProvider;
import net.minecraftforge.items.IItemHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RangedWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommonRangedBasedProvider implements IItemHandlerProvider
{
    private static final long serialVersionUID = -2181178091874381035L;

    private final String id;
    private final String wrappedId;
    private final int    minSlot;
    private final int    maxSlotExlcuding;

    public CommonRangedBasedProvider(final String id, final String wrappedId, final int minSlot, final int maxSlotExlcuding)
    {
        this.id = id;
        this.wrappedId = wrappedId;
        this.minSlot = minSlot;
        this.maxSlotExlcuding = maxSlotExlcuding;

        if (id.equalsIgnoreCase(wrappedId))
        {
            throw new IllegalArgumentException("Can not reference yourself when ranging");
        }
    }

    @NotNull
    @Override
    public ResourceLocation getId()
    {
        return new ResourceLocation(id);
    }

    @Nullable
    @Override
    public IItemHandler get(final IItemHandlerManager manager)
    {
        final IItemHandler other = manager.getItemHandlerFromId(new ResourceLocation(wrappedId));

        if (!(other instanceof IItemHandlerModifiable))
        {
            throw new IllegalArgumentException("Manager did not return a modifyable item handler: " + wrappedId);
        }

        return new RangedWrapper((IItemHandlerModifiable) other, minSlot, maxSlotExlcuding);
    }
}
