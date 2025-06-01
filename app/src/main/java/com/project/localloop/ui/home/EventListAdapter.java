package com.project.localloop.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.localloop.R;
import com.project.localloop.database.User;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter <EventListAdapter.EventListViewHolder> {

    private List <User> dataList;

    public static class EventListViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView emailView;

        public EventListViewHolder(View itemView) {
            super(itemView);
            //nameView = itemView.findViewById(R.id.text_user_name);
            //emailView = itemView.findViewById(R.id.text_user_email);
        }
    }

    public EventListAdapter(List<User> list) {
        this.dataList = list;
    }

    @NonNull
    @Override
    public EventListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_registered_user, parent, false);
        return new EventListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventListViewHolder holder, int position) {
        User data = dataList.get(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

