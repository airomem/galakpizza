package pl.setblack.exp.galakpizza.system;

import org.junit.Test;
import pl.setblack.exp.galakpizza.api.GalakPizzaService;
import pl.setblack.exp.galakpizza.domain.Order;
import pl.setblack.exp.galakpizza.domain.Size;
import pl.setblack.exp.galakpizza.domain.Variant;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public abstract class GalakPizzaTestBase<G extends GalakPizzaService> {
    protected G gp;


    @Test
    public void shouldPlaceOrdetToPlanet() {


        long orderId = gp.placeOrder("planetx", Variant.HAWAII, Size.LARGE);

        assertThat(orderId, is(greaterThan(0L)));
    }

    @Test
    public void ordersShouldHaveVariousId() {


        long orderId1 = gp.placeOrder("planetx", Variant.HAWAII, Size.LARGE);
        long orderId2 = gp.placeOrder("planety", Variant.HAWAII, Size.MEDIUM);

        assertThat(orderId1, is(not(equalTo(orderId2))));
    }

    @Test
    public void singleOrderForGalaxyShouldBeProcessed() {


        long orderId1 = gp.placeOrder("planetx", Variant.HAWAII, Size.LARGE);

        final List<Order> orders = gp.takeOrdersFromBestPlanet();

        assertThat(orders.get(0).id, is(equalTo(orderId1)));
    }

    @Test
    public void planetWithSingleOrderForGalaxyShouldBeBestWhenNoOtherExists() {


         gp.placeOrder("planetx", Variant.HAWAII, Size.LARGE);

        final List<Order> orders = gp.takeOrdersFromBestPlanet();

        assertThat(orders.get(0).planet, is(equalTo("planetx")));
    }

    @Test
    public void planetWithTwoOrdersShouldBeBetterThanWithOne() {


        gp.placeOrder("planetx", Variant.HAWAII, Size.LARGE);
        gp.placeOrder("planety", Variant.VEGETARIAN, Size.LARGE);
        gp.placeOrder("planetx", Variant.MARGHERITA, Size.MEDIUM);


        final List<Order> orders = gp.takeOrdersFromBestPlanet();

        assertThat(orders.get(0).planet, is(equalTo("planetx")));
    }

    @Test
    public void planetWithThreeOrdersShouldBeBetterThanWithTwo() {


        gp.placeOrder("planetx", Variant.HAWAII, Size.LARGE);
        gp.placeOrder("planety", Variant.VEGETARIAN, Size.LARGE);
        gp.placeOrder("planetx", Variant.MARGHERITA, Size.MEDIUM);
        gp.placeOrder("planety", Variant.VEGETARIAN, Size.XL);
        gp.placeOrder("planety", Variant.HAWAII, Size.LARGE);


        final List<Order> orders = gp.takeOrdersFromBestPlanet();

        assertThat(orders.get(0).planet, is(equalTo("planety")));
    }

    @Test
    public void allPlanetsShouldBeEventuallyProcessed() {


        gp.placeOrder("planetx", Variant.HAWAII, Size.LARGE);
        gp.placeOrder("planety", Variant.VEGETARIAN, Size.LARGE);
        gp.placeOrder("planetz", Variant.MARGHERITA, Size.MEDIUM);
        gp.placeOrder("planety", Variant.VEGETARIAN, Size.XL);
        gp.placeOrder("planety", Variant.HAWAII, Size.LARGE);

        gp.takeOrdersFromBestPlanet();
        gp.takeOrdersFromBestPlanet();
        gp.takeOrdersFromBestPlanet();
        final List<Order> orders = gp.takeOrdersFromBestPlanet();

        assertThat(orders, is(empty()));
    }
}