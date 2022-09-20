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
import java.util.function.Function;
import javax.persistence.PersistenceException;

public class GalakPizza implements GalakPizzaService {

    final SessionFactory sessionFactory;

    public GalakPizza(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public long placeOrder(String planet, Variant variant, Size size) {
        return this.runInSession(session -> {
            final Order orderEntity = new Order(planet, variant, size);
            final Long key = (Long) session.save(orderEntity);
            incrementPlanetCounter(planet, session);
            return key;
        });
    }

    private void incrementPlanetCounter(String planet, Session session) {
        Planet planetEntity = session.get(Planet.class, planet);
        if (planetEntity == null) {
            planetEntity = new Planet(planet);
            session.save(planetEntity);
        }
        planetEntity.increment();
    }

    public List<Order> takeOrdersFromBestPlanet() {
        return this.runInSession(session -> {
            final Query bestPlanetQuery = session.getNamedQuery("select best planet from table");
            bestPlanetQuery.setMaxResults(1);
            final Iterator<Planet> bestPlanetsIterator = bestPlanetQuery.iterate();
            if (bestPlanetsIterator.hasNext()) {

                final Planet bestOne = bestPlanetsIterator.next();
                final Query ordersQuery = session.getNamedQuery("select orders");
                ordersQuery.setParameter("planet", bestOne.name);
                final List<Order> orders = ordersQuery.list();

                orders.forEach(o -> session.delete(o));
                bestOne.clear();
                return orders;
            }
            return Collections.EMPTY_LIST;
        });
    }


    @Override
    public long countStandingOrders() {
        return this.runInSession(session -> {
            final Query ordersCount = session.getNamedQuery("count orders");
            final Long cnt = (Long) ordersCount.iterate().next();
            return cnt;
        });
    }


    private <T> T runInSession(Function<Session, T> dbCommand) {
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
            return runInSession(dbCommand);
        } catch (PersistenceException pe) {
            if (pe.getCause() instanceof ConstraintViolationException) {
                session.getTransaction().rollback();
                session.close();
                return runInSession(dbCommand);
            } else {
                pe.printStackTrace();
                throw new RuntimeException(pe);
            }
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException(t);
        }
    }

}
