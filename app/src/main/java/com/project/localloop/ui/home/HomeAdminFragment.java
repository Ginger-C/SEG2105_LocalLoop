package com.project.localloop.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.project.localloop.R;

public class HomeAdminFragment extends Fragment {

    public static HomeAdminFragment newInstance() {
        return new HomeAdminFragment();
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_host, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get username from MainActivity
        TextView tv = view.findViewById(R.id.tv_welcome);
        String userName = requireActivity()
                .getIntent()
                .getStringExtra(MainActivity.EXTRA_USERNAME);
        String userRole = requireActivity()
                .getIntent()
                .getStringExtra(MainActivity.EXTRA_ROLE);
        tv.setText(String.format("Welcome", userName, userRole));
    }
}
