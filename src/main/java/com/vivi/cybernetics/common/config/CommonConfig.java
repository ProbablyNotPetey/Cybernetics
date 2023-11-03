package com.vivi.cybernetics.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

    public static final ForgeConfigSpec SPEC;



    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        setupConfg(builder);
        SPEC = builder.build();
    }

    private static void setupConfg(ForgeConfigSpec.Builder builder) {

    }
}
