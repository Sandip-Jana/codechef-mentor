package com.hackathon.codechefapp.activities.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.adapter.ChatAdapter;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.dao.chat.Message;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;
import com.hackathon.codechefapp.utils.DisplayToast;
import com.hosopy.actioncable.ActionCable;
import com.hosopy.actioncable.ActionCableException;
import com.hosopy.actioncable.Channel;
import com.hosopy.actioncable.Consumer;
import com.hosopy.actioncable.Subscription;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    private ArrayList<Message> messageList = new ArrayList<>();

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;

    private EditText editTextChat;
    private Button sendButton;

    private Consumer consumer;
    private Subscription subscription;

    private SharedPreferenceUtils prefs;

    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editTextChat = findViewById(R.id.editTextChatbox);
        sendButton = findViewById(R.id.sendBtn);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatAdapter = new ChatAdapter(this, messageList);
        chatRecyclerView.setAdapter(chatAdapter);

        prefs = SharedPreferenceUtils.getInstance(getApplicationContext());

        URI uri = null;
        try {
            uri = new URI("http://149.129.138.172/cable");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Consumer.Options options = new Consumer.Options();
        Map<String, String> headers = new HashMap();
        HashSet<String> preferences = SharedPreferenceUtils.getInstance(this).getCookies();
        for (String cookie : preferences) {
            headers.put("Cookie", cookie);
        }
        options.headers = headers;

        Consumer consumer = ActionCable.createConsumer(uri, options);

        Channel appearanceChannel = new Channel("ChatChannel");
        subscription = consumer.getSubscriptions().create(appearanceChannel);

        consumer.connect();

        addListeners();
    }

    private void addListeners() {

        subscription.onConnected(new Subscription.ConnectedCallback() {
            @Override
            public void call() {
                Log.d(TAG, "Subscription connected");
                isConnected = true;
            }
        }).onRejected(new Subscription.RejectedCallback() {
            @Override
            public void call() {

            }
        }).onReceived(new Subscription.ReceivedCallback() {
            @Override
            public void call(JsonElement data) {
                Log.d(TAG, "Subscription Message Received");
                receiveMessage(data);
            }
        }).onDisconnected(new Subscription.DisconnectedCallback() {
            @Override
            public void call() {
                Log.d(TAG, "Subscription Disconnected");
            }
        }).onFailed(new Subscription.FailedCallback() {
            @Override
            public void call(ActionCableException e) {
                Log.d(TAG, "Subscription error message");
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected && editTextChat.getText().toString().trim().length() > 0) {
                    //send message
                    final JsonObject params = new JsonObject();
                    params.addProperty("message", editTextChat.getText().toString());
                    subscription.perform("send_message", params);

                } else if (!isConnected) {
                    onBackPressed();
                    DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), "Connection problem");
                } else {
                    DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), "Message cant be empty");
                }
            }
        });

        chatRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, final int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    chatRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                        }
                    }, 100);
                }
            }
        });

    }

    private void receiveMessage(JsonElement params) {

        JsonObject jsonObject = params.getAsJsonObject();
        final Message message = new Message();
        String senderName = jsonObject.get("sender_name").getAsString();
        if (senderName.toLowerCase().trim().equalsIgnoreCase(prefs.getStringValue(PreferenceConstants.LOGGED_IN_USER_NAME, "").toLowerCase().trim())) {
            message.setCurrentUser(true);
        } else {
            message.setCurrentUser(false);
        }

        message.setSenderName(senderName);
        message.setMessage(jsonObject.get("message").getAsString());
        message.setTime(jsonObject.get("time").getAsString());

        messageList.add(message);
        chatAdapter = new ChatAdapter(getApplicationContext(), messageList);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //chatAdapter.notifyItemInserted( chatAdapter.getItemCount()-1 );
                chatRecyclerView.swapAdapter(chatAdapter, false);
                chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                if (message.isCurrentUser()) {
                    editTextChat.setText("");
                    //hideKeyboard
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {

        isConnected = false;
        if (consumer != null)
            consumer.getSubscriptions().remove(subscription);

        if (consumer != null)
            consumer.disconnect();

        super.onDestroy();
    }
}


