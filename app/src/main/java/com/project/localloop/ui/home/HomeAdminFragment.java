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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

    // To refresh user list
    private SwipeRefreshLayout swipeRefresh;


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
        // Initialize swipe refresh
        swipeRefresh = root.findViewById(R.id.adminFrag_swipeRefresh);
        // Refresh content upon creation
        swipeRefresh.setRefreshing(true);
        repo.fetchAllRegisteredUsersOnce(new DataRepository.RegUserListCallback() {
            @Override
            public void onSuccess(List<User> registeredUserList) {
                ulAdapter.updateData(registeredUserList);
                swipeRefresh.setRefreshing(false); // 停止加载动画
            }

            @Override
            public void onError(String errorMsg) {
                Log.e("HomeAdminFragment", "Failed to fetch users: " + errorMsg);
                swipeRefresh.setRefreshing(false);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();

        // Receive bundle : test
        if (args != null) {
            curUserName = args.getString("userName", "User");
            curAccountType = args.getLong("accountType", -1);
            Log.d("HomeHostFragment", "Received bundle: userName=" + curUserName + " | accountType=" + curAccountType);
        }
        // Refresh for getting new results
        swipeRefresh.setOnRefreshListener(() -> {
            repo.fetchAllRegisteredUsersOnce(new DataRepository.RegUserListCallback() {
                @Override
                public void onSuccess(List<User> registeredUserList) {
                    ulAdapter.updateData(registeredUserList);
                    swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onError(String errorMsg) {
                    Log.e("HomeAdminFragment", "Failed to fetch users: " + errorMsg);
                    swipeRefresh.setRefreshing(false);
                }
            });
        });

    }

}
