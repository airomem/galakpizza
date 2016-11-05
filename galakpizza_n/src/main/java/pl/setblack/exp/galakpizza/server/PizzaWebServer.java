package pl.setblack.exp.galakpizza.server;

import pl.setblack.badass.Politician;
import pl.setblack.exp.galakpizza.api.PlanetSummary;
import pl.setblack.exp.galakpizza.domain.Order;
import pl.setblack.exp.galakpizza.system.GalakPizza;
import ratpack.error.ServerErrorHandler;
import ratpack.func.Action;
import ratpack.handling.Chain;
import ratpack.jackson.Jackson;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class PizzaWebServer {

    final GalakPizza gp;

    public PizzaWebServer() {
        this.gp = new GalakPizza();
    }

    private void init() {
        Politician.beatAroundTheBush(() -> {
            RatpackServer.start(server -> server
                    .serverConfig(
                            ServerConfig
                                    .embedded()
                                    .publicAddress(new URI("http://localhost"))
                                    .port(8085))
                    .registryOf(registry -> registry.add("World!"))
                    .handlers(chain -> {
                                chain
                                        .prefix("services", apiChain -> apiChain
                                                .prefix("order", placeOrderAction())
                                                .prefix("takeOrders", takeOrdersAction())
                                                .prefix("countOrders", countOrdersAction())
                                                .prefix("debugOrders", showOrdersAction())
                                        )
                                        .register(registry ->
                                                registry.add(ServerErrorHandler.class, (context, throwable) ->
                                                        context.render("caught by error handler: " + throwable.getMessage())
                                                ));

                            }

                    )
            );
        });
    }

    private Action<Chain> takeOrdersAction() {
        return orderChain -> orderChain
                .post(ctx -> {
                    final List<Order> bestOrders = gp.takeOrdersFromBestPlanet();
                    final List<OutgoingOrder> out = bestOrders
                            .stream()
                            .map(o -> OutgoingOrder.fromOrder(o))
                            .collect(Collectors.toList());
                    ctx.render(Jackson.json(out));
                });
    }

    private Action<Chain> placeOrderAction() {
        return orderChain -> orderChain
                .post(ctx -> {
                    System.out.println("got post");
                    ctx.parse(IncomingOrder.class)
                            .onError(error -> System.out.println(error))
                            .then(order -> {
                                System.out.println("got order");
                                final long orderId = gp.placeOrder(order.planet, order.variant, order.size);
                                ctx.render(String.valueOf(orderId));
                            });


                });
    }

    private Action<Chain> countOrdersAction() {
        return orderChain -> orderChain
                .get(ctx -> {
                    final long orderCnt = gp.countStandingOrders();
                    ctx.render(String.valueOf(orderCnt));
                });
    }

    private Action<Chain> showOrdersAction() {
        return orderChain -> orderChain
                .get(ctx -> {
                    final Collection<PlanetSummary> orders = gp.getPlanetsSummary();
                    ctx.render(Jackson.json(orders));
                });
    }

    public static void main(final String... args) throws Exception {
        final PizzaWebServer server = new PizzaWebServer();
        server.init();
    }
}
