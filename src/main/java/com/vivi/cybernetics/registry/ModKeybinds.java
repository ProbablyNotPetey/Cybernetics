package com.vivi.cybernetics.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {

    public static final KeyMapping PLAYER_CYBERWARE_MENU = new KeyMapping("key.cybernetics.player_cyberware_menu", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, "key.cybernetics.category");
}
