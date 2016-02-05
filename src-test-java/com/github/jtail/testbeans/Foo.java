package com.github.jtail.testbeans;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
public class Foo {
    @Valid
    private Bar valid;

    private Bar unverified;
}

