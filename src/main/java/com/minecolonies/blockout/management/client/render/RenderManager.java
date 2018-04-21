package com.minecolonies.blockout.management.client.render;

import com.minecolonies.blockout.core.element.IDrawableUIElement;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.management.render.IRenderManager;
import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.render.standard.RenderingController;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class RenderManager implements IRenderManager
{
    @SideOnly(Side.CLIENT)
    private final IRenderingController renderingController = new RenderingController();

    @SideOnly(Side.CLIENT)
    @Override
    public void drawBackground(@NotNull final IUIElement host)
    {
        if (host instanceof IDrawableUIElement)
        {
            IDrawableUIElement iDrawableUIElement = (IDrawableUIElement) host;
            iDrawableUIElement.drawBackground(renderingController);
        }

        if (host instanceof IUIElementHost)
        {
            IUIElementHost iuiElementHost = (IUIElementHost) host;
            iuiElementHost.values().forEach(this::drawBackground);
        }
    }

    @Override
    public void drawForeground(@NotNull final IUIElement host)
    {
        if (host instanceof IDrawableUIElement)
        {
            IDrawableUIElement iDrawableUIElement = (IDrawableUIElement) host;
            iDrawableUIElement.drawForeground(renderingController);
        }

        if (host instanceof IUIElementHost)
        {
            IUIElementHost iuiElementHost = (IUIElementHost) host;
            iuiElementHost.values().forEach(this::drawForeground);
        }
    }
}