package com.hackathon.codechefapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackathon.codechefapp.R;
import com.hackathon.codechefapp.listener.OnItemClickListener;
import com.hackathon.codechefapp.model.search.Content;
import com.hackathon.codechefapp.utils.DisplayToast;

import java.util.ArrayList;

/**
 * Created by SANDIP JANA on 16-09-2018.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private ArrayList<Content> searchData;

    private static OnItemClickListener onItemClickListener;

    public SearchAdapter( ArrayList<Content> searchData ) {
        this.searchData = searchData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView fullname;
        private TextView username;

        public ViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.codechefUsername);
            fullname = view.findViewById(R.id.fullname);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if( onItemClickListener!=null ) {
                onItemClickListener.onItemClick( view , getAdapterPosition() );
            }
        }
    }


    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_adapter , parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.fullname.setText( searchData.get(position).getUsernameCamelCase() );
        holder.username.setText( searchData.get(position).getUsername() );
    }

    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return searchData.size();
    }

    public void clear() {
        if(searchData!=null) {
            searchData.clear();
            searchData = null;
        }

        if(onItemClickListener!=null) {
            onItemClickListener = null;
        }
    }

}
