package com.github.jtail.testbeans;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Bean for validation array.
 */
@Getter
@Setter
public class Chicken {
    @Max(value = 2, message = "Chickens have no more than 2 legs")
    @Min(value = 0, message = "A schicken with no legs can be quite tasty, but we do not expect negative numbers here")
    private int legs;

    @NotNull(message = "Body must be present")
    private String body;

    /** Head is higly optional */
    private String head;
}

