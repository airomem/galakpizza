package pl.setblack.exp.galakpizza.system;

import pl.setblack.exp.galakpizza.domain.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class PlanetOrders implements Serializable, Comparable<PlanetOrders> {
    final String name;

    private List<Order> orders = new ArrayList<>();

    public PlanetOrders(String name) {
        this.name = name;
    }

    void assignOrder(final Order order) {
        this.orders.add(order);
    }

    @Override
    public int compareTo(final PlanetOrders other) {
        return other.orders.size() - this.orders.size();
    }

    List<Order> takeOrders() {
        final List<Order> result = new ArrayList<>(this.orders);
        this.orders = new ArrayList<>();
        return result;
    }
}
