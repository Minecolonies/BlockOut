package com.minecolonies.blockout_test.tests;

import com.google.common.collect.Lists;
import com.minecolonies.blockout_test.tests.guis.BoundListHorizontalTest;
import com.minecolonies.blockout_test.tests.guis.ImageOnlyTest;
import com.minecolonies.blockout_test.tests.guis.InventoryGridTest;

import java.util.List;

public class BlockOutUITestManager
{
    private static BlockOutUITestManager ourInstance = new BlockOutUITestManager();

    public static BlockOutUITestManager getInstance()
    {
        return ourInstance;
    }

    private final List<IBlockOutUITest> testList;

    private BlockOutUITestManager()
    {
        testList = Lists.newArrayList(
          new ImageOnlyTest(),
          new BoundListHorizontalTest(),
          new InventoryGridTest()
        );
    }

    public List<IBlockOutUITest> getTestList()
    {
        return testList;
    }
}
