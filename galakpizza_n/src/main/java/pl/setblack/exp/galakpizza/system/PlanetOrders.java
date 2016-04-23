package pl.setblack.exp.galakpizza.system;

import pl.setblack.exp.galakpizza.domain.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


class PlanetOrders implements Serializable, Comparable<PlanetOrders> {
    final String name;

    private List<Order> orders = new ArrayList<>();

    final AtomicInteger size = new AtomicInteger(0);

    public PlanetOrders(String name) {
        this.name = name;
    }

    synchronized void assignOrder(final Order order) {

        this.orders.add(order);
        this.size.incrementAndGet();

    }

    @Override
    public int compareTo(final PlanetOrders other) {
        return other.size.get() - this.size.get();
    }

    synchronized List<Order> takeOrders() {

        final List<Order> result = new ArrayList<>(this.orders);
        this.orders = new ArrayList<>();
        this.size.set(0);
        return result;

    }
}
