package com.vertx.vue;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import java.util.List;
import java.util.stream.Collectors;

public class Server extends AbstractVerticle {

    private static final String API_ADDRESS = "api.data";
    private static final String MESSAGE_ADDRESS_REGEX = "^message.data.*";
    private static final String MESSAGE_ADDRESS = "message.data.";
    private static final String USERS_ADDRESS = "users.data";

    private static JsonArray users = new JsonArray();
    private static JsonObject conversations = new JsonObject();

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        //Create a router to handle all the requests coming into the server
        Router router = Router.router(vertx);
        setupRoutes(router);

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

        BridgeOptions bridgeOptions = setupEBAddress();

        router.route("/sockjs/*").handler(SockJSHandler.create(vertx).bridge(bridgeOptions));

        //Load the web browser UI via here
        router.get("/*").handler(StaticHandler.create("webroot/"));
    }

    private BridgeOptions setupEBAddress() {
        //Setting the bridge options prevents internal address from leaking information out
        BridgeOptions bridgeOptions = new BridgeOptions()
                //Outgoing traffic eventbus address - This is the address the eventbus address the browser would be able to listen on
        //Incoming traffic eventbus address - This is the address the eventbus address the browser would be able to communicate with us on
                .addInboundPermitted(new PermittedOptions().setAddress(API_ADDRESS))
                .addOutboundPermitted(new PermittedOptions().setAddress(API_ADDRESS))
                .addOutboundPermitted(new PermittedOptions().setAddressRegex(MESSAGE_ADDRESS_REGEX))
                .addOutboundPermitted(new PermittedOptions().setAddress(USERS_ADDRESS));


        vertx.eventBus().consumer(API_ADDRESS, this::apiHandler);

        return bridgeOptions;
    }

    private void apiHandler(Message<JsonObject> message) {
        System.out.println("API Handler");
        System.out.println(String.format("Message -> %s", message.body().encode()));
        String action = message.body().getString("action");
        JsonObject data = message.body().getJsonObject("data");
        switch (action) {
            case "get-contacts":
                handleGetContacts(message, action, data);
                break;
            case "get-conversation":
                handleGetConversation(message, action, data);
                break;
            case "send-message":
                handleSendMessage(message, action, data);
                break;
            case "login":
                handleLogin(message, action, data);
                break;
            case "logout":
                handleLogout(message, action, data);
        }
    }

    private void handleLogout(Message<JsonObject> message, String action, JsonObject user) {
        System.out.println("Loggedout data ->" + user.encode());

        //Remove users that match the user we're trying to remove
        List users_list = users
                .stream()
                .filter(o -> !((JsonObject) o).getString("name").equals(user.getString("name")))
                .collect(Collectors.toList());

        users = new JsonArray(users_list);

        message.reply("loggedout");
        vertx.eventBus().publish(USERS_ADDRESS, new JsonObject().put("action", "loggedout").put("data", user));
    }

    private void handleLogin(Message<JsonObject> message, String action, JsonObject user) {
        System.out.println("Loggein data ->" + user.encode());
        users.add(user);
        message.reply("LoggedIn");
        vertx.eventBus().publish(USERS_ADDRESS, new JsonObject().put("action", "loggedin").put("data", user));
    }

    private void handleSendMessage(Message<JsonObject> message, String action, JsonObject data) {
        JsonObject chat = data.getJsonObject("message");
        JsonObject owner = data.getJsonObject("owner");
        JsonObject resp = data.getJsonObject("resp");

        //Unique identifier for the conversation
        String conversation_id = (owner.getString("name") +"-"+resp.getString("name")).toLowerCase();
        String conversation_id_rev =  (resp.getString("name")+"-"+owner.getString("name")).toLowerCase();

        //Persist the conversation for user later
        storeChat(conversation_id, conversation_id_rev, chat);

        vertx.eventBus().publish(MESSAGE_ADDRESS+conversation_id, data);
        vertx.eventBus().publish(MESSAGE_ADDRESS+conversation_id_rev, data);

        message.reply(new JsonObject());//Reply success
    }

    private void storeChat(String conversation_id, String conversation_id_rev, JsonObject chat) {
        //Persist the conversation
        getChat(conversation_id, conversation_id_rev).add(chat); //Storing conversation in memory
        //todo: Persist the conversation into a database
    }


    private String createConversationId(String owner_name, String resp_name) {
        return (owner_name + "-"+resp_name).toLowerCase();
    }

    private JsonArray getChat(String conversation_id, String conversation_id_rev) {
        if (conversations.containsKey(conversation_id) || conversations.containsKey(conversation_id_rev)) {
            System.out.println("Conversation found");
            System.out.println(String.format("Conversations -> %s", conversations.encode()));
            JsonArray conversation = conversations.getJsonArray(conversation_id);
            if (conversation == null) {
                conversation = conversations.getJsonArray(conversation_id_rev);
            }
            System.out.println(String.format("Conversation -> %s", conversation.encode()));
            return conversation;
        } else {
            //Create a new conversation
            System.out.println("Conversation not found creating a new one");
            JsonArray chats = new JsonArray();
            conversations.put(conversation_id, chats);
            return chats;
        }
    }

    private void handleGetContacts(Message<JsonObject> message, String action, JsonObject user) {
        //Doing this so we don't send back the currently logged in user
        List users_list = users
                .stream()
                .filter(o -> !((JsonObject) o).getString("name").equals(user.getString("name")))
                .collect(Collectors.toList());

        message.reply(new JsonArray(users_list));
    }

    private void handleGetConversation(Message<JsonObject> message, String action, JsonObject data) {
        JsonObject owner = data.getJsonObject("owner");
        JsonObject resp = data.getJsonObject("resp");

        //Unique identifier for the conversation
        String conversation_id = createConversationId(owner.getString("name"), resp.getString("name"));
        String conversation_id_rev =  createConversationId(resp.getString("name"), owner.getString("name"));

        System.out.println(String.format("Conversation ID - %s | %s", conversation_id, conversation_id_rev));

        message.reply(getChat(conversation_id, conversation_id_rev));
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        super.stop(stopFuture);
    }
}
