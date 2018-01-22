package com.minecolonies.blockout.element.management.input;

import com.minecolonies.blockout.core.management.IUIManager;
import com.minecolonies.blockout.core.management.input.IInputManager;

public abstract class AbstractInputManager implements IInputManager
{

    private final IUIManager manager;
    private       boolean    shouldRemoveFocusOnAcceptanceFailure;

    protected AbstractInputManager(final IUIManager manager) {this.manager = manager;}

    @Override
    public boolean shouldRemoveFocusOnAcceptanceFailure()
    {
        return shouldRemoveFocusOnAcceptanceFailure;
    }

    @Override
    public void setShouldRemoveFocusOnAcceptanceFailure(final boolean shouldRemoveFocusOnAcceptanceFailure)
    {
        this.shouldRemoveFocusOnAcceptanceFailure = shouldRemoveFocusOnAcceptanceFailure;
    }

    public IUIManager getManager()
    {
        return manager;
    }

    protected void onAcceptanceFailure()
    {
        if (shouldRemoveFocusOnAcceptanceFailure)
        {
            getManager().getFocusManager().setFocusedElement(null);
        }
    }

}
