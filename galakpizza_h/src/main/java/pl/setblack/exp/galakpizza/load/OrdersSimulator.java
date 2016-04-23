package pl.setblack.exp.galakpizza.load;

import pl.setblack.exp.galakpizza.api.GalakPizzaOrders;
import pl.setblack.exp.galakpizza.domain.Size;
import pl.setblack.exp.galakpizza.domain.Variant;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class OrdersSimulator implements Runnable {
    final GalakPizzaOrders myPizza;

    final Random rnd = new Random(137);

    final AtomicBoolean stopped = new AtomicBoolean(false);

    final AtomicLong counter = new AtomicLong(0);

    public OrdersSimulator(GalakPizzaOrders myPizza) {
        this.myPizza = myPizza;
    }

    @Override
    public void run() {
        while (!stopped.get()) {
            placeRandomOrder();
        }
    }

    public void placeRandomOrder() {
        final String planetName = "planet" + rnd.nextInt(100000);
        final Variant var = Variant.values()[rnd.nextInt(Variant.values().length)];
        final Size size = Size.values()[rnd.nextInt(Size.values().length)];
        this.myPizza.placeOrder(planetName, var, size);
        this.counter.getAndIncrement();
    }

    public long stopSimulation() {
        this.stopped.lazySet(true);
        return this.counter.get();
    }

    public long getFinalState() {
        return this.counter.get();
    }
}
