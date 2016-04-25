package pl.setblack.exp.galakpizza.api;

import pl.setblack.exp.galakpizza.domain.Order;

import java.util.List;

public interface GalakPizzaDelivery {
    List<Order> takeOrdersFromBestPlanet();

    long countStandingOrders();

}
