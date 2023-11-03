package com.vivi.cybernetics.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.BooleanValue enableBerserkShaders;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        setupConfg(builder);
        SPEC = builder.build();
    }

    private static void setupConfg(ForgeConfigSpec.Builder builder) {

        enableBerserkShaders = builder
                .comment("Enables or disables the shaders when using the Berserk. Defaults to true.")
                .define("enableBerserkShaders", true);


    }
}
