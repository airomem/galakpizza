package pl.setblack.exp.galakpizza.server;

import pl.setblack.badass.Politician;
import pl.setblack.exp.galakpizza.domain.Order;
import pl.setblack.exp.galakpizza.system.GalakPizza;
import ratpack.error.ServerErrorHandler;
import ratpack.exec.Promise;
import ratpack.func.Action;
import ratpack.handling.Chain;
import ratpack.jackson.Jackson;
import ratpack.jackson.JsonRender;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;


public class PizzaWebServer {

    private final PizzaNonBlobkingService gp;

    public PizzaWebServer() {
        this.gp = new PizzaNonBlobkingService(new GalakPizza());
    }

    private void init() {
        Politician.beatAroundTheBush(() -> {
            RatpackServer.start(server -> server
                    .serverConfig(
                            ServerConfig
                                    .embedded()
                                    .publicAddress(new URI("http://localhost"))
                                    .port(8085))
                    .handlers(chain -> {
                                chain
                                        .prefix("services", apiChain -> apiChain
                                                .prefix("order", placeOrderAction())
                                                .prefix("takeOrders", takeOrdersAction())
                                                .prefix("countOrders", countOrdersAction())
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
                    final CompletionStage<List<Order>> bestOrders = gp.takeOrdersFromBestPlanet();
                    final CompletionStage<JsonRender> result = bestOrders.thenApply(list->list.stream()
                            .map(o -> OutgoingOrder.fromOrder(o))
                            .collect(Collectors.toList()))
                            .thenApply(Jackson::json);
                    final Promise promise = Promise.async(downstream -> {
                        downstream.accept(result);
                    });

                    ctx.render(promise);

                });
    }

    private Action<Chain> placeOrderAction() {
        return orderChain -> orderChain
                .post(ctx -> {

                    ctx.parse(IncomingOrder.class)
                            .onError(error -> System.out.println(error))
                            .then(order -> {

                                gp.placeOrder(order.planet, order.variant, order.size);
                                ctx.render("-1");
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

    public static void main(final String... args) throws Exception {
        final PizzaWebServer server = new PizzaWebServer();
        server.init();
    }
}
