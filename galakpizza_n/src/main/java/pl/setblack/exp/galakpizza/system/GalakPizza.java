package pl.setblack.exp.galakpizza.system;

import pl.setblack.airomem.core.Command;
import pl.setblack.airomem.core.PersistenceController;
import pl.setblack.airomem.core.builders.PrevaylerBuilder;
import pl.setblack.exp.galakpizza.api.GalakPizzaService;
import pl.setblack.exp.galakpizza.api.PlanetSummary;
import pl.setblack.exp.galakpizza.domain.Order;
import pl.setblack.exp.galakpizza.domain.Size;
import pl.setblack.exp.galakpizza.domain.Variant;

import java.util.Collection;
import java.util.List;

public class GalakPizza implements GalakPizzaService {
        final PersistenceController<GalakPizzaCore,GalakPizzaCore> controller;

    public GalakPizza() {
        controller = PrevaylerBuilder
                .newBuilder()
                .withinUserFolder("pizza")
                .useSupplier( () -> new GalakPizzaCore())
                .disableRoyalFoodTester()
                .build();
    }

    public void destroy() {
        controller.erase();
    }

    public void safeClose() {
        controller.close();
    }

    @Override
    public List<Order> takeOrdersFromBestPlanet() {
        return controller.executeAndQuery( core -> core.takeOrdersFromBestPlanet());
    }

    @Override
    public long countStandingOrders() {
        return controller.query( c->c.countStandingOrders());
    }

    @Override
    public void placeOrder(final String planet, final Variant variant, final Size size) {
        controller.execute( core -> core.placeOrder(planet, variant, size));
    }

    @Override
    public Collection<PlanetSummary> getPlanetsSummary() {
        return controller.query(c -> c.getPlanetsSummary());
    }
}
