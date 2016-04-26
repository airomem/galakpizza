package pl.setblack.exp.galakpizza.server;

import pl.setblack.badass.Politician;
import pl.setblack.exp.galakpizza.domain.Order;
import pl.setblack.exp.galakpizza.system.GalakPizza;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;

import java.net.URI;

import static ratpack.jackson.Jackson.jsonNode;

public class MyRatpackServer {

    final GalakPizza gp;

    public MyRatpackServer() {
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
                    .handlers(chain -> chain
                            .prefix("api", apiChain -> apiChain
                            .prefix("order", orderChain -> orderChain
                            .post(  ctx -> {
                                ctx.parse(IncomingOrder.class)
                                        .then(order -> {
                                            final long orderId = gp.placeOrder( order.planet, order.variant, order.size);
                                            ctx.render(String.valueOf(orderId));
                                        });
                            })))
                            .get(ctx -> ctx.render("Hello " + ctx.get(String.class)))
                            .get(":name", ctx -> ctx.render("Hello " + ctx.getPathTokens().get("name") + "!"))
                    )
            );
        });
    }

    public static void main(final String... args) throws Exception {
        final MyRatpackServer server = new MyRatpackServer();
        server.init();
    }
}
