package com.hackathon.codechefapp.activities.chat;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.adapter.ChatAdapter;
import com.hackathon.codechefapp.client.RetrofitClient;
import com.hackathon.codechefapp.constants.Constants;
import com.hackathon.codechefapp.constants.PreferenceConstants;
import com.hackathon.codechefapp.dao.chat.Message;
import com.hackathon.codechefapp.model.chat.RetrieveMessagesResponse;
import com.hackathon.codechefapp.preferences.SharedPreferenceUtils;
import com.hackathon.codechefapp.retrofitmapping.IChef;
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
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    private ArrayList<Message> messageList = new ArrayList<>();

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;

    private EditText editTextChat;
    private Button sendButton;
    private ProgressBar chatProgressBar;

    private Consumer consumer;
    private Subscription subscription;

    private SharedPreferenceUtils prefs;

    private boolean isConnected = false;

    private String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initToolbar();

        editTextChat = findViewById(R.id.editTextChatbox);
        sendButton = findViewById(R.id.sendBtn);
        chatProgressBar = findViewById(R.id.chatProgressBar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Constants.ROOM_ID)) {
            roomId = (String) bundle.get(Constants.ROOM_ID);

            if (bundle.containsKey(Constants.CHAT_HEADER)) {
                String tab_header = (String) bundle.get(Constants.CHAT_HEADER);
                getSupportActionBar().setTitle(tab_header);
            } else if (roomId != null && !roomId.isEmpty()) {
                int chatRoomId = Integer.parseInt(roomId);
                switch (chatRoomId) {
                    case -1:
                        getSupportActionBar().setTitle(R.string.general);
                        break;
                    case -2:
                        getSupportActionBar().setTitle(R.string.ioi);
                        break;
                    case -3:
                        getSupportActionBar().setTitle(R.string.acm);
                        break;
                    case -4:
                        getSupportActionBar().setTitle(R.string.placements_discuss);
                        break;
                    default:
                        break;
                }
            }

        } else {
            Log.d(TAG, "RoomId not fetched");
            return;
        }

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setHasFixedSize(false);

        chatAdapter = new ChatAdapter(this, messageList);
        chatRecyclerView.setAdapter(chatAdapter);

        prefs = SharedPreferenceUtils.getInstance(getApplicationContext());

        fetchPreviousMessages(roomId);

    }

    private void initToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startChatting() {

        URI uri = null;
        try {
            uri = new URI(getString(R.string.chat_url) + prefs.getStringValue(PreferenceConstants.AUTH_CODE, ""));
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
        appearanceChannel.addParam("room", Integer.parseInt(roomId));
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
                Log.d(TAG, "Subscription Rejected");
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
                    hideKeyboard(ChatActivity.this);
                    DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), "An unauthorized connection attempt was rejected");

                } else {
                    hideKeyboard(ChatActivity.this);
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
                chatRecyclerView.swapAdapter(chatAdapter, false);
                chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                if (message.isCurrentUser()) {
                    editTextChat.setText("");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
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

    private void fetchPreviousMessages(String roomId) {
        chatProgressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new RetrofitClient().getAlibabaCookiesApi(this);
        IChef iChef = retrofit.create(IChef.class);
        final Call<List<RetrieveMessagesResponse>> fetchMessages = iChef.getPreviousMessages(roomId);
        fetchMessages.enqueue(new Callback<List<RetrieveMessagesResponse>>() {
            @Override
            public void onResponse(Call<List<RetrieveMessagesResponse>> call, Response<List<RetrieveMessagesResponse>> response) {
                chatProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response != null && response.body() != null) {
                    renderMessagesToUI(response.body());
                } else {
                    DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), String.valueOf(R.string.error_message));
                }
            }

            @Override
            public void onFailure(Call<List<RetrieveMessagesResponse>> call, Throwable t) {
                chatProgressBar.setVisibility(View.GONE);
                DisplayToast.makeSnackbar(getWindow().getDecorView().getRootView(), String.valueOf(R.string.error_message));
            }
        });
    }

    private void renderMessagesToUI(List<RetrieveMessagesResponse> oldMessages) {

        for (int i = 0; i < oldMessages.size(); i++) {

            final Message message = new Message();
            message.setSenderName(oldMessages.get(i).getSenderName());
            message.setMessage(oldMessages.get(i).getMessage());
            message.setTime(oldMessages.get(i).getTime());
            if (message.getSenderName().toLowerCase().trim().equalsIgnoreCase(prefs.getStringValue(PreferenceConstants.LOGGED_IN_USER_NAME, "").toLowerCase().trim())) {
                message.setCurrentUser(true);
            } else {
                message.setCurrentUser(false);
            }

            messageList.add(message);
        }

        chatAdapter = new ChatAdapter(getApplicationContext(), messageList);
        chatRecyclerView.swapAdapter(chatAdapter, false);
        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);

        startChatting();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}


