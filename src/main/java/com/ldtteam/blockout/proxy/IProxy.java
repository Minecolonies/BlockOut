package com.ldtteam.blockout.proxy;

import com.ldtteam.blockout.connector.core.IGuiController;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.connector.core.ILoaderManager;
import com.ldtteam.blockout.connector.core.IUIElementFactoryController;
import com.ldtteam.blockout.core.management.IUIManager;
import com.ldtteam.blockout.core.management.network.INetworkManager;
import com.ldtteam.blockout.core.management.render.IRenderManager;
import com.ldtteam.blockout.core.management.update.IUpdateManager;
import com.ldtteam.blockout.style.core.IStyleManager;
import com.ldtteam.blockout.style.core.resources.loader.IResourceLoaderManager;
import com.ldtteam.blockout.template.ITemplateEngine;
import com.ldtteam.blockout.util.color.MultiColoredFontRenderer;
import com.ldtteam.blockout.util.math.Vector2d;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public interface IProxy
{
    void onPreInit();

    @NotNull
    IGuiController getGuiController();

    @NotNull
    ILoaderManager getLoaderManager();

    @NotNull
    IUIElementFactoryController getFactoryController();

    @Nullable
    InputStream getResourceStream(@NotNull ResourceLocation location) throws Exception;

    @NotNull
    Vector2d getImageSize(@NotNull final ResourceLocation location);

    @NotNull
    INetworkManager generateNewNetworkManagerForGui(@NotNull final IGuiKey key);

    @NotNull
    IUpdateManager generateNewUpdateManager(@NotNull final IUIManager manager);

    @NotNull
    World getWorldFromDimensionId(@NotNull final int dimId);

    @SideOnly(Side.CLIENT)
    @NotNull
    IRenderManager generateNewRenderManager();

    void initializeFontRenderer();

    @NotNull
    MultiColoredFontRenderer getFontRenderer();

    @NotNull
    IResourceLoaderManager getResourceLoaderManager();

    @NotNull
    IStyleManager getStyleManager();

    @NotNull
    ITemplateEngine getTemplateEngine();

    @NotNull
    IGuiController getClientSideOnlyGuiController();
}
