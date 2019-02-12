package com.ldtteam.blockout.binding.dependency;

import com.google.common.base.Functions;
import com.ldtteam.blockout.binding.property.Property;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class TransformingDependencyObjectTest
{

    @Test
    public void requiresDataContext()
    {
        final Property<String> properOne = new Property<>(Optional.empty(), Optional.empty(), false);
        final Property<String> properTwo = new Property<>(Optional.empty(), Optional.empty(), true);

        final IDependencyObject<String> propDepOne = new PropertyBasedDependencyObject<>(properOne, "Default");
        final IDependencyObject<String> propDepTwo = new PropertyBasedDependencyObject<>(properTwo, "Default");

        final IDependencyObject<String> targetOne = new TransformingDependencyObject<>(propDepOne, Functions.identity(), Functions.identity());
        final IDependencyObject<String> targetTwo = new TransformingDependencyObject<>(propDepTwo, Functions.identity(), Functions.identity());

        Assert.assertFalse(targetOne.requiresDataContext());
        Assert.assertTrue(targetTwo.requiresDataContext());
    }

    @Test
    public void get()
    {
        final IDependencyObject<String> input = new StaticDependencyObject<>("Input");

        final IDependencyObject<String> target = new TransformingDependencyObject<>(input, s -> s.replace("In", "Out"), s -> s.replace("Out", "In"));

        Assert.assertEquals("Output", target.get(new DummyInjectionTarget()));
    }

    @Test
    public void set()
    {
        final IDependencyObject<String> input = new StaticDependencyObject<>("Input");

        final IDependencyObject<String> target = new TransformingDependencyObject<>(input, s -> s.replace("In", "Out"), s -> s.replace("Out", "In"));

        Assert.assertEquals("Output", target.get(new DummyInjectionTarget()));

        target.set(new DummyInjectionTarget(), "Test");

        Assert.assertEquals("Test", target.get(new DummyInjectionTarget()));

        target.set(new DummyInjectionTarget(), "Output");

        Assert.assertEquals("Input", input.get(new DummyInjectionTarget()));
        Assert.assertEquals("Output", target.get(new DummyInjectionTarget()));
    }

    @Test
    public void hasChanged()
    {
        final IDependencyObject<String> input = new StaticDependencyObject<>("Input");

        final IDependencyObject<String> target = new TransformingDependencyObject<>(input, s -> s.replace("In", "Out"), s -> s.replace("Out", "In"));

        Assert.assertTrue(target.hasChanged(new DummyInjectionTarget()));
        Assert.assertEquals("Output", target.get(new DummyInjectionTarget()));
        Assert.assertFalse(target.hasChanged(new DummyInjectionTarget()));

        target.set(new DummyInjectionTarget(), "Test");

        Assert.assertTrue(target.hasChanged(new DummyInjectionTarget()));
        Assert.assertEquals("Test", target.get(new DummyInjectionTarget()));
        Assert.assertFalse(target.hasChanged(new DummyInjectionTarget()));
        Assert.assertFalse(target.hasChanged(new DummyInjectionTarget()));
        target.set(new DummyInjectionTarget(), "Output");
        Assert.assertTrue(target.hasChanged(new DummyInjectionTarget()));
        Assert.assertTrue(target.hasChanged(new DummyInjectionTarget()));

        Assert.assertEquals("Input", input.get(new DummyInjectionTarget()));
        Assert.assertEquals("Output", target.get(new DummyInjectionTarget()));
        Assert.assertFalse(target.hasChanged(new DummyInjectionTarget()));
    }
}