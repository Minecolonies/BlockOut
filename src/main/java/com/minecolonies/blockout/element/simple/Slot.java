package com.minecolonies.blockout.element.simple;

import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.builder.core.builder.IBlockOutGuiConstructionDataBuilder;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.drawable.IDrawableUIElement;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.Dock;
import com.minecolonies.blockout.core.factory.IUIElementFactory;
import com.minecolonies.blockout.core.management.update.IUpdateManager;
import com.minecolonies.blockout.element.core.AbstractSimpleUIElement;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.IUIElementDataBuilder;
import com.minecolonies.blockout.render.core.IRenderingController;
import com.minecolonies.blockout.style.resources.ImageResource;
import com.minecolonies.blockout.util.math.Vector2d;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static com.minecolonies.blockout.util.Constants.Controls.General.*;
import static com.minecolonies.blockout.util.Constants.Controls.Slot.*;

public class Slot extends AbstractSimpleUIElement implements IDrawableUIElement
{
    @NotNull
    private IDependencyObject<ResourceLocation> inventoryId;
    @NotNull
    private IDependencyObject<Integer>          inventoryIndex;
    @NotNull
    private IDependencyObject<ResourceLocation> backgroundImageResource;

    @NotNull
    private int slotIndex;

    public Slot(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<ResourceLocation> inventoryId,
      @NotNull final IDependencyObject<Integer> inventoryIndex,
      @NotNull final IDependencyObject<ResourceLocation> backgroundImage)
    {
        super(KEY_SLOT, style, id, parent);
        this.inventoryId = inventoryId;
        this.inventoryIndex = inventoryIndex;
        this.backgroundImageResource = backgroundImage;
    }

    public Slot(
      @NotNull final IDependencyObject<ResourceLocation> style,
      @NotNull final String id,
      @NotNull final IUIElementHost parent,
      @NotNull final IDependencyObject<EnumSet<Alignment>> alignments,
      @NotNull final IDependencyObject<Dock> dock,
      @NotNull final IDependencyObject<AxisDistance> margin,
      @NotNull final IDependencyObject<Object> dataContext,
      @NotNull final IDependencyObject<Boolean> visible,
      @NotNull final IDependencyObject<Boolean> enabled,
      @NotNull final IDependencyObject<ResourceLocation> inventoryId,
      @NotNull final IDependencyObject<Integer> inventoryIndex,
      @NotNull final IDependencyObject<ResourceLocation> backgroundImageResource)
    {
        super(KEY_SLOT, style, id, parent, alignments, dock, margin, DependencyObjectHelper.createFromValue(new Vector2d(18, 18)), dataContext, visible, enabled);
        this.inventoryId = inventoryId;
        this.inventoryIndex = inventoryIndex;
        this.backgroundImageResource = backgroundImageResource;
    }

    @Override
    public void update(@NotNull final IUpdateManager updateManager)
    {
        super.update(updateManager);

        if (inventoryId.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (inventoryIndex.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
        if (backgroundImageResource.hasChanged(getDataContext()))
        {
            updateManager.markDirty();
        }
    }

    @Override
    public void drawBackground(@NotNull final IRenderingController controller)
    {
        final Vector2d size = getLocalBoundingBox().getSize();
        final ImageResource resource = getBackgroundImage();
        final Vector2d scalingFactor = resource.getScalingFactor(size);

        GlStateManager.pushMatrix();
        GlStateManager.scale(scalingFactor.getX(), scalingFactor.getY(), 1f);
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();

        controller.bindTexture(resource.getDiskLocation());
        controller.drawTexturedModalRect(new Vector2d(),
          size,
          resource.getOffset(),
          resource.getSize(),
          resource.getFileSize());

        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();

        controller.drawSlotContent(this);
        controller.drawSlotMouseOverlay(this);
    }

    @Override
    public void drawForeground(@NotNull final IRenderingController controller)
    {

    }

    @NotNull
    public ImageResource getBackgroundImage()
    {
        return getResource(getBackgroundImageResource());
    }

    @NotNull
    public ResourceLocation getBackgroundImageResource()
    {
        return backgroundImageResource.get(getDataContext());
    }

    void setBackgroundImageResource(@NotNull final ResourceLocation location)
    {
        backgroundImageResource = DependencyObjectHelper.createFromValue(location);
    }

    @NotNull
    public ResourceLocation getInventoryId()
    {
        return inventoryId.get(getDataContext());
    }

    void setInventoryId(@NotNull final ResourceLocation inventoryId)
    {
        this.inventoryId = DependencyObjectHelper.createFromValue(inventoryId);
    }

    @NotNull
    public Integer getInventoryIndex()
    {
        return inventoryIndex.get(getDataContext());
    }

    void setInventoryIndex(@NotNull final Integer index)
    {
        inventoryIndex = DependencyObjectHelper.createFromValue(index);
    }

    @NotNull
    public int getSlotIndex()
    {
        return slotIndex;
    }

    public void setSlotIndex(@NotNull final int slotIndex)
    {
        this.slotIndex = slotIndex;
    }

    public static final class SlotConstructionDataBuilder extends AbstractSimpleUIElement.SimpleControlConstructionDataBuilder<SlotConstructionDataBuilder, Slot>
    {

        public SlotConstructionDataBuilder(
          final String controlId,
          final IBlockOutGuiConstructionDataBuilder data,
          final Class<Slot> controlClass)
        {
            super(controlId, data, controlClass);
        }

        public SlotConstructionDataBuilder withDependentInventoryId(@NotNull final IDependencyObject<ResourceLocation> inventoryId)
        {
            return withDependency("inventoryId", inventoryId);
        }

        public SlotConstructionDataBuilder withInventoryId(@NotNull final ResourceLocation inventoryId)
        {
            return withDependency("inventoryId", DependencyObjectHelper.createFromValue(inventoryId));
        }

        public SlotConstructionDataBuilder withDependentInventoryIndex(@NotNull final IDependencyObject<Integer> inventoryIndex)
        {
            return withDependency("inventoryIndex", inventoryIndex);
        }

        public SlotConstructionDataBuilder withInventoryIndex(@NotNull final Integer inventoryIndex)
        {
            return withDependency("inventoryIndex", DependencyObjectHelper.createFromValue(inventoryIndex));
        }

        public SlotConstructionDataBuilder withDependentBackgroundImageResource(@NotNull final IDependencyObject<ResourceLocation> backgroundImageResource)
        {
            return withDependency("backgroundImageResource", backgroundImageResource);
        }

        public SlotConstructionDataBuilder withBackgroundImageResource(@NotNull final ResourceLocation backgroundImageResource)
        {
            return withDependency("backgroundImageResource", DependencyObjectHelper.createFromValue(backgroundImageResource));
        }
    }
    
    public static class Factory implements IUIElementFactory<Slot>
    {

        @NotNull
        @Override
        public ResourceLocation getType()
        {
            return KEY_SLOT;
        }

        @NotNull
        @Override
        public Slot readFromElementData(@NotNull final IUIElementData elementData)
        {
            final IDependencyObject<ResourceLocation> style = elementData.getBoundStyleId();
            final String id = elementData.getElementId();
            final IDependencyObject<EnumSet<Alignment>> alignments = elementData.getBoundAlignmentAttribute(CONST_ALIGNMENT);
            final IDependencyObject<Dock> dock = elementData.getBoundEnumAttribute(CONST_DOCK, Dock.class, Dock.NONE);
            final IDependencyObject<AxisDistance> margin = elementData.getBoundAxisDistanceAttribute(CONST_MARGIN);
            final IDependencyObject<Object> dataContext = elementData.getBoundDataContext();
            final IDependencyObject<Boolean> visible = elementData.getBoundBooleanAttribute(CONST_VISIBLE);
            final IDependencyObject<Boolean> enabled = elementData.getBoundBooleanAttribute(CONST_ENABLED);
            final IDependencyObject<ResourceLocation> inventoryId = elementData.getBoundResourceLocationAttribute(CONST_INVENTORY_ID);
            final IDependencyObject<Integer> inventoryIndex = elementData.getBoundIntegerAttribute(CONST_INVENTORY_INDEX);
            final IDependencyObject<ResourceLocation> icon = elementData.getBoundResourceLocationAttribute(CONST_BACKGROUND_IMAGE);

            return new Slot(
              style,
              id,
              elementData.getParentView(),
              alignments,
              dock,
              margin,
              dataContext,
              visible,
              enabled,
              inventoryId,
              inventoryIndex,
              icon);
        }

        @Override
        public void writeToElementData(@NotNull final Slot element, @NotNull final IUIElementDataBuilder builder)
        {
            builder
              .addAlignment(CONST_ALIGNMENT, element.getAlignment())
              .addEnum(CONST_DOCK, element.getDock())
              .addAxisDistance(CONST_MARGIN, element.getMargin())
              .addVector2d(CONST_ELEMENT_SIZE, element.getElementSize())
              .addBoolean(CONST_VISIBLE, element.isVisible())
              .addBoolean(CONST_ENABLED, element.isEnabled())
              .addResourceLocation(CONST_BACKGROUND_IMAGE, element.getBackgroundImageResource())
              .addResourceLocation(CONST_INVENTORY_ID, element.getInventoryId())
              .addInteger(CONST_INVENTORY_INDEX, element.getInventoryIndex());
        }
    }
}
