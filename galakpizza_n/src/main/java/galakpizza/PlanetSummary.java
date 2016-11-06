package galakpizza;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlanetSummary {
    public final String name;
    public final long counter;

    public PlanetSummary(
            @JsonProperty("name") String name,
            @JsonProperty("counter") long counter) {
        this.name = name;
        this.counter = counter;
    }
}
