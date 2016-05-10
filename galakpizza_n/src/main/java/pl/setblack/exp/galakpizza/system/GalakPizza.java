package pl.setblack.exp.galakpizza.system;

import org.apache.commons.io.FileUtils;
import pl.setblack.airomem.core.Command;
import pl.setblack.airomem.core.PersistenceController;
import pl.setblack.airomem.core.SimpleController;
import pl.setblack.airomem.core.builders.PersistenceFactory;
import pl.setblack.airomem.core.builders.PrevaylerBuilder;
import pl.setblack.exp.galakpizza.api.GalakPizzaService;
import pl.setblack.exp.galakpizza.domain.Order;
import pl.setblack.exp.galakpizza.domain.Size;
import pl.setblack.exp.galakpizza.domain.Variant;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
        return controller.executeAndQuery( takeBest);
    }

    @Override
    public long countStandingOrders() {
        return controller.query( c->c.countStandingOrders());
    }

    @Override
    public long placeOrder(final String planet, final Variant variant, final Size size) {
        return controller.executeAndQuery( new PlaceOrderCommand(planet,variant,size));
    }

    private static final class TakeBestOrdersCommand implements Command<GalakPizzaCore, List<Order>> {
        @Override
        public List<Order> execute(GalakPizzaCore system) {
            return system.takeOrdersFromBestPlanet();
        }
    }
    private static final class PlaceOrderCommand implements Command<GalakPizzaCore, Long> {
        private final String planet;
        private final Variant variant;
        private final Size size;

        public PlaceOrderCommand(String planet, Variant variant, Size size) {
            this.planet = planet;
            this.variant = variant;
            this.size = size;
        }

        @Override
        public Long execute(GalakPizzaCore system) {
            return system.placeOrder(planet,variant, size);
        }
    }

    final TakeBestOrdersCommand takeBest = new TakeBestOrdersCommand();
}
