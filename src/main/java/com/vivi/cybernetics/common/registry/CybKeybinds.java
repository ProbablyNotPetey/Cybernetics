package com.vivi.cybernetics.common.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class CybKeybinds {

    public static final KeyMapping PLAYER_CYBERWARE_MENU = new KeyMapping("key.cybernetics.player_cyberware_menu", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, "key.cybernetics.category");

    public static final KeyMapping PLAYER_ABILITIES_MENU = new KeyMapping("key.cybernetics.player_abilities_menu", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.cybernetics.category");
    public static final KeyMapping DASH = new KeyMapping("key.cybernetics.dash", KeyConflictContext.UNIVERSAL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, "key.cybernetics.category");
}
