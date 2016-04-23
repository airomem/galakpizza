package pl.setblack.exp.galakpizza.system;

import pl.setblack.exp.galakpizza.api.GalakPizzaService;
import pl.setblack.exp.galakpizza.domain.Order;
import pl.setblack.exp.galakpizza.domain.Size;
import pl.setblack.exp.galakpizza.domain.Variant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class GalakPizza implements GalakPizzaService {
    private final Map<String, PlanetOrders> orders = new ConcurrentHashMap<>();

    private final AtomicLong orderSequence = new AtomicLong(0);

    private PriorityQueue<PlanetOrders> bestPlanets = new PriorityQueue<>();

    public long placeOrder(String planet, Variant variant, Size size) {
        final long id = orderSequence.incrementAndGet();
        final Order order = new Order(id, planet, variant, size);
        assignOrderToPlanet(order);

        return id;
    }

    public List<Order> takeOrdersFromBestPlanet() {
        final Optional<PlanetOrders> planetOpt = takeBestPlanet();
        if (planetOpt.isPresent()) {
            final PlanetOrders planet = planetOpt.get();
            List<Order> orders = planet.takeOrders();
            return orders;
        }
        return Collections.EMPTY_LIST;
    }

    private Optional<PlanetOrders> takeBestPlanet() {
        synchronized (this.bestPlanets) {
            final PlanetOrders planet = this.bestPlanets.poll();
            return Optional.ofNullable(planet);
        }
    }

    @Override
    public long countStandingOrders() {
        return this.orders.values().stream().map(p -> p.takeOrders().size()).reduce(0, (x, y) -> x + y);
    }

    private void assignOrderToPlanet(Order order) {
        final PlanetOrders po = orders.computeIfAbsent(order.planet,
                planetName -> new PlanetOrders(planetName));
        po.assignOrder(order);
        synchronized (this.bestPlanets) {
            this.bestPlanets.remove(po);
            this.bestPlanets.offer(po);
        }
    }

}
