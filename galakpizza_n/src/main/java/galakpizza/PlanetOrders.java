package galakpizza;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class PlanetOrders implements Serializable, Comparable<PlanetOrders> {
    private static final long serialVersionUID = 1L;

    private ArrayList<Order> orders ;

    public PlanetOrders() {
        this( new ArrayList<>());
    }

    public PlanetOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    long size() {
        return this.orders.size();
    }

    void assignOrder(final Order order) {
        this.orders.add(order);
    }

    List<Order> takeOrders() {
        final List<Order> result = new ArrayList<>(this.orders);
        this.orders = new ArrayList<>();
        return result;
    }

    public int compareTo(PlanetOrders o) {
        return o.orders.size() - this.orders.size() ;
    }
}
