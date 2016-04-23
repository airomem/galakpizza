package pl.setblack.exp.galakpizza.load;

import pl.setblack.exp.galakpizza.api.GalakPizzaDelivery;
import pl.setblack.exp.galakpizza.domain.Order;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class DeliverySimulator implements Runnable {

    private  final GalakPizzaDelivery  deliveryService;

    final Random rnd = new Random(631);

    final AtomicBoolean stopped = new AtomicBoolean(false);

    final AtomicLong counter = new AtomicLong(0);

    public DeliverySimulator(GalakPizzaDelivery deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Override
    public void run() {
        while( !stopped.get())  {
            final long servedOnPlanet = serveBestPlanet();
            this.counter.getAndAdd(servedOnPlanet);
        }
    }

    public long serveBestPlanet() {
        final List<Order> orders = this.deliveryService.takeOrdersFromBestPlanet();
        return orders.size();
    }
    public long stopSimulation() {
        this.stopped.lazySet(true);
        return this.counter.get();
    }

    public long getFinalState() {
        return this.counter.get();
    }
}
