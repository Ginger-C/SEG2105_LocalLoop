package com.project.localloop.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.localloop.R;

import com.project.localloop.database.User;
import com.project.localloop.repository.DataRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeAdminFragment extends Fragment {

    // Firebase encapsulated
    private final DataRepository repo = DataRepository.getInstance();

    // Param passed from Bundle :current user
    private String curUserName;
    private long curAccountType;


    // To view currently registered user list
    private RecyclerView userListView;
    private UserListAdapter ulAdapter;


    public static HomeAdminFragment newInstance(String name, long role) {
        HomeAdminFragment fragment = new HomeAdminFragment();
        Bundle args = new Bundle();
        args.putString("userName", name);
        args.putLong("accountType", role);
        fragment.setArguments(args);
        Log.d("HomeAdminFragment", "Bundle set: userName=" + name + " | accountType=" + role);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_home_admin, container, false);
        // Initialize recycler view
        userListView = root.findViewById(R.id.recycler_user_list);
        userListView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Initialize adapter
        ulAdapter = new UserListAdapter(new ArrayList<>());
        userListView.setAdapter(ulAdapter);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv = view.findViewById(R.id.tv_welcome);
        Bundle args = getArguments();

        // Receive bundle
        if (args != null) {
            curUserName = args.getString("userName", "User");
            curAccountType = args.getLong("accountType", -1);

            Log.d("HomeHostFragment", "Received bundle: userName=" + curUserName + " | accountType=" + curAccountType);
        } else {
            Log.e("HomeHostFragment", "No arguments received");
            tv.setText("Welcome (unknown)");
        }

        // Observe data change
        repo.getAllUsers().observe(getViewLifecycleOwner(), userList -> {
            ulAdapter.updateData(userList);
        });
    }

    // Formatter: accountType(long to String)
    private String roleToString(long role) {
        if (role == 0) return "Admin";
        if (role == 1) return "Host";
        if (role == 2) return "Participant";
        return "Unknown";
    }

    // Refresh for getting new results

}
