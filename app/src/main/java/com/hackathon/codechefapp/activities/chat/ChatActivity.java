package com.hackathon.codechefapp.activities.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hackathon.codechefapp.R;
import com.hosopy.actioncable.ActionCable;
import com.hosopy.actioncable.ActionCableException;
import com.hosopy.actioncable.Channel;
import com.hosopy.actioncable.Consumer;
import com.hosopy.actioncable.Subscription;

import java.net.URI;
import java.net.URISyntaxException;

import br.net.bmobile.websocketrails.WebSocketRailsChannel;
import br.net.bmobile.websocketrails.WebSocketRailsDispatcher;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    Consumer consumer;

    Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //startChat();

        try {
            startChatLibraryTest();
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage() + "exception occured");
        }
    }

    private void startChatLibraryTest() throws Exception {


        //step 1
        URI uri = new URI("http://149.129.138.172/cable");
        consumer = ActionCable.createConsumer(uri);


        //step 2
        Channel appearanceChannel = new Channel("ChatChannel");
        subscription = consumer.getSubscriptions().create(appearanceChannel);

        //step 3
        subscription
                .onConnected(new Subscription.ConnectedCallback() {
                    @Override
                    public void call() {
                        Log.d(TAG , "yes i am connected");
                        sendMessage();

                    }
                }).onRejected(new Subscription.RejectedCallback() {
            @Override
            public void call() {
                Log.d(TAG , "yes in call method");
            }
        }).onReceived(new Subscription.ReceivedCallback() {
            @Override
            public void call(JsonElement data) {
                // Called when the subscription receives data from the server
                Log.d(TAG , "subscription receives data from the server "  + data  );
            }
        }).onDisconnected(new Subscription.DisconnectedCallback() {
            @Override
            public void call() {
                // Called when the subscription has been closed
                Log.d(TAG , "subscription receives data from the server");
            }
        }).onFailed(new Subscription.FailedCallback() {
            @Override
            public void call(ActionCableException e) {
                // Called when the subscription encounters any error
                Log.d(TAG , "subscription receives data from the server");
            }
        });


        // 4. Establish connection
        consumer.connect();

        Log.d(TAG , "running");

    }


    private void sendMessage() {

        Log.d(TAG , "Sending Message");
        JsonObject params = new JsonObject();
        params.addProperty("message", "Jaadu ho Gya Yeeeee");
        //5. ok
        subscription.perform("send_message" , params);
        Log.d(TAG , " Message sent");
    }

    private void startChat() {
//        try {
//            dispatcher = new WebSocketRailsDispatcher(new URL("http://149.129.138.172/cable"));
//            dispatcher.connect();
//
//            while (!dispatcher.getState().toLowerCase().trim().equalsIgnoreCase("connected")) {
//                String id = dispatcher.getConnectionId();
//                Log.d("Chini Test", dispatcher.getState());
//            }
//
//            webSocketRailsChannel = dispatcher.subscribe("ChatChannel");
//
//            if (dispatcher.isSubscribed("ChatChannel")) {
//                Message message = new Message();
//
//                message.setMessage("Hiiiii Chinmoy");
//
//                webSocketRailsChannel.trigger("send_message", message);
//
//                webSocketRailsChannel.bind("send_message", new WebSocketRailsDataCallback() {
//
//                    @Override
//                    public void onDataAvailable(Object data) {
//                        Log.d("Chini Test", "callback data received");
//                    }
//                });
//
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onDestroy() {

        Log.d("Chini Test", "ok");
//        dispatcher.unsubscribe("ChatChannel");
//
//        dispatcher.disconnect();

        consumer.getSubscriptions().remove(subscription);

        consumer.disconnect();

        super.onDestroy();

    }

//    private class Message {
//
//        String name;
//        String message;
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//
//    }

}
