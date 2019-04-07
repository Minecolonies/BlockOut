package com.ldtteam.blockout.connector.common.inventory.provider;

import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerProvider;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.jvoxelizer.block.entity.IBlockEntity;
import com.ldtteam.jvoxelizer.common.capability.ICapability;
import com.ldtteam.jvoxelizer.dimension.IDimension;
import com.ldtteam.jvoxelizer.item.handling.IItemHandler;
import com.ldtteam.jvoxelizer.util.facing.IFacing;
import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import com.ldtteam.jvoxelizer.util.math.coordinate.block.IBlockCoordinate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommonTileBasedProvider implements IItemHandlerProvider
{

    @NotNull
    private final String id;

    @NotNull
    private final int dimId;
    @NotNull
    private final int x;
    @NotNull
    private final int y;
    @NotNull
    private final int z;

    @Nullable
    private final IFacing facing;

    public CommonTileBasedProvider(
      @NotNull final IIdentifier id,
      @NotNull final int dimId,
      @NotNull final int x,
      @NotNull final int y,
      @NotNull final int z,
      @Nullable final IFacing facing)
    {
        this.id = id.toString();
        this.dimId = dimId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.facing = facing;
    }

    @Override
    public int hashCode()
    {
        int result = getId().hashCode();
        result = 31 * result + dimId;
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + (facing != null ? facing.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final CommonTileBasedProvider that = (CommonTileBasedProvider) o;

        if (dimId != that.dimId)
        {
            return false;
        }
        if (x != that.x)
        {
            return false;
        }
        if (y != that.y)
        {
            return false;
        }
        if (z != that.z)
        {
            return false;
        }
        if (!getId().equals(that.getId()))
        {
            return false;
        }
        return facing == that.facing;
    }

    @NotNull
    @Override
    public IIdentifier getId()
    {
        return IIdentifier.create(id);
    }

    @Nullable
    @Override
    public IItemHandler get(@NotNull final IItemHandlerManager manager)
    {
        final IDimension<?> blockAccess = ProxyHolder.getInstance().getDimensionFromDimensionId(dimId);
        final IBlockEntity tileEntity = blockAccess.getBlockEntity(IBlockCoordinate.create(x,y,z));

        if (tileEntity == null)
        {
            return null;
        }

        if (tileEntity.hasCapability(ICapability.getItemHandlerCapability(), facing))
        {
            return tileEntity.getCapability(ICapability.getItemHandlerCapability(), facing);
        }

        return null;
    }
}
