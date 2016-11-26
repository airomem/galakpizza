package galakpizza;

import pl.setblack.airomem.core.Persistent;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

public class GalakPizza implements GalakPizzaService {
    final Persistent<GalakPizzaCore> controller;

    final Path storeFolder = Paths.get("pizzaStore");

    public GalakPizza() {
        controller = Persistent.loadOptional(storeFolder, () -> new GalakPizzaCore());
    }

    public List<Order> takeOrdersFromBestPlanet() {
        return controller.executeAndQuery(core -> core.takeOrdersFromBestPlanet());
    }

    public long countStandingOrders() {
        return controller.query(c -> c.countStandingOrders());
    }

    public void placeOrder(final String planet, final Variant variant, final Size size) {
        controller.execute(core -> core.placeOrder(planet, variant, size));
    }


    public Collection<PlanetSummary> getPlanetsSummary() {
        return controller.query(c -> c.getPlanetsSummary());
    }

    public void close() {
        controller.close();
    }

}
