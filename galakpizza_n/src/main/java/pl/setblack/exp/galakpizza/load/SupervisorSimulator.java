package pl.setblack.exp.galakpizza.load;

import pl.setblack.exp.galakpizza.api.GalakPizzaDelivery;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class SupervisorSimulator implements Runnable {

    private  final GalakPizzaDelivery  deliveryService;


    final AtomicBoolean stopped = new AtomicBoolean(false);

    final AtomicLong totalSeen = new AtomicLong(0);

    final AtomicLong  timesSeen = new AtomicLong(0);


    public SupervisorSimulator(GalakPizzaDelivery deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Override
    public void run() {
        while( !stopped.get())  {
            final long orders = deliveryService.countStandingOrders();
            this.totalSeen.addAndGet(orders);
            timesSeen.incrementAndGet();
        }
    }


    public long stopSimulation() {
        this.stopped.lazySet(true);
        return this.totalSeen.get();
    }

    public long getFinalState() {
        return this.timesSeen.get();
    }
}
