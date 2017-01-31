package pl.setblack.exp.galakpizza.system;

import pl.setblack.exp.galakpizza.domain.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class PlanetOrders implements Serializable {
    final String name;

    private List<Order> orders = new ArrayList<>();

    public PlanetOrders(String name) {
        this.name = name;
    }

    void assignOrder(final Order order) {
        this.orders.add(order);
    }

    List<Order> takeOrders() {
        final List<Order> result = new ArrayList<>(this.orders);
        this.orders = new ArrayList<>();
        return result;
    }

    public int size() {
        return this.orders.size();
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
