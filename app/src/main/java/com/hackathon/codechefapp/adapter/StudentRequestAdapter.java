package com.hackathon.codechefapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.listener.OnItemClickListener;
import com.hackathon.codechefapp.model.search.Content;

import java.util.ArrayList;

/**
 * Created by SANDIP JANA on 22-09-2018.
 */

public class StudentRequestAdapter extends RecyclerView.Adapter<StudentRequestAdapter.ViewHolder> {
    private ArrayList<Content> searchData;

    private static OnItemClickListener onItemClickListener;

    public StudentRequestAdapter(ArrayList<Content> searchData) {
        this.searchData = searchData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout userDetailsLayout;
        private TextView fullname;
        private TextView username;
        private Button acceptButton;
        private Button rejectButton;

        public ViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.codechefUsername);
            fullname = view.findViewById(R.id.fullname);
            acceptButton = view.findViewById(R.id.acceptBtn);
            rejectButton = view.findViewById(R.id.rejectBtn);
            userDetailsLayout = view.findViewById(R.id.userDetailsLayout);

            acceptButton.setOnClickListener(this);
            rejectButton.setOnClickListener(this);
            userDetailsLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.acceptBtn:
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, getAdapterPosition());
                    }
                    break;
                case R.id.rejectBtn:
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, getAdapterPosition());
                    }
                    break;
                case R.id.userDetailsLayout:
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(view, getAdapterPosition());
                    }
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public StudentRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.student_requests_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.fullname.setText(searchData.get(position).getUsernameCamelCase());
        holder.username.setText(searchData.get(position).getUsername());
    }

    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return searchData.size();
    }

    public void clear() {
        if (searchData != null) {
            searchData.clear();
            searchData = null;
        }

        if (onItemClickListener != null) {
            onItemClickListener = null;
        }
    }

}
