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
    private long accountType;

    public static HomeHostFragment newInstance(String name, long role) {
        HomeHostFragment fragment = new HomeHostFragment();
        Bundle args = new Bundle();
        args.putString("userName", name);
        args.putLong("accountType", role);
        fragment.setArguments(args);
        Log.d("HomeHostFragment", "Bundle set: userName=" + name + " | accountType=" + role);
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
            accountType = args.getLong("accountType", -1);

            Log.d("HomeHostFragment", "Received bundle: userName=" + userName + " | accountType=" + accountType);

            String roleText = roleToString(accountType);
            tv.setText("Welcome, " + userName + "! You are logged in as (" + roleText + ").");
        } else {
            Log.e("HomeHostFragment", "No arguments received");
            tv.setText("Welcome (unknown)");
        }
    }

    // Formatter: accountType(long to String)
    private String roleToString(long role) {
        if (role == 0) return "Admin";
        if (role == 1) return "Host";
        if (role == 2) return "Participant";
        return "Unknown";
    }
}
