package pl.setblack.exp.galakpizza.system;

import pl.setblack.airomem.core.Storable;
import pl.setblack.exp.galakpizza.api.GalakPizzaService;
import pl.setblack.exp.galakpizza.domain.Order;
import pl.setblack.exp.galakpizza.domain.Size;
import pl.setblack.exp.galakpizza.domain.Variant;

import java.io.Serializable;
import java.util.*;


public class GalakPizzaCore implements GalakPizzaService, Serializable, Storable<GalakPizzaCore> {
    private final Map<String, PlanetOrders> orders = new HashMap<>();

    private long orderSequence = 1;

    private PriorityQueue<PlanetOrders> bestPlanets = new PriorityQueue<>(256);

    public long placeOrder(String planet, Variant variant, Size size) {
        final long id = orderSequence++;
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

        PlanetOrders planet = this.bestPlanets.poll();
        while (planet != null && planet.isEmptied()) {
            planet = this.bestPlanets.poll();
        }
        return Optional.ofNullable(planet);

    }

    @Override
    public long countStandingOrders() {
        return this.orders.values().stream().map(p -> p.takeOrders().size()).reduce(0, (x, y) -> x + y);
    }

    private void assignOrderToPlanet(Order order) {
        final PlanetOrders po = orders.computeIfAbsent(order.planet,
                planetName -> new PlanetOrders(planetName));
        po.assignOrder(order);

        //this.bestPlanets.remove(po);
        this.bestPlanets.offer(po);

    }

    @Override
    public GalakPizzaCore getImmutable() {
        return this;
    }
}
