package galakpizza;

import com.fasterxml.jackson.annotation.JsonProperty;

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
