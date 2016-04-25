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

    public void close() {
        controller.erase();
    }

    private void deleteFiles() {
        try {
            String path = System.getProperty("user.home")
                    + File.separator
                    + "prevayler"
                    + File.separator
                    + "pizza";
            FileUtils.deleteDirectory(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> takeOrdersFromBestPlanet() {
        return controller.executeAndQuery( core->core.takeOrdersFromBestPlanet());
    }

    @Override
    public long countStandingOrders() {
        return controller.query( c->c.countStandingOrders());
    }

    @Override
    public long placeOrder(final String planet, final Variant variant, final Size size) {
        return controller.executeAndQuery( core -> core.placeOrder(planet,variant,size));
    }


}
