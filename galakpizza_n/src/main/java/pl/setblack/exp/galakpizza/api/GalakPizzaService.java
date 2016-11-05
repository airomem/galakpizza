package pl.setblack.exp.galakpizza.api;

import pl.setblack.exp.galakpizza.domain.Order;
import pl.setblack.exp.galakpizza.domain.Size;
import pl.setblack.exp.galakpizza.domain.Variant;

import java.util.Collection;
import java.util.List;

public interface GalakPizzaService  {
    void placeOrder(String planet, Variant variant, Size size);

    List<Order> takeOrdersFromBestPlanet();

    long countStandingOrders();

    Collection<PlanetSummary> getPlanetsSummary();
}
