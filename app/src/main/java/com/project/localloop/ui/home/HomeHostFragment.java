package com.project.localloop.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.localloop.R;

public class HomeHostFragment extends Fragment {

    // Param passed from Bundle
    private String userName;
    private long userRole;

    public static HomeHostFragment newInstance(String name, long role) {
        HomeHostFragment fragment = new HomeHostFragment();
        Bundle args = new Bundle();
        args.putString("userName", name);
        args.putLong("userRole", role);
        fragment.setArguments(args);
        Log.d("HomeHostFragment", "Bundle set: userName=" + name + " | userRole=" + role);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_host, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv = view.findViewById(R.id.tv_welcome);
        Bundle args = getArguments();

        if (args != null) {
            userName = args.getString("userName", "User");
            userRole = args.getLong("userRole", -1);

            Log.d("HomeHostFragment", "Received bundle: userName=" + userName + " | userRole=" + userRole);

            String roleText = roleToString(userRole);
            tv.setText("Welcome, " + userName + " (" + roleText + ")");
        } else {
            Log.e("HomeHostFragment", "No arguments received");
            tv.setText("Welcome (unknown)");
        }
    }

    // Helper: convert userRole to readable name
    private String roleToString(long role) {
        if (role == 0) return "Admin";
        if (role == 1) return "Participant";
        if (role == 2) return "Host";
        return "Unknown";
    }
}
