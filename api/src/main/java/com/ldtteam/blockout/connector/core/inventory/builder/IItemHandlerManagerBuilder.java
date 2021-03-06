package com.ldtteam.blockout.connector.core.inventory.builder;

import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerProvider;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IItemHandlerManagerBuilder
{

    @NotNull
    IItemHandlerManagerBuilder withProvider(@NotNull final IItemHandlerProvider provider);

    @NotNull
    default IItemHandlerManagerBuilder withTileBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final TileEntity tileEntity, @Nullable Direction facing
    )
    {
        return this.withTileBasedProvider(id, tileEntity.getWorld().getDimensionKey(), tileEntity.getPos(), facing);
    }

    @NotNull
    default IItemHandlerManagerBuilder withTileBasedProvider(
            @NotNull final ResourceLocation id, @NotNull final RegistryKey<World> dimId, @NotNull final BlockPos blockPos, @Nullable Direction facing
    )
    {
        return this.withTileBasedProvider(id, dimId, blockPos.getX(), blockPos.getY(), blockPos.getZ(), facing);
    }

    @NotNull
    IItemHandlerManagerBuilder withTileBasedProvider(
            @NotNull final ResourceLocation id, @NotNull final RegistryKey<World> dimId, @NotNull final int x, @NotNull final int y, @NotNull final int z, @Nullable final
    Direction facing);

    @NotNull
    default IItemHandlerManagerBuilder withTileBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final World world, @NotNull final BlockPos blockPos, @Nullable Direction facing
    )
    {
        return this.withTileBasedProvider(id, world.getDimensionKey(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), facing);
    }

    @NotNull
    default IItemHandlerManagerBuilder withEntityBasedProvider(
      @NotNull final ResourceLocation id, @NotNull final Entity entity, @Nullable final Direction facing
    )
    {
        return this.withEntityBasedProvider(id, entity.getEntityWorld().getDimensionKey(), entity.getEntityId(), facing);
    }

    @NotNull
    IItemHandlerManagerBuilder withEntityBasedProvider(
            @NotNull final ResourceLocation id, @NotNull final RegistryKey<World> dimId, @NotNull final int networkId, @Nullable final Direction facing
    );

    @NotNull
    IItemHandlerManagerBuilder withWrapped(
      @NotNull final ResourceLocation id, @NotNull final ResourceLocation wrappedId, @NotNull final int minSlot, @NotNull final int maxSlotExcluding
    );

    @NotNull
    default IItemHandlerManagerBuilder copyFrom(@NotNull final IItemHandlerManagerBuilder builder)
    {
        return copyFrom(builder.build());
    }

    @NotNull
    IItemHandlerManagerBuilder copyFrom(@NotNull final IItemHandlerManager manager);

    @NotNull
    IItemHandlerManager build();
}
