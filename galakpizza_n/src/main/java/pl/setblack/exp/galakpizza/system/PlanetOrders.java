package pl.setblack.exp.galakpizza.system;

import pl.setblack.exp.galakpizza.domain.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class PlanetOrders implements Serializable, Comparable<PlanetOrders> {
    final String name;

    private List<Order> orders = new ArrayList<>();

    private boolean emptied = true;

    public PlanetOrders(String name) {
        this.name = name;
    }

    void assignOrder(final Order order) {
        this.orders.add(order);
        this.emptied = false;
    }

    @Override
    public int compareTo(final PlanetOrders other) {
        return other.orders.size() - this.orders.size();
    }

    List<Order> takeOrders() {
        final List<Order> result = new ArrayList<>(this.orders);
        this.orders = new ArrayList<>();
        this.emptied = true;
        return result;
    }

    public boolean isEmpty() {
        return emptied;
    }

    public static class Wrapper implements  Comparable<Wrapper>,Serializable{
        final PlanetOrders porders;

        final int size;

        public Wrapper(PlanetOrders porders) {
            this.porders = porders;
            this.size =  porders.orders.size();
        }

        @Override
        public int compareTo(final Wrapper other) {
            return other.size - this.size;
        }
    }
}
