package com.hackathon.codechefapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.dao.chat.Message;

import java.util.ArrayList;

/**
 * Created by SANDIP JANA on 23-09-2018.
 */

public class ChatAdapter extends RecyclerView.Adapter {

    private static final int MESSAGE_SENT = 1;
    private static final int MESSAGE_RECEIVED = 2;

    private Context context;
    private ArrayList<Message> messageList;

    public ChatAdapter(Context context, ArrayList<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    private class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageTxt;
        private final TextView timeTxt;

        SentMessageViewHolder(View view) {
            super(view);

            messageTxt = (TextView) view.findViewById(R.id.text_message_body);
            timeTxt = (TextView) view.findViewById(R.id.text_message_time);
        }

    }

    private class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageTxt;
        private final TextView timeTxt;
        private final TextView nameTxt;
        private final ImageView profileImage;

        ReceivedMessageViewHolder(View view) {
            super(view);

            messageTxt = (TextView) itemView.findViewById(R.id.text_message_body);
            timeTxt = (TextView) itemView.findViewById(R.id.text_message_time);
            nameTxt = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_SENT) {
            return new SentMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_sent, parent, false));
        } else if (viewType == MESSAGE_RECEIVED) {
            return new ReceivedMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_received, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case MESSAGE_SENT:

                ((SentMessageViewHolder) holder).messageTxt.setText(messageList.get(position).getMessage());
                ((SentMessageViewHolder) holder).timeTxt.setText(messageList.get(position).getTime());

                break;
            case MESSAGE_RECEIVED:

                ((ReceivedMessageViewHolder) holder).messageTxt.setText(messageList.get(position).getMessage());
                ((ReceivedMessageViewHolder) holder).nameTxt.setText(messageList.get(position).getSenderName());
                ((ReceivedMessageViewHolder) holder).timeTxt.setText(messageList.get(position).getTime());
                ((ReceivedMessageViewHolder) holder).profileImage.setImageDrawable(context.getResources().getDrawable(R.drawable.circled_codechef_face));

                break;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);

        if (message.isCurrentUser())
            return MESSAGE_SENT;
        else
            return MESSAGE_RECEIVED;
    }

}
