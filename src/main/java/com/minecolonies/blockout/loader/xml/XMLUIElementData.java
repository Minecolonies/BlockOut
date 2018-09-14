package com.minecolonies.blockout.loader.xml;

import com.google.common.base.Functions;
import com.minecolonies.blockout.binding.dependency.DependencyObjectHelper;
import com.minecolonies.blockout.binding.dependency.IDependencyObject;
import com.minecolonies.blockout.binding.property.Property;
import com.minecolonies.blockout.binding.property.PropertyCreationHelper;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.core.element.values.Alignment;
import com.minecolonies.blockout.core.element.values.AxisDistance;
import com.minecolonies.blockout.core.element.values.AxisDistanceBuilder;
import com.minecolonies.blockout.core.element.values.ControlDirection;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.util.Constants;
import com.minecolonies.blockout.util.Log;
import com.minecolonies.blockout.util.math.BoundingBox;
import com.minecolonies.blockout.util.math.Vector2d;
import com.minecolonies.blockout.util.xml.XMLStreamSupport;
import com.minecolonies.blockout.util.xml.XMLToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Node;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Special parameters for the panes.
 */
public class XMLUIElementData implements IUIElementData
{

    private final Node           node;
    private final IUIElementHost parent;

    /**
     * Instantiates the pane parameters.
     *
     * @param n the node.
     */
    public XMLUIElementData(final Node n, final IUIElementHost parent)
    {
        node = n;
        this.parent = parent;
    }

    @Override
    public ResourceLocation getType()
    {
        return new ResourceLocation(Constants.MOD_ID, node.getNodeName());
    }

    @NotNull
    @Override
    public IDependencyObject<ResourceLocation> getBoundStyleId()
    {
        return bindOrReturnBoundTo(
          "style",
          ResourceLocation::new,
          PropertyCreationHelper.createFromNonOptional(
            Optional.of(c -> {
                if (getParentView() != null)
                {
                    return getParentView().getStyleId();
                }

                return Constants.Styles.CONST_DEFAULT;
            }),
            Optional.empty()
          ),
          Constants.Styles.CONST_DEFAULT
        );
    }

    @Nullable
    @Override
    public IUIElementHost getParentView()
    {
        return parent;
    }

    @Override
    @Nullable
    public List<IUIElementData> getChildren(@NotNull final IUIElementHost parentOfChildren)
    {
        return XMLStreamSupport
                 .streamChildren(node)
                 .filter(n -> n.getNodeType() == Node.ELEMENT_NODE)
                 .filter(n -> !n.getNodeName().startsWith(String.format("%s.", node.getNodeName())))
                 .map(n -> new XMLUIElementData(n, parentOfChildren))
                 .collect(Collectors.toList());
    }

    /**
     * Checks if this control has possible children.
     *
     * @return True when this control has children, false when not.
     */
    @Override
    public boolean hasChildren()
    {
        return node.hasChildNodes() && XMLStreamSupport
                                         .streamChildren(node)
                                         .anyMatch(n -> n.getNodeType() == Node.ELEMENT_NODE);
    }

    /**
     * Get the String attribute from the name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the String.
     */
    @Override
    public String getStringAttribute(final String name, final String def)
    {
        final Node attr = getAttribute(name);
        return (attr != null) ? attr.getNodeValue() : def;
    }

    @Override
    public IDependencyObject<Integer> getBoundIntegerAttribute(@NotNull final String name, final int def)
    {
        return bindOrReturnStaticViaString(name, Integer::new, def);
    }

    @Override
    public IDependencyObject<Float> getBoundFloatAttribute(@NotNull final String name, final float def)
    {
        return bindOrReturnStaticViaString(name, Float::new, def);
    }

    @Override
    public IDependencyObject<Double> getBoundDoubleAttribute(@NotNull final String name, final double def)
    {
        return bindOrReturnStaticViaString(name, Double::new, def);
    }

    @Override
    public IDependencyObject<Boolean> getBoundBooleanAttribute(@NotNull final String name, final boolean def)
    {
        return bindOrReturnStaticViaString(name, Boolean::new, def);
    }

    @Override
    public <T extends Enum<T>> IDependencyObject<T> getBoundEnumAttribute(@NotNull final String name, final Class<T> clazz, final T def)
    {
        return bindOrReturnStaticViaString(name, s -> Enum.valueOf(clazz, s), def);
    }

    @Override
    public <T extends Enum<T>> IDependencyObject<EnumSet<T>> getBoundEnumSetAttribute(@NotNull final String name, @NotNull final Class<T> clazz)
    {
        return bindOrReturnStaticViaString(name, s -> {
            final String[] splitted = s.split(",");

            final EnumSet<T> result = EnumSet.noneOf(clazz);
            for (String e : splitted)
            {
                result.add(Enum.valueOf(clazz, e.trim()));
            }

            return result;
        }, EnumSet.noneOf(clazz));
    }

    /**
     * Get the integer attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the int.
     */
    @Override
    public int getIntegerAttribute(final String name, final int def)
    {
        final String attr = getStringAttribute(name, null);
        if (attr != null)
        {
            return Integer.parseInt(attr);
        }
        return def;
    }

    @Override
    public IDependencyObject<String> getBoundStringAttribute(@NotNull final String name, final String def)
    {
        return bindOrReturnStaticViaString(name, Functions.identity(), def);
    }

    /**
     * Get the float attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the float.
     */
    @Override
    public float getFloatAttribute(final String name, final float def)
    {
        final String attr = getStringAttribute(name, null);
        if (attr != null)
        {
            return Float.parseFloat(attr);
        }
        return def;
    }

    @Override
    public IDependencyObject<AxisDistance> getBoundAxisDistanceAttribute(
      @NotNull final String name, final AxisDistance def)
    {
        return bindOrReturnStaticViaString(name, s -> {
            final AxisDistanceBuilder builder = new AxisDistanceBuilder();
            builder.readFromString(getParentView().getElementSize(), s);

            return builder.create();
        }, def);
    }

    /**
     * Get the double attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the double.
     */
    @Override
    public double getDoubleAttribute(final String name, final double def)
    {
        final String attr = getStringAttribute(name, null);
        if (attr != null)
        {
            return Double.parseDouble(attr);
        }

        return def;
    }

    @Override
    public IDependencyObject<EnumSet<Alignment>> getBoundAlignmentAttribute(
      @NotNull final String name, final EnumSet<Alignment> def)
    {
        return bindOrReturnStaticViaString(name, Alignment::fromString, def);
    }

    /**
     * Get the boolean attribute from name and definition.
     *
     * @param name the name.
     * @param def  the definition.
     * @return the boolean.
     */
    @Override
    public boolean getBooleanAttribute(final String name, final boolean def)
    {
        final String attr = getStringAttribute(name, null);
        if (attr != null)
        {
            return Boolean.parseBoolean(attr);
        }
        return def;
    }

    /**
     * Returns a bound ControlDirection attribute from a name and a default value.
     * If the value is not bound nor found, a static bound to the given default value is returned.
     *
     * @param name The name
     * @param def  The default value.
     * @return The bound object.
     */
    @Override
    public IDependencyObject<ControlDirection> getBoundControlDirectionAttribute(
      @NotNull final String name, final ControlDirection def)
    {
        return bindOrReturnStaticViaString(name, ControlDirection::fromString, def);
    }

    @Override
    public IDependencyObject<Vector2d> getBoundVector2dAttribute(@NotNull final String name, final Vector2d def)
    {
        return bindOrReturnStaticViaString(name, s -> {
            final String[] components = s.split(",");
            if (components.length == 1)
            {
                return new Vector2d(Double.parseDouble(components[0]));
            }
            else if (components.length == 2)
            {
                return new Vector2d(Double.parseDouble(components[0]), Double.parseDouble(components[1]));
            }
            else
            {
                return def;
            }
        }, def);
    }

    @Override
    public IDependencyObject<ResourceLocation> getBoundResourceLocationAttribute(@NotNull final String name, final ResourceLocation def)
    {
        return bindOrReturnStaticViaString(name, ResourceLocation::new, def);
    }

    @Override
    public IDependencyObject<BoundingBox> getBoundBoundingBoxAttribute(
      @NotNull final String name, final BoundingBox def)
    {
        return bindOrReturnStaticViaString(name, BoundingBox::fromString, def);
    }

    @Override
    public IDependencyObject<Object> getBoundDataContext()
    {
        return bindOrReturnBoundTo("dataContext", e -> new Object(), PropertyCreationHelper.create(
          Optional::of,
          null
          ),
          new Object());
    }

    @Override
    public IDependencyObject<Object> getBoundObject(@NotNull final String name, final Object def)
    {
        return
          bindOrReturnStaticViaString(
            name,
            s -> s,
            def
          );
    }

    /**
     * Returns the nbt stored in the attribute with the given name.
     *
     * @param name The name to lookup the nbt for.
     * @return The NBT contained in the attribute with the given name.
     */
    @Override
    public <T extends NBTBase> T getNBTAttribute(@NotNull final String name)
    {
        final Node node = getAttribute(name);
        final NBTBase nbt = XMLToNBT.fromXML(node);

        try
        {
            return (T) nbt;
        }
        catch (ClassCastException ex)
        {
            Log.getLogger().warn("Failed to parse XML into NBT. Contained type is not correct.", ex);
            return null;
        }
    }

    /**
     * Returns the bound nbt stored in the attribute with the given name.
     *
     * @param name The name to lookup the bound nbt for.
     * @param def  The default used incase of binding failure.
     * @return The bound nbt.
     */
    @Override
    public <T extends NBTBase> IDependencyObject<T> getBoundNBTAttribute(@NotNull final String name, @NotNull final T def)
    {
        return bindOrReturnStatic(name, node -> {
              final NBTBase base = XMLToNBT.fromXML(node);
              try
              {
                  return (T) base;
              }
              catch (ClassCastException ex)
              {
                  Log.getLogger().warn("Failed to read XML into NBT.", ex);
                  return null;
              }
          },
          def);
    }

    private <T> IDependencyObject<T> bindOrReturnStaticViaString(
      @NotNull final String name, @NotNull final
    Function<String, T> extract, T defaultValue)
    {
        return bindOrReturnStatic(name,
          node -> extract.apply(node.getNodeValue()),
          defaultValue);
    }

    private <T> IDependencyObject<T> bindOrReturnStatic(
      @NotNull final String name, @NotNull final
    Function<Node, T> extract, T defaultValue)
    {
        @Nullable final Node node = getAttribute(name);
        if (node == null)
        {
            return DependencyObjectHelper.createFromValue(defaultValue);
        }

        @Nullable final String attribute = node == null ? null : node.getNodeValue();
        if (attribute == null)
        {
            return DependencyObjectHelper.createFromValue(defaultValue);
        }

        final String elementContents = attribute;

        final Matcher singleNameMatcher = IUIElementData.SINGLE_NAME_BINDING_REGEX.matcher(elementContents);
        if (singleNameMatcher.matches())
        {
            final String fieldName = singleNameMatcher.group("singleName");

            final Property<T> fieldProperty;
            if (fieldName.equalsIgnoreCase("this"))
            {
                fieldProperty = PropertyCreationHelper.createFromNonOptional(
                  Optional.of((context) -> (T) context),
                  Optional.empty()
                );
            }
            else
            {
                fieldProperty = PropertyCreationHelper.createFromName(Optional.of(fieldName));
            }
            return DependencyObjectHelper.createFromProperty(fieldProperty, defaultValue);
        }

        final Matcher multiNameMatcher = IUIElementData.SPLIT_NAME_BINDING_REGEX.matcher(elementContents);
        if (multiNameMatcher.matches())
        {
            final String getterName = multiNameMatcher.group("getterName");
            final String setterName = multiNameMatcher.group("setterName");
            final Property<T> getterSetterProperty = PropertyCreationHelper.createFromName(Optional.of(getterName), Optional.of(setterName));
            return DependencyObjectHelper.createFromProperty(getterSetterProperty, defaultValue);
        }

        return DependencyObjectHelper.createFromValue(extract.apply(node));
    }

    private Node getAttribute(final String name)
    {
        final Node attributeNode = node.getAttributes().getNamedItem(name);
        if (attributeNode == null)
        {
            return XMLStreamSupport
                     .streamChildren(node)
                     .filter(n -> n.getNodeType() == Node.ELEMENT_NODE)
                     .filter(n -> n.getNodeName().startsWith(String.format("%s.", node.getNodeName())))
                     .filter(n -> n.getNodeName().replace(String.format("%s.", node.getNodeName()), "").equals(name))
                     .findFirst()
                     .orElse(null);
        }

        return attributeNode;
    }

    private <T> IDependencyObject<T> bindOrReturnBoundTo(
      @NotNull final String name,
      @NotNull final Function<String, T> extract,
      @NotNull final Property<T> boundTo,
      @NotNull final T defaultValue)
    {
        @Nullable final String attribute = getStringAttribute(name, null);
        if (attribute == null)
        {
            return DependencyObjectHelper.createFromProperty(boundTo, defaultValue);
        }

        final String elementContents = attribute;

        final Matcher singleNameMatcher = IUIElementData.SINGLE_NAME_BINDING_REGEX.matcher(elementContents);
        if (singleNameMatcher.matches())
        {
            final String fieldName = singleNameMatcher.group("singleName");

            final Property<T> fieldProperty;
            if (fieldName.equalsIgnoreCase("this"))
            {
                fieldProperty = PropertyCreationHelper.createFromNonOptional(
                  Optional.of((context) -> (T) context),
                  Optional.empty()
                );
            }
            else
            {
                fieldProperty = PropertyCreationHelper.createFromName(Optional.of(fieldName));
            }
            return DependencyObjectHelper.createFromProperty(fieldProperty, defaultValue);
        }

        final Matcher multiNameMatcher = IUIElementData.SPLIT_NAME_BINDING_REGEX.matcher(elementContents);
        if (multiNameMatcher.matches())
        {
            final String getterName = multiNameMatcher.group("getterName");
            final String setterName = multiNameMatcher.group("setterName");
            final Property<T> getterSetterProperty = PropertyCreationHelper.createFromName(Optional.of(getterName), Optional.of(setterName));
            return DependencyObjectHelper.createFromProperty(getterSetterProperty, defaultValue);
        }

        return DependencyObjectHelper.createFromValue(extract.apply(attribute));
    }
}
