package com.github.jtail.testbeans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Point {
    @DecimalMax("100.0")
    private double x;
    @DecimalMax("100.0")
    private double y;
}
