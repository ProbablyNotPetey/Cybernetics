package com.vivi.cybernetics.common.attribute;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class RangedIntAttribute extends Attribute {
    /** The lowest possible value for the attribute. */
    private final int minValue;
    /** The highest possible value for the attribute. */
    private final int maxValue;

    public RangedIntAttribute(String descriptionId, int defaultValue, int min, int max) {
        super(descriptionId, defaultValue);
        this.minValue = min;
        this.maxValue = max;
        if (min > max) {
            throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
        } else if (defaultValue < min) {
            throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
        } else if (defaultValue > max) {
            throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
        }
    }

    /**
     * Gets the lowest possible value for the attribute.
     * @return The lowest possible value for the attribute; {@link #minValue}.
     */
    public int getMinValue() {
        return this.minValue;
    }

    /**
     * Gets the highest possible value for the attribute.
     * @return The highest possible value for the attribute; {@link #maxValue}.
     */
    public int getMaxValue() {
        return this.maxValue;
    }

    /**
     * Sanitizes the value of the attribute to fit within the expected parameter range of the attribute.
     * @return The sanitized attribute value.
     * @param pValue The value of the attribute to sanitize.
     */
    public double sanitizeValue(double pValue) {
        return Double.isNaN(pValue) ? this.minValue : (int) (Mth.clamp(pValue, this.minValue, this.maxValue));
    }
}