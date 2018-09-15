package com.ldtteam.blockout.event.injector;

import com.ldtteam.blockout.event.IEventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IEventHandlerProvider
{

    @NotNull
    <S, A> List<IEventHandler<S, A>> getEventHandlers(@NotNull final String id, @NotNull final Class<S> sourceClass, @NotNull final Class<A> argumentClass);
}
