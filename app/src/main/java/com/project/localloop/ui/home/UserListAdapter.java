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

/**
 * UserListAdapter.java
 * Adapter for preparing data for RecyclerView, showing user list stored in Firebase
 * synchronously in HomeFragment
 *
 * @author Ginger-C
 */
public class UserListAdapter extends RecyclerView.Adapter <UserListAdapter.UserListViewHolder> {

    private List <User> userList; // read-only

    public static class UserListViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxtView;
        TextView emailTxtView;
        TextView accountTypeTxtView;
        TextView statusTxtView;

        public UserListViewHolder(View itemView) {
            super(itemView);
            nameTxtView = itemView.findViewById(R.id.adminFrag_userName);
            emailTxtView = itemView.findViewById(R.id.adminFrag_email);
            accountTypeTxtView = itemView.findViewById(R.id.adminFrag_accountType);
            statusTxtView = itemView.findViewById(R.id.adminFrag_status);
        }
    }

    public UserListAdapter(List<User> list) {
        this.userList = list;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_registered_user , parent, false);
        return new UserListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameTxtView.setText(user.getUserName());
        holder.emailTxtView.setText(user.getEmail());
        holder.accountTypeTxtView.setText(user.getAccountType() == 1 ? "Host" : "Participant");
        holder.statusTxtView.setText(user.isSuspended() ? "Suspended" : "Active");
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    void updateData(List<User> newList)
    {
        this.userList = newList;
        notifyDataSetChanged();

    }
}

