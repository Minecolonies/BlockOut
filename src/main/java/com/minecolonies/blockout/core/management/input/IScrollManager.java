package com.minecolonies.blockout.core.management.input;

public interface IScrollManager extends IInputManager
{
    void onMouseScroll(final int localX, final int localY, final int deltaWheel);
}
