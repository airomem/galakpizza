package pl.setblack.exp.galakpizza.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.setblack.exp.galakpizza.domain.Size;
import pl.setblack.exp.galakpizza.domain.Variant;

public class IncomingOrder {
    final String planet;
    final Variant variant;
    final Size size;

    public IncomingOrder(
            @JsonProperty("planet") String planet,
            @JsonProperty("variant")  Variant variant,
            @JsonProperty("size") Size size) {
        this.planet = planet;
        this.variant = variant;
        this.size = size;
    }
}
