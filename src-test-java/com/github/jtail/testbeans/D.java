package com.github.jtail.testbeans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class D<T> {
    @Valid
    private T x;
    @Valid
    private T y;
}
