package com.vertx.vue;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class Server extends AbstractVerticle {

    public static String INCOMING_ADDRESS  = "incoming.data";
    public static String OUTGOING_ADDRESS = "outgoing.data";
    public static String PING_ADDRESS = "ping.data";

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        //Create a router to handle all the requests coming into the server
        Router router = Router.router(vertx);
        setupRoutes(router);

        //Pings
        pings();

        //Create a http server to listen specified port
        getVertx()
                .createHttpServer()

                //Assign the router to intercept all the requests
                .requestHandler(router::accept)

                //Server listening on specified
                .listen(8081, serverResult -> {
                    //Vertx is event driven, wait for an event that
                    //Server has been created
                    if (serverResult.succeeded()) {
                        System.out.println("Server started successfully");
                        startFuture.complete();
                    } else {
                        System.out.println("Server failed to start");
                        serverResult.cause().printStackTrace();
                        startFuture.fail(serverResult.cause());
                    }
                });
    }

    private void setupRoutes(Router router) {

        //Setting the bridge options prevents internal address from leaking information out
        BridgeOptions bridgeOptions = new BridgeOptions()
                //Outgoing traffic eventbus address - This is the address the eventbus address the browser would be able to listen on
                .addOutboundPermitted(new PermittedOptions().setAddress(OUTGOING_ADDRESS))
                .addOutboundPermitted(new PermittedOptions().setAddress(INCOMING_ADDRESS))
                .addOutboundPermitted(new PermittedOptions().setAddress(PING_ADDRESS));
                //Incoming traffic eventbus address - This is the address the eventbus address the browser would be able to communicate with us on
//                .addInboundPermitted(new PermittedOptions().setAddress(INCOMING_ADDRESS))
//                .addInboundPermitted(new PermittedOptions().setAddress(PING_ADDRESS));

        router.route("/sockjs/*").handler(SockJSHandler.create(vertx).bridge(bridgeOptions));

        //Load the web browser UI via here
        router.get("/*").handler(StaticHandler.create("webroot/"));
    }

    private void pings() {
        //Send pings out every 5seconds
        vertx.setPeriodic(5000, event -> {
            vertx.eventBus().send(PING_ADDRESS, "Hi", event1 -> {
                System.out.println(String.format("Response received: %s", event1.result().body()));
            });
        });

        //Listen for messages on our pong-address
        vertx.eventBus().consumer(PING_ADDRESS, event -> {
            System.out.println(String.format("Event -> address: %s | body: %s", event.address(), event.body()));
            event.reply("Hello");
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        super.stop(stopFuture);
    }
}
