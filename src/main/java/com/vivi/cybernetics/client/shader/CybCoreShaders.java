package com.vivi.cybernetics.client.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.vivi.cybernetics.Cybernetics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class CybCoreShaders implements ResourceManagerReloadListener {
    private static final CybCoreShaders INSTANCE = new CybCoreShaders();
    private CybCoreShaders() {

    }
    public static CybCoreShaders getInstance() {
        return INSTANCE;
    }


    private final List<ShaderReference> shaders = new ArrayList<>();

    private ShaderInstance scanShader;

    public static ShaderInstance getScanShader() {
        return INSTANCE.scanShader;
    }

    public void init() {
        shaders.add(new ShaderReference("scan", DefaultVertexFormat.POSITION_TEX, shader -> scanShader = shader));

        Minecraft.getInstance().submitAsync(() -> {
            final ResourceManager manager = Minecraft.getInstance().getResourceManager();
            this.onResourceManagerReload(manager);
            if (manager instanceof ReloadableResourceManager reloadableManager) {
                reloadableManager.registerReloadListener(this);
            }
        });
    }


    @Override
    public void onResourceManagerReload(ResourceManager pResourceManager) {
        RenderSystem.assertOnRenderThread();
        shaders.forEach(reference -> reference.reload(pResourceManager));
    }

    /**
     * Borrowed from <a href=https://github.com/MightyPirates/Scannable/blob/1.20.1/common/src/main/java/li/cil/scannable/client/shader/Shaders.java>Scannable</a>,
     * which is under MIT license
     */
    private static final class ShaderReference {
        private static final Logger LOGGER = LogManager.getLogger();

        private final String name;
        private final VertexFormat format;
        private final Consumer<ShaderInstance> reloadAction;
        private ShaderInstance shader;

        public ShaderReference(final String name, final VertexFormat format, final Consumer<ShaderInstance> reloadAction) {
            this.name = name;
            this.format = format;
            this.reloadAction = reloadAction;
        }

        public void reload(final ResourceProvider provider) {
            if (shader != null) {
                shader.close();
                shader = null;
            }

            try {
                shader = new ShaderInstance(location -> provider.getResource(new ResourceLocation(Cybernetics.MOD_ID, location.getPath())).or(() -> provider.getResource(location)), name, format);
            } catch (final Exception e) {
                LOGGER.error(e);
            }

            reloadAction.accept(shader);
        }
    }
}
