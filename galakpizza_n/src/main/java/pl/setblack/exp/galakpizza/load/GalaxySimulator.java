package pl.setblack.exp.galakpizza.load;

import pl.setblack.exp.galakpizza.api.GalakPizzaService;
import pl.setblack.exp.galakpizza.system.GalakPizza;
import pl.setblack.exp.galakpizza.system.GalakPizzaCore;

import java.util.ArrayList;
import java.util.List;

public class GalaxySimulator {

    public static final int SECONDS_TOTAL = 10;
    private final GalakPizzaService galakPizza;

    public GalaxySimulator(GalakPizzaService galakPizza) {
        this.galakPizza = galakPizza;
    }

    public static void main(String... args) {
        runSimulation(5000);
        System.gc();
        sleep();
        long orders = runSimulation(SECONDS_TOTAL*1000);
        System.out.println("Orders placed:" + orders);
        System.out.println("O/S:" + (double)orders/(double) SECONDS_TOTAL);
    }

    private static long runSimulation(long time) {
        final GalakPizza gp = new GalakPizza();
        final GalaxySimulator simulator = new GalaxySimulator(gp);
        final long result =   simulator.runGalaxy(time, 4, 1);
        gp.close();
        return result;
    }

    public long runGalaxy(long time, int clientThreads, int deliveryThreads) {
        final List<OrdersSimulator> clients = new ArrayList<>();
        final List<DeliverySimulator> delivery = new ArrayList<>();
        for (int i = 0; i < clientThreads; i++) {
            clients.add(new OrdersSimulator(galakPizza));
        }
        for (int i = 0; i < deliveryThreads; i++) {
            delivery.add(new DeliverySimulator(galakPizza));
        }

        clients.forEach(c -> new Thread(c).start());
        delivery.forEach(c -> new Thread(c).start());
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        clients.forEach(cl ->cl.stopSimulation());
        delivery.forEach( dl ->dl.stopSimulation());

        sleep();

        final long orders = clients.stream().map(c -> c.getFinalState()).reduce(0L, (x, y) -> x + y);
        final long performedOrders = delivery.stream().map(d -> d.getFinalState()).reduce(0L, (x, y) -> x + y);

        final long standingOrders = galakPizza.countStandingOrders();

        if( standingOrders+performedOrders != orders ) {
            throw new IllegalStateException("oops [" +standingOrders + "," + performedOrders+"]  "
                    +(standingOrders+performedOrders)+ " <> "+orders );
        }
        return performedOrders;
    }

    private static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
