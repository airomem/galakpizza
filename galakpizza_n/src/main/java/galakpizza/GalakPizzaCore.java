package galakpizza;

import pl.setblack.airomem.core.Storable;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


public class GalakPizzaCore implements GalakPizzaService, Serializable {
    private static final long serialVersionUID = 1L;
    private final HashMap<String, PlanetOrders> orders = new HashMap<>();

    private AtomicLong ordersTotal = new AtomicLong(0);

    public void placeOrder(String planet, Variant variant, Size size) {
        final Order order = new Order(planet, variant, size);
        assignOrderToPlanet(order);
        ordersTotal.incrementAndGet();
    }

    private void assignOrderToPlanet(Order order) {
        final PlanetOrders po = orders.computeIfAbsent(order.planet,
                planetName -> new PlanetOrders());
        po.assignOrder(order);
    }

    public List<Order> takeOrdersFromBestPlanet() {
        final Optional<String> bestPlanet = this.orders.entrySet()
                .stream()
                .sorted( (o1, o2) -> o1.getValue().compareTo(o2.getValue()))
                .findFirst()
                .map( e -> e.getKey());
        final List<Order> result =  bestPlanet.flatMap(p -> Optional.ofNullable(this.orders.get(p)))
                .map(p->p.takeOrders()).orElse(Collections.EMPTY_LIST);
        this.ordersTotal.addAndGet(-result.size());
        return result;
    }

    public long countStandingOrders() {
        return this.ordersTotal.get();
    }


    public Collection<PlanetSummary> getPlanetsSummary() {
        return this.orders
                .entrySet()
                .stream()
                .map((entry)-> new PlanetSummary(entry.getKey(), entry.getValue().size()))
                .filter(p -> p.counter > 0)
                .collect(Collectors.toList());
    }
}
