package com.vivi.cybernetics.common.item;

public class CapacityCyberwareItem extends CyberwareItem {

    private final float value;
    private final Operation operation;
    public CapacityCyberwareItem(Properties pProperties, float value, Operation operation) {
        super(pProperties);
        this.value = value;
        this.operation = operation;
    }

    public float getValue() {
        return value;
    }

    public Operation getOperation() {
        return operation;
    }

    public static enum Operation {
        ADDITION,
        MULTIPLY
    }
}
