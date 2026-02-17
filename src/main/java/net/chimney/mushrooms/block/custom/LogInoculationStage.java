package net.chimney.mushrooms.block.custom;

import net.minecraft.util.StringRepresentable;

public enum LogInoculationStage implements StringRepresentable {
    INOCULATED("inoculated"),          // Stage 0: Just inoculated
    COLONIZING("colonizing"),          // Stage 1: Mycelium spreading (we'll use stage 2 assets for now)
    FULLY_COLONIZED("fully_colonized"); // Stage 2: Ready to fruit

    private final String name;

    LogInoculationStage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public LogInoculationStage getNext() {
        return switch (this) {
            case INOCULATED -> COLONIZING;
            case COLONIZING -> FULLY_COLONIZED;
            case FULLY_COLONIZED -> FULLY_COLONIZED;
        };
    }

    public String getTextureSuffix() {
        return switch (this) {
            case INOCULATED -> "0";  // Will use final stage for now
            case COLONIZING -> "1";  // Will use final stage for now
            case FULLY_COLONIZED -> "2";
        };
    }
}
