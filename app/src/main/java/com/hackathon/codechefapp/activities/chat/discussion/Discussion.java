package com.hackathon.codechefapp.activities.chat.discussion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.activities.chat.ChatActivity;
import com.hackathon.codechefapp.constants.Constants;

public class Discussion extends AppCompatActivity {

    private ImageView generalImg;
    private ImageView ioiImg;
    private ImageView acmImg;
    private ImageView jobsImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        generalImg = findViewById(R.id.general);
        ioiImg = findViewById(R.id.ioi);
        acmImg = findViewById(R.id.acm);
        jobsImg = findViewById(R.id.discuss);

        addListeners();
    }

    private void addListeners() {
        generalImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChatActivity(Constants.GENERAL_CHAT_ROOM);
            }
        });
        ioiImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChatActivity(Constants.IOI_CHAT_ROOM);
            }
        });
        acmImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChatActivity(Constants.ACM_CHAT_ROOM);
            }
        });
        jobsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChatActivity(Constants.JOB_CHAT_ROOM);
            }
        });
    }

    private void startChatActivity( String roomId ) {
        Intent intent = new Intent(Discussion.this, ChatActivity.class);
        intent.putExtra(Constants.ROOM_ID , roomId);
        startActivity(intent);
    }

}
