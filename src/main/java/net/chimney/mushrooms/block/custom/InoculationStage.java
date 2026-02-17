package net.chimney.mushrooms.block.custom;

import net.minecraft.util.StringRepresentable;

public enum InoculationStage implements StringRepresentable {
    INOCULATED("inoculated"),
    IN_PROGRESS("in_progress"),
    FULLY_INOCULATED("fully_inoculated");

    private final String name;

    InoculationStage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getSerializedName() {
        return name;
    }

    public InoculationStage getNext() {
        return switch (this) {
            case INOCULATED -> IN_PROGRESS;
            case IN_PROGRESS -> FULLY_INOCULATED;
            case FULLY_INOCULATED -> FULLY_INOCULATED; // Stays at final stage
        };
    }
}
