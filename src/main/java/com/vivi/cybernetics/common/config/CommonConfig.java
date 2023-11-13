package com.vivi.cybernetics.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.IntValue initialCapacity;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        setupConfg(builder);
        SPEC = builder.build();
    }

    private static void setupConfg(ForgeConfigSpec.Builder builder) {

        initialCapacity = builder
                .comment("Initial capacity for the player, without any capacity shards. Defaults to 50.")
                .defineInRange("initialCapacity", 50, 1, 1024);
    }
}
