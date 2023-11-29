package com.vivi.cybernetics.common.ability;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks class as a hidden ability, meaning it won't appear in the ability wheel and it should be enabled/disabled elsewhere in code.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HiddenAbility {
}