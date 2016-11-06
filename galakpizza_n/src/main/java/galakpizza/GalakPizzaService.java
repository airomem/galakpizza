package galakpizza;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.List;

public interface GalakPizzaService  {
    void placeOrder(String planet, Variant variant, Size size);

    List<Order> takeOrdersFromBestPlanet();

    long countStandingOrders();

    Collection<PlanetSummary> getPlanetsSummary();
}

