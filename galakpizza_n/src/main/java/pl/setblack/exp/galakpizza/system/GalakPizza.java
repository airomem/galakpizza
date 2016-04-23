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
    final TakeOrders takeOrdersCmd = new TakeOrders();
    public GalakPizza() {
        controller = PrevaylerBuilder
                .newBuilder()
                .withinUserFolder("pizza")
                .withJournalFastSerialization(true)
                .useSupplier( () -> new GalakPizzaCore())
                .disableRoyalFoodTester()
                .build();
    }

    public void close() {
        controller.close();
        try {
            FileUtils.deleteDirectory(new File("/home/jarek/pizza"));
        } catch (IOException  e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Order> takeOrdersFromBestPlanet() {
        return controller.executeAndQuery( takeOrdersCmd);
    }

    @Override
    public long countStandingOrders() {
        return controller.executeAndQuery(core -> core.countStandingOrders());
    }

    @Override
    public long placeOrder(final String planet, final Variant variant, final Size size) {
        return controller.executeAndQuery(new PlaceOrderEvent(planet, variant, size));
    }

    private static class PlaceOrderEvent implements Command<GalakPizzaCore, Long >, Serializable {
        public final String planet;
        public final Variant variant;
        public final Size size;

        public PlaceOrderEvent(String planet, Variant variant, Size size) {
            this.planet = planet;
            this.variant = variant;
            this.size = size;
        }
        @Override
        public Long execute(GalakPizzaCore galakPizzaCore) {
            return galakPizzaCore.placeOrder(planet, variant, size);
        }
    }

    private static class TakeOrders implements Command<GalakPizzaCore, List<Order> > {
        @Override
        public List<Order> execute(GalakPizzaCore galakPizzaCore) {
            return galakPizzaCore.takeOrdersFromBestPlanet();
        }
    }
}
