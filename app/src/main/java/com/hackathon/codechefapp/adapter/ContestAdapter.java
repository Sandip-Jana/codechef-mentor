package com.hackathon.codechefapp.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.listener.OnItemClickListener;
import com.hackathon.codechefapp.model.contests.ShowContests.ContestList;

import java.util.List;

/**
 * Created by SANDIP JANA on 29-09-2018.
 */
public class ContestAdapter extends RecyclerView.Adapter<ContestAdapter.ViewHolder> {

    private List<ContestList> data;

    private static OnItemClickListener onItemClickListener;

    public ContestAdapter(List<ContestList> data) {
        this.data = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final private TextView contestCode;
        final private TextView contestName;
        final private TextView startDate;
        final private TextView endDate;
        final private CardView contestCard;

        public ViewHolder(View view) {
            super(view);

            contestCode = view.findViewById(R.id.contestCode);
            contestName = view.findViewById(R.id.contestName);
            startDate = view.findViewById(R.id.startDate);
            endDate = view.findViewById(R.id.endDate);
            contestCard = view.findViewById(R.id.contestCardView);

            view.setOnClickListener(this);
            contestCard.setOnClickListener(this);
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
    public ContestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contest_row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ContestAdapter.ViewHolder holder, int position) {
        ((ViewHolder) holder).contestCode.setText(data.get(position).getCode());
        ((ViewHolder) holder).contestName.setText(data.get(position).getName());
        ((ViewHolder) holder).startDate.setText(data.get(position).getStartDate());
        ((ViewHolder) holder).endDate.setText(data.get(position).getEndDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
