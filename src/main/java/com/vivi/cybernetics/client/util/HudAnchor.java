package com.vivi.cybernetics.client.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Adapted from <a href=https://www.curseforge.com/minecraft/mc-mods/dirtbagutil>DirtbagUtil</a> under LGPL v3
 */
@SuppressWarnings("IntegerDivisionInFloatingPointContext")
public enum HudAnchor implements StringRepresentable {

    TOP_LEFT("top_left", (w, h, x, y) -> new Vec2(x, y)),
    TOP_MIDDLE("top_middle", (w, h, x, y) -> new Vec2((w / 2) + x, y)),
    TOP_RIGHT("top_right", (w, h, x, y) -> new Vec2(w + x, y)),
    MIDDLE_LEFT("middle_left", (w, h, x, y) -> new Vec2(x, (h / 2) + y)),
    MIDDLE("middle", (w, h, x, y) -> new Vec2((w / 2) + x, (h / 2) + y)),
    MIDDLE_RIGHT("middle_right", (w, h, x, y) -> new Vec2(w + x, (h / 2) + y)),
    BOTTOM_LEFT("bottom_left", (w, h, x, y) -> new Vec2(x, h + y)),
    BOTTOM_MIDDLE("bottom_middle", (w, h, x, y) -> new Vec2((w / 2) + x, h + y)),
    BOTTOM_RIGHT("bottom_right", (w, h, x, y) -> new Vec2(w + x, h + y));


    private static final Map<String, HudAnchor> ID_MAP = ImmutableMap.copyOf(Arrays.stream(values())
            .collect(Collectors.<HudAnchor, String, HudAnchor>toMap(HudAnchor::getSerializedName, Function.identity())));
    private final String name;
    private final IWithOffset function;

    HudAnchor(final String name, final IWithOffset function) {
        this.name = name;
        this.function = function;
    }

    public static HudAnchor getByName(final String name) {
        return ID_MAP.getOrDefault(name, TOP_LEFT);
    }

    public Vec2 getWithOffset(final int screenWidth, final int screenHeight, final int offsetX, final int offsetY) {
        return function.apply(screenWidth, screenHeight, offsetX, offsetY);
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }


    @FunctionalInterface
    public interface IWithOffset {
        Vec2 apply(final int screenWidth, final int screenHeight, final int offsetX, final int offsetY);
    }

}
