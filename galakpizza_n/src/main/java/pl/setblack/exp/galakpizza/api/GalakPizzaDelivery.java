package pl.setblack.exp.galakpizza.api;

import pl.setblack.exp.galakpizza.domain.Order;
import pl.setblack.exp.galakpizza.domain.Size;
import pl.setblack.exp.galakpizza.domain.Variant;

import java.util.List;

public interface GalakPizzaDelivery {
    List<Order> takeOrdersFromBestPlanet();

    long countStandingOrders();
}
