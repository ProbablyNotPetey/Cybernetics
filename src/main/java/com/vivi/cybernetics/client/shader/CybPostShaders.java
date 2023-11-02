package com.vivi.cybernetics.client.shader;

import com.vivi.cybernetics.Cybernetics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CybPostShaders implements ResourceManagerReloadListener {
    private static final CybPostShaders INSTANCE = new CybPostShaders();

    private final List<PostChain> shaders = new ArrayList<>();

    private PostChain berserk;


    private CybPostShaders() {

    }

    public static CybPostShaders getInstance() {
        return INSTANCE;
    }

    public void init() {
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
        clear();
        try {
            berserk = createPostChain(new ResourceLocation(Cybernetics.MOD_ID, "berserk"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        shaders.forEach(PostChain::close);
        shaders.clear();
    }

    public PostChain createPostChain(ResourceLocation location) throws IOException {
        return new PostChain(Minecraft.getInstance().textureManager, Minecraft.getInstance().getResourceManager(), Minecraft.getInstance().getMainRenderTarget(), new ResourceLocation(location.getNamespace(), "shaders/post/" + location.getPath() + ".json"));
    }

    public PostChain getBerserk() {
        return berserk;
    }
}
