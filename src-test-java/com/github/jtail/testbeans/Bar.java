package com.github.jtail.testbeans;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Bar {
    public final static String NO_BEER_NO_BAR = "No beer - no bar";

    @NotNull(message = NO_BEER_NO_BAR)
    private String beer;
}

