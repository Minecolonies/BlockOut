package com.ldtteam.blockout.management.server.update;

import com.ldtteam.blockout.element.IUIElement;
import com.ldtteam.blockout.element.root.IRootGuiElement;
import com.ldtteam.blockout.management.IUIManager;
import com.ldtteam.blockout.management.common.update.ChildUpdateManager;
import com.ldtteam.blockout.management.update.IUpdateManager;
import com.ldtteam.blockout.util.Log;
import org.jetbrains.annotations.NotNull;

public class ServerUpdateManager implements IUpdateManager
{

    @NotNull
    private final IUIManager manager;
    private       boolean    dirty = false;

    public ServerUpdateManager(@NotNull final IUIManager manager) {this.manager = manager;}

    @Override
    public void updateElement(@NotNull final IUIElement element)
    {
        if (element instanceof IRootGuiElement)
        {
            IRootGuiElement rootGuiElement = (IRootGuiElement) element;
            rootGuiElement.getUiManager().getProfiler().startTick();
            rootGuiElement.getUiManager().getProfiler().startSection("Global Update");

            ChildUpdateManager childUpdateManager = new ChildUpdateManager(this);
            childUpdateManager.updateElement(rootGuiElement);
            rootGuiElement.getUiManager().getProfiler().endSection();
            rootGuiElement.getUiManager().getProfiler().endTick();

/*            File tmpDir = new File("./profiler.json");
            if (!tmpDir.exists())
            {
                ProfilerExporter.exportProfiler(element);
            }*/

            final IUIElement focusedElement = rootGuiElement.getUiManager().getFocusManager().getFocusedElement();
            if (!rootGuiElement.getAllCombinedChildElements().values().contains(focusedElement))
            {
                rootGuiElement.getUiManager().getFocusManager().setFocusedElement(null);
            }
        }
        else
        {
            Log.getLogger().warn("Somebody tried to update a none root element.");
        }
    }

    @Override
    public void markDirty()
    {
        this.dirty = true;
    }

    /**
     * Called by the forge event handler to indicate that a tick has passed on this UI and that a update packet should be send.
     */
    public void onNetworkTick()
    {
        if (dirty)
        {
            manager.getNetworkManager().onElementChanged(manager.getHost());
            dirty = false;
        }
    }

    /**
     * Indicates if the server side is dirty and needs to be updated.
     *
     * @return true when dirty false when not.
     */
    public boolean isDirty()
    {
        return dirty;
    }
}
