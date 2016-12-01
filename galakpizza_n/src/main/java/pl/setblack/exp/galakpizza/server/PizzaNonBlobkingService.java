package pl.setblack.exp.galakpizza.server;

import pl.setblack.exp.galakpizza.api.GalakPizzaService;
import pl.setblack.exp.galakpizza.domain.Order;
import pl.setblack.exp.galakpizza.domain.Size;
import pl.setblack.exp.galakpizza.domain.Variant;
import pl.setblack.exp.galakpizza.system.GalakPizza;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PizzaNonBlobkingService {
    private final Executor writesExecutor = Executors.newSingleThreadExecutor();

    private final GalakPizzaService galakPizza;

    public PizzaNonBlobkingService(GalakPizzaService gp) {
        galakPizza = gp;
    }


    public void placeOrder(String planet, Variant variant, Size size) {
        writesExecutor.execute( ()->galakPizza.placeOrder(planet,variant,size));
    }


    public CompletionStage<List<Order>> takeOrdersFromBestPlanet() {
        final CompletableFuture<List<Order>> futureOrders = new CompletableFuture<>();
        writesExecutor.execute( ()->{
            futureOrders.complete(galakPizza.takeOrdersFromBestPlanet());
        });
        return futureOrders;
    }


    public long countStandingOrders() {
        return galakPizza.countStandingOrders();
    }
}
