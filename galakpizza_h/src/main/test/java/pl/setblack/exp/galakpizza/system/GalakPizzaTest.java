package pl.setblack.exp.galakpizza.system;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.setblack.exp.HibernateStart;
import pl.setblack.exp.galakpizza.domain.Order;
import pl.setblack.exp.galakpizza.domain.Size;
import pl.setblack.exp.galakpizza.domain.Variant;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GalakPizzaTest {

    HibernateStart hibernate;
    SessionFactory sf;

    @Before
    public void initHibernate() {
        hibernate = new HibernateStart();
        sf = hibernate.init();
    }

    @After
    public void closeHibernate() {
        hibernate.close();
    }

    @Test
    public void shouldPlaceOrdetToPlanet() {
        final GalakPizza gp = new GalakPizza(sf);

        long orderId = gp.placeOrder("planetx", Variant.HAWAII, Size.LARGE);

        assertThat(orderId, is(greaterThan(0L)));
    }

    @Test
    public void ordersShouldHaveVariousId() {
        final GalakPizza gp = new GalakPizza(sf);

        long orderId1 = gp.placeOrder("planetx", Variant.HAWAII, Size.LARGE);
        long orderId2 = gp.placeOrder("planety", Variant.HAWAII, Size.MEDIUM);

        assertThat(orderId1, is(not(equalTo(orderId2))));
    }

    @Test
    public void singleOrderForGalaxyShouldBeProcessed() {
        final GalakPizza gp = new GalakPizza(sf);

        long orderId1 = gp.placeOrder("planetx", Variant.HAWAII, Size.LARGE);

        final List<Order> orders = gp.takeOrdersFromBestPlanet();

        assertThat(orders.get(0).getId(), is(equalTo(orderId1)));
    }

    @Test
    public void planetWithSingleOrderForGalaxyShouldBeBestWhenNoOtherExists() {
        final GalakPizza gp = new GalakPizza(sf);

        gp.placeOrder("planetx", Variant.HAWAII, Size.LARGE);

        final List<Order> orders = gp.takeOrdersFromBestPlanet();

        assertThat(orders.get(0).planet, is(equalTo("planetx")));
    }

    @Test
    public void planetWithTwoOrdersShouldBeBetterThanWithOne() {
        final GalakPizza gp = new GalakPizza(sf);

        gp.placeOrder("planetx", Variant.HAWAII, Size.LARGE);
        gp.placeOrder("planety", Variant.VEGETARIAN, Size.LARGE);
        gp.placeOrder("planetx", Variant.MARGHERITA, Size.MEDIUM);


        final List<Order> orders = gp.takeOrdersFromBestPlanet();

        assertThat(orders.get(0).planet, is(equalTo("planetx")));
    }

    @Test
    public void planetWithThreeOrdersShouldBeBetterThanWithTwo() {
        final GalakPizza gp = new GalakPizza(sf);

        gp.placeOrder("planetx", Variant.HAWAII, Size.LARGE);
        gp.placeOrder("planety", Variant.VEGETARIAN, Size.LARGE);
        gp.placeOrder("planetx", Variant.MARGHERITA, Size.MEDIUM);
        gp.placeOrder("planety", Variant.VEGETARIAN, Size.EXTRA_LARGE);
        gp.placeOrder("planety", Variant.HAWAII, Size.LARGE);


        final List<Order> orders = gp.takeOrdersFromBestPlanet();

        assertThat(orders.get(0).planet, is(equalTo("planety")));
    }

    @Test
    public void allPlanetsShouldBeEventuallyProcessed() {
        final GalakPizza gp = new GalakPizza(sf);

        gp.placeOrder("planetx", Variant.HAWAII, Size.LARGE);
        gp.placeOrder("planety", Variant.VEGETARIAN, Size.LARGE);
        gp.placeOrder("planetz", Variant.MARGHERITA, Size.MEDIUM);
        gp.placeOrder("planety", Variant.VEGETARIAN, Size.EXTRA_LARGE);
        gp.placeOrder("planety", Variant.HAWAII, Size.LARGE);

        gp.takeOrdersFromBestPlanet();
        gp.takeOrdersFromBestPlanet();
        gp.takeOrdersFromBestPlanet();
        final List<Order> orders = gp.takeOrdersFromBestPlanet();

        assertThat(orders, is(empty()));
    }


}