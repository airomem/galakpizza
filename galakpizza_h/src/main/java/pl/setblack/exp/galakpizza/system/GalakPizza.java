package pl.setblack.exp.galakpizza.system;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import pl.setblack.exp.galakpizza.api.GalakPizzaService;
import pl.setblack.exp.galakpizza.domain.Order;
import pl.setblack.exp.galakpizza.domain.Planet;
import pl.setblack.exp.galakpizza.domain.Size;
import pl.setblack.exp.galakpizza.domain.Variant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Supplier;

public class GalakPizza implements GalakPizzaService {

    final SessionFactory sessionFactory;

    public GalakPizza(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public long placeOrder(String planet, Variant variant, Size size) {
        return this.runOnSession( session  -> {
            final Order orderEntity = new Order(planet, variant, size);
            final Long key = (Long)session.save(orderEntity);

            incrementPlanetCounter(planet, session);
            return key;
        });
    }

    private void incrementPlanetCounter(String planet, Session session) {
        Planet planetEntity = session.get(Planet.class, planet);
        if ( planetEntity == null) {
            planetEntity =  new Planet(planet);
            try {
                session.save(planetEntity);
            }
            catch (ConstraintViolationException cve) {
                incrementPlanetCounter(planet, session);
                System.out.println("success!");
            }
        }
        planetEntity.increment();
    }

    public List<Order> takeOrdersFromBestPlanet() {
        return this.runOnSession( session  -> {
            final Query bestPlanetQuery = session.createQuery("SELECT p  FROM Planet p " +
                    "ORDER BY p.count desc" );
            bestPlanetQuery.setMaxResults(1);
            final Iterator<Planet> bestPlanetsIterator = bestPlanetQuery.iterate();
            if (bestPlanetsIterator.hasNext()) {
                final Planet bestOne = bestPlanetsIterator.next();
                final Query ordersQuery = session.createQuery("SELECT o FROM Order o WHERE o.planet = :planet");
                ordersQuery.setParameter("planet", bestOne.name);
                final List<Order> orders = ordersQuery.list();
                bestOne.clear();
                orders.forEach(o -> session.delete(o));
                return orders;
            }
            return Collections.EMPTY_LIST;
        });
 }



    @Override
    public long countStandingOrders() {
        return this.runOnSession( session -> {
            final Query ordersCount = session.createQuery("SELECT count(o)  FROM Order o ");
            final Long cnt = (Long)ordersCount.iterate().next();
            return cnt;
        });
    }



    private <T> T runOnSession(Function<Session, T> dbCommand) {
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            final T result = dbCommand.apply(session);
            session.getTransaction().commit();
            session.close();
            return result;
        } catch (ConstraintViolationException cve) {
            session.getTransaction().rollback();
            session.close();
            return runOnSession(dbCommand);
        }

    }

}
