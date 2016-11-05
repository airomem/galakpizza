package pl.setblack.exp.galakpizza.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.setblack.exp.galakpizza.domain.Order;
import pl.setblack.exp.galakpizza.domain.Size;
import pl.setblack.exp.galakpizza.domain.Variant;

public class OutgoingOrder {
    public final String planet;
    public final Variant variant;
    public final Size size;

    public OutgoingOrder(
            @JsonProperty("planet") String planet,
            @JsonProperty("variant")  Variant variant,
            @JsonProperty("size") Size size) {
        this.planet = planet;
        this.variant = variant;
        this.size = size;
    }

    public static OutgoingOrder fromOrder(final Order o) {
            return new OutgoingOrder( o.planet, o.variant, o.size);
    }
}
