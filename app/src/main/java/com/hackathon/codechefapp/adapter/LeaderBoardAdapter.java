package com.hackathon.codechefapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.listener.OnItemClickListener;
import com.hackathon.codechefapp.model.leaderboard.LeaderBoardResponse;

import java.util.List;

/**
 * Created by SANDIP JANA on 29-09-2018.
 */
public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {

    private List<LeaderBoardResponse> data;

    private static OnItemClickListener onItemClickListener;

    public LeaderBoardAdapter(List<LeaderBoardResponse> data) {
        this.data = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final private TextView username;
        final private TextView score;

        public ViewHolder(View view) {
            super(view);

            username = view.findViewById(R.id.codechefUsername);
            score = view.findViewById(R.id.score);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public LeaderBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.leaerboard_row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(LeaderBoardAdapter.ViewHolder holder, int position) {
        ((ViewHolder) holder).username.setText(data.get(position).getCurrentUser());
        ((ViewHolder) holder).score.setText(data.get(position).getScore().toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
