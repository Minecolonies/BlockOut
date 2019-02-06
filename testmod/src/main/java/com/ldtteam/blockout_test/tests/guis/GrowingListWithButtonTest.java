package com.ldtteam.blockout_test.tests.guis;

import com.google.common.collect.Lists;
import com.ldtteam.blockout.builder.data.builder.BlockOutGuiConstructionDataBuilder;
import com.ldtteam.blockout.element.advanced.List;
import com.ldtteam.blockout.element.simple.Button;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout_test.context.BindingTestContext;
import com.ldtteam.blockout_test.tests.IBlockOutUITest;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GrowingListWithButtonTest implements IBlockOutUITest
{
    @NotNull
    @Override
    public String getTestName()
    {
        return TextFormatting.RED + "Dynamic list" + TextFormatting.RESET;
    }

    @Override
    public void onTestButtonClicked(
      final EntityPlayerMP entityPlayer, final Button button, final Button.ButtonClickedEventArgs eventArgs)
    {
        final ArrayList<BindingTestContext> list = Lists.newArrayList();

        //TODO: Fix list builder not taking consumer!.

        ProxyHolder.getInstance().getGuiController().openUI(entityPlayer, iGuiKeyBuilder -> iGuiKeyBuilder
                                                                                              .ofFile(new ResourceLocation("blockout_test:gui/dynamic_list.json"))
                                                                                              .usingData(b -> b
                                                                                                                .withControl("add_button",
                                                                                                                  Button.ButtonConstructionDataBuilder.class,
                                                                                                                  bb -> bb
                                                                                                                          .withClickedEventHandler((bu, e) -> {
                                                                                                                              if (!e.isStart())
                                                                                                                              {
                                                                                                                                  return;
                                                                                                                              }

                                                                                                                              list.add(new BindingTestContext(
                                                                                                                                "Entry: " + (list.size() + 1)));
                                                                                                                          }))
                                                                                                                .withControl("content_list",
                                                                                                                  List.ListConstructionDataBuilder.class,
                                                                                                                  ll -> ll.withDataContext(list)
                                                                                                                          .withTemplateConstructionData(new BlockOutGuiConstructionDataBuilder()
                                                                                                                                                          .withControl(
                                                                                                                                                            "delete_button",
                                                                                                                                                            Button.ButtonConstructionDataBuilder.class,
                                                                                                                                                            bb -> bb.withClickedEventHandler(
                                                                                                                                                              (bu, e) -> list.remove(
                                                                                                                                                                bu.getDataContext())))
                                                                                                                                                          .build()))
                                                                                              )
                                                                                              .withDefaultItemHandlerManager()
                                                                                              .forEntity(entityPlayer));
    }
}
