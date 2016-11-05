package pl.setblack.exp.galakpizza.domain;

import java.io.Serializable;

public class Order implements Serializable {

    public final String planet;
    public final Variant variant;
    public final Size size;

    public Order(String planet, Variant variant, Size size) {
        this.planet = planet;
        this.size = size;
        this.variant = variant;
    }
}
