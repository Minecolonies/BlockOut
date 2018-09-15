package com.ldtteam.blockout.element.core;

import com.ldtteam.blockout.binding.dependency.DependencyObjectHelper;
import com.ldtteam.blockout.binding.dependency.IDependencyObject;
import com.ldtteam.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.builder.core.builder.IBlockOutUIElementConstructionDataBuilder;
import com.ldtteam.blockout.core.element.IUIElement;
import com.ldtteam.blockout.core.element.IUIElementHost;
import com.ldtteam.blockout.core.element.values.Alignment;
import com.ldtteam.blockout.core.element.values.AxisDistance;
import com.ldtteam.blockout.core.element.values.Dock;
import com.ldtteam.blockout.core.management.update.IUpdateManager;
import com.ldtteam.blockout.event.IEventHandler;
import com.ldtteam.blockout.util.math.BoundingBox;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Optional;

public abstract class AbstractSimpleUIElement implements IUIElement
{
    @NotNull
    private final ResourceLocation type;

    @NotNull
    private IDependencyObject<ResourceLocation> style;

    @NotNull
    private final String           id;
    @NotNull
    private       IUIElementHost   parent;

    @NotNull
    private IDependencyObject<EnumSet<Alignment>> alignments  = DependencyObjectHelper.createFromValue(EnumSet.of(Alignment.NONE));
    @NotNull
    private IDependencyObject<Dock>               dock        = DependencyObjectHelper.createFromValue(Dock.NONE);
    @NotNull
    private IDependencyObject<AxisDistance>       margin      = DependencyObjectHelper.createFromValue(new AxisDistance());
    @NotNull
    private IDependencyObject<Vector2d>           elementSize = DependencyObjectHelper.createFromValue(new Vector2d());

    @NotNull
    private BoundingBox localBoundingBox;
    @NotNull
    private BoundingBox absoluteBoundingBox;

    @NotNull
    private IDependencyObject<Object> dataContext = DependencyObjectHelper.createFromValue(new Object());

    @NotNull
    private IDependencyObject<Boolean> visible = DependencyObjectHelper.createFromValue(true);
    @NotNull
    private IDependencyObject<Boolean> enabled = DependencyObjectHelper.createFromValue(true);

    public AbstractSimpleUIElement(
      @NotNull final ResourceLocation type,
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @Nullable final IUIElementHost parent)
    {
        this.type = type;
        this.style = style;
        this.id = id;
        this.parent = parent;
    }

    public AbstractSimpleUIElement(
      @NotNull final ResourceLocation type,
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Vector2d> elementSize,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled)
    {
        this.type = type;
        this.style = style;
        this.id = id;
        this.parent = parent;
        this.alignments = alignments;
        this.dock = dock;
        this.margin = margin;
        this.elementSize = elementSize;
        this.dataContext = dataContext;
        this.visible = visible;
        this.enabled = enabled;
        
    }

    @NotNull
    @Override
    public ResourceLocation getType()
    {
        return type;
    }

    @NotNull
    @Override
    public String getId()
    {
        return id;
    }

    @Nullable
    @Override
    public Object getDataContext()
    {
        if (dataContext.requiresDataContext())
        {
            return dataContext.get(getParent().getDataContext());
        }
        
        return dataContext.get(this);
    }


    @Override
    public void update(@NotNull final IUpdateManager updateManager)
    {
        if (updateBoundingBoxes())
        {
            updateManager.markDirty();
        }

        if (dataContext.hasChanged(parent.getDataContext()))
        {
            updateManager.markDirty();
        }
        if (alignments.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (style.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (dock.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (margin.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (enabled.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (visible.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (elementSize.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
    }

    @Override
    public EnumSet<Alignment> getAlignment()
    {
        return alignments.get(this);
    }

    @Override
    public void setDataContext(@Nullable final Object dataContext)
    {
        if (this.dataContext.requiresDataContext())
        {
            this.dataContext.set(getParent().getDataContext(), dataContext);
        }
        
        this.dataContext.set(this, dataContext);
    }

    @Override
    public Dock getDock()
    {
        return dock.get(this);
    }

    @Override
    public void setEnabled(final boolean enabled)
    {
        this.enabled.set(this, enabled);
    }

    @Override
    public AxisDistance getMargin()
    {
        return margin.get(this);
    }

    @Override
    public void setAlignment(@NotNull final EnumSet<Alignment> alignment)
    {
        this.alignments.set(this, alignment);
    }

    @Override
    public void setDock(@NotNull final Dock dock)
    {
        this.dock.set(this, dock);
    }

    @Override
    public void setMargin(@NotNull final AxisDistance margin)
    {
        this.margin.set(this, margin);
    }

    @Override
    public Vector2d getElementSize()
    {
        return elementSize.get(this);
    }

    @Override
    public void setElementSize(@NotNull final Vector2d elementSize)
    {
        this.elementSize.set(this, elementSize);
    }

    @NotNull
    @Override
    public BoundingBox getLocalBoundingBox()
    {
        return localBoundingBox;
    }

    @NotNull
    @Override
    public BoundingBox getAbsoluteBoundingBox()
    {
        return absoluteBoundingBox;
    }

    @NotNull
    @Override
    public IUIElementHost getParent()
    {
        return parent;
    }

    @Override
    public void setParent(@NotNull final IUIElementHost parent)
    {
        this.parent = parent;
    }

    @Override
    public boolean isVisible()
    {
        return visible.get(this) && (this == getParent() || getParent().isVisible());
    }

    @Override
    public void setVisible(final boolean visible)
    {
        this.visible.set(this, visible);
    }

    @Override
    public boolean isEnabled()
    {
        return isVisible() && enabled.get(this) && (this == getParent() || getParent().isEnabled());
    }

    /**
     * Returns the style of this IUIElement.
     *
     * @return The style of the element.
     */
    @NotNull
    @Override
    public ResourceLocation getStyleId()
    {
        return style.get(this);
    }

    private boolean updateBoundingBoxes()
    {
        boolean updated = false;

        if (updateLocalBoundingBox())
        {
            updated = true;
        }
        updateAbsoluteBoundingBox();

        return updated;
    }

    private boolean updateLocalBoundingBox()
    {
        final BoundingBox currentBoundingBox = this.localBoundingBox;

        //If we have no parent we see our default size as parent.
        //Else grab the size from the parent.
        final Vector2d parentSize = getParent() != this ? getParent().getLocalInternalBoundingBox().getSize() : new Vector2d(Double.MAX_VALUE, Double.MAX_VALUE);

        Optional<Double> marginLeft = getMargin().getLeft();
        Optional<Double> marginTop = getMargin().getTop();
        Optional<Double> marginRight = getMargin().getRight();
        Optional<Double> marginBottom = getMargin().getBottom();

        double width = getElementSize().getX();
        double height = getElementSize().getY();

        if (Alignment.LEFT.isActive(this) && Alignment.RIGHT.isActive(this))
        {
            if (!marginLeft.isPresent() && !marginRight.isPresent())
            {
                marginLeft = Optional.of((parentSize.getX() - width) / 2);
            }
            else if (!marginLeft.isPresent())
            {
                marginLeft = Optional.of(0d);
                width = parentSize.getX() - marginRight.orElse(0d);
            }
            else if (!marginRight.isPresent())
            {
                width = parentSize.getX() - marginLeft.orElse(0d);
            }
            else
            {
                width = parentSize.getX() - marginLeft.get() - marginRight.get();
            }
        }
        else if (Alignment.RIGHT.isActive(this))
        {
            marginLeft = Optional.of(parentSize.getX() - width - marginRight.orElse(0d));
        }

        if (Alignment.TOP.isActive(this) && Alignment.BOTTOM.isActive(this))
        {
            if (!marginTop.isPresent() && !marginBottom.isPresent())
            {
                marginTop = Optional.of((parentSize.getY() - height) / 2);
            }
            else if (!marginTop.isPresent())
            {
                marginTop = Optional.of(0d);
                height = parentSize.getY() - marginBottom.orElse(0d);
            }
            else if (!marginBottom.isPresent())
            {
                height = parentSize.getY() - marginTop.orElse(0d);
            }
            else
            {
                height = parentSize.getY() - marginTop.get() - marginBottom.get();
            }
        }
        else if (Alignment.BOTTOM.isActive(this))
        {
            marginTop = Optional.of(parentSize.getY() - height - marginBottom.orElse(0d));
        }

        final Vector2d origin = new Vector2d(marginLeft.orElse(0d), marginTop.orElse(0d));


        final Vector2d size =
          new Vector2d(width, height).nullifyNegatives();

        this.localBoundingBox = new BoundingBox(origin, size);
        this.localBoundingBox = getDock().apply(this, this.localBoundingBox);

        return currentBoundingBox == null || !currentBoundingBox.equals(this.localBoundingBox);
    }

    private void updateAbsoluteBoundingBox()
    {
        if (getParent() == this)
        {
            this.absoluteBoundingBox = getLocalBoundingBox();
            return;
        }

        final BoundingBox parentAbsoluteBindingBox = getParent().getAbsoluteInternalBoundingBox();
        this.absoluteBoundingBox = new BoundingBox(parentAbsoluteBindingBox.getLocalOrigin().move(getLocalBoundingBox().getLocalOrigin()), getLocalBoundingBox().getSize());
    }

    public static abstract class SimpleControlConstructionDataBuilder<B extends SimpleControlConstructionDataBuilder<B, S>, S extends AbstractSimpleUIElement>
      implements IBlockOutUIElementConstructionDataBuilder<B, S>
    {

        private final String                              controlId;
        private final IBlockOutGuiConstructionDataBuilder data;
        private final Class<S>                            controlClass;

        protected SimpleControlConstructionDataBuilder(final String controlId, final IBlockOutGuiConstructionDataBuilder data, final Class<S> controlClass)
        {
            this.controlId = controlId;
            this.data = data;
            this.controlClass = controlClass;
        }

        @NotNull
        @Override
        public B withDependency(@NotNull final String fieldName, @NotNull final IDependencyObject<?> dependency)
        {
            data.withDependency(controlId, fieldName, dependency);
            return (B) this;
        }

        @NotNull
        @Override
        public <A> B withEventHandler(
          @NotNull final String eventName, @NotNull final Class<A> argumentTypeClass, @NotNull final IEventHandler<S, A> eventHandler)
        {
            data.withEventHandler(controlId, eventName, controlClass, argumentTypeClass, eventHandler);
            return (B) this;
        }

        @NotNull
        public B withDependentAllignments(@NotNull final IDependencyObject<EnumSet<Alignment>> alignments)
        {
            return withDependency("alignments", alignments);
        }

        @NotNull
        public B withDependentDock(@NotNull final IDependencyObject<Dock> dock)
        {
            return withDependency("dock", dock);
        }

        @NotNull
        public B withDependentMargin(@NotNull final IDependencyObject<AxisDistance> margin)
        {
            return withDependency("margin", margin);
        }

        @NotNull
        public B withDependentSize(@NotNull final IDependencyObject<Vector2d> elementSize)
        {
            return withDependency("elementSize", elementSize);
        }

        @NotNull
        public B withDependentDataContext(@NotNull final IDependencyObject<Object> dataContext)
        {
            return withDependency("dataContext", dataContext);
        }

        @NotNull
        public B withDependentVisibility(@NotNull final IDependencyObject<Boolean> visible)
        {
            return withDependency("visible", visible);
        }

        @NotNull
        public B withDependentEnablement(@NotNull final IDependencyObject<Boolean> enabled)
        {
            return withDependency("enabled", enabled);
        }

        @NotNull
        public B withAllignments(@NotNull final EnumSet<Alignment> alignments)
        {
            return withDependency("alignments", DependencyObjectHelper.createFromValue(alignments));
        }

        @NotNull
        public B withDock(@NotNull final Dock dock)
        {
            return withDependency("dock", DependencyObjectHelper.createFromValue(dock));
        }

        @NotNull
        public B withMargin(@NotNull final AxisDistance margin)
        {
            return withDependency("margin", DependencyObjectHelper.createFromValue(margin));
        }

        @NotNull
        public B withSize(@NotNull final Vector2d elementSize)
        {
            return withDependency("elementSize", DependencyObjectHelper.createFromValue(elementSize));
        }

        @NotNull
        public B withDataContext(@NotNull final Object dataContext)
        {
            return withDependency("dataContext", DependencyObjectHelper.createFromValue(dataContext));
        }

        @NotNull
        public B withVisibility(@NotNull final Boolean visible)
        {
            return withDependency("visible", DependencyObjectHelper.createFromValue(visible));
        }

        @NotNull
        public B withEnablement(@NotNull final Boolean enabled)
        {
            return withDependency("enabled", DependencyObjectHelper.createFromValue(enabled));
        }
    }
}