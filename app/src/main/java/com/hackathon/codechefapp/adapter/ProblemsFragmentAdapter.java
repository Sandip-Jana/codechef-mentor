package com.hackathon.codechefapp.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.listener.OnItemClickListener;
import com.hackathon.codechefapp.model.contests.ShowProblems.ProblemsList;

import java.util.List;

/**
 * Created by SANDIP JANA on 29-09-2018.
 */
public class ProblemsFragmentAdapter extends RecyclerView.Adapter<ProblemsFragmentAdapter.ViewHolder> {

    private List<ProblemsList> data;

    private static OnItemClickListener onItemClickListener;

    public ProblemsFragmentAdapter(List<ProblemsList> data) {
        this.data = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final private TextView problemCode;
        final private TextView submissionsCnt;
        final private TextView accuracy;
        final private CardView problemsCardView;

        public ViewHolder(View view) {
            super(view);

            problemCode = view.findViewById(R.id.problemCode);
            submissionsCnt = view.findViewById(R.id.submissionsCnt);
            accuracy = view.findViewById(R.id.accuracy);
            problemsCardView = view.findViewById(R.id.problemsCardView);

            problemsCardView.setOnClickListener(this);
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
    public ProblemsFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.problems_row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ProblemsFragmentAdapter.ViewHolder holder, int position) {
        ((ViewHolder) holder).problemCode.setText(data.get(position).getProblemCode());
        ((ViewHolder) holder).submissionsCnt.setText("Successfull Submissions: " + data.get(position).getSuccessfulSubmissions());
        ((ViewHolder) holder).accuracy.setText("Accuracy: " + String.format("%.2f", data.get(position).getAccuracy()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
