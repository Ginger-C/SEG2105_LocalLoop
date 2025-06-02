package com.project.localloop.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.localloop.R;
import com.project.localloop.database.Event;
import com.project.localloop.database.User;

import java.util.List;

/**
 * EventListAdapter.java
 * Adapter for preparing data for RecyclerView, showing event list stored in Firebase
 * synchronously in EventPageFragment
 * refresh AUTOMATICALLY (as for 25/6/1)
 *
 * @author Ginger-C
 */
public class EventListAdapter extends RecyclerView.Adapter <EventListAdapter.EventListViewHolder> {

    private List <Event> eventList; // read-only

    public static class EventListViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxtView;
        TextView emailTxtView;
        TextView accountTypeTxtView;
        TextView statusTxtView;

        public EventListViewHolder(View itemView) {
            super(itemView);
          /*  nameTxtView = itemView.findViewById(R.id.adminFrag_userName);*/
        }
    }

    public EventListAdapter(List<Event> list) {
        this.eventList = list;
    }

    @NonNull
    @Override
    public EventListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_registered_event , parent, false);
        return new EventListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventListViewHolder holder, int position) {
        Event event = eventList.get(position);
        /*holder.nameTxtView.setText(event.getUserName());
        holder.emailTxtView.setText(user.getEmail());*/
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

   /* void updateData(List<Event> newList)
    {
        this.eventList = newList;
        notifyDataSetChanged();

    }*/
}

