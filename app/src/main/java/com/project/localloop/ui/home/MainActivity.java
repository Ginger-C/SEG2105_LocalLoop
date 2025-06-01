package com.project.localloop.ui.home;

import com.project.localloop.database.User;
import com.project.localloop.repository.DataRepository;
import com.project.localloop.ui.login.LoginRegisterActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import androidx.lifecycle.Observer;
import com.project.localloop.R;

public class MainActivity extends AppCompatActivity {
    // Firebase encapsulated
    private final DataRepository repo = DataRepository.getInstance();
    protected Bundle bundle;

    // User info: passed via Intent or Firebase, shared via Bundle
    private long accountType = -1; //default -1: never pssed correct value
    private String userName = null;
    private boolean isDisbled; // If disabled, disable home page buttons
    // Swipe operations
    private int currentIndex = 0; // Central fragment page
    private ViewPager2 viewPager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private BottomNavigationView botNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Step 1: Assign bottom navigation and viewPager
        botNav = findViewById(R.id.main_bottom_nav);
        viewPager = findViewById(R.id.main_view_pager);
        viewPager.setAdapter(new MainPagerAdapter(this));

        // Step 2: Bottom nav fragment switch → now delegate to ViewPager2
        botNav.setOnItemSelectedListener(item -> {
            int newIndex = 1;
            int id = item.getItemId();
            if (id == R.id.botNav_left) {
                newIndex = 0;
            } else if (id == R.id.botNav_center) {
                newIndex = 1;
            } else if (id == R.id.botNav_right) {
                newIndex = 2;
            } else {
                return false;
            }
            viewPager.setCurrentItem(newIndex, true);
            currentIndex = newIndex;
            return true;
        });

        // Step 3: By default, bot nav select center fragment
        botNav.setSelectedItemId(R.id.botNav_center);
        viewPager.setCurrentItem(1, false);

        // Step 4: Sync swipe → bottom nav selection
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
                if (position == 0) {
                    botNav.setSelectedItemId(R.id.botNav_left);
                } else if (position == 1) {
                    botNav.setSelectedItemId(R.id.botNav_center);
                } else if (position == 2) {
                    botNav.setSelectedItemId(R.id.botNav_right);
                }
            }
        });

        // Step 5: Drawer toggle
        Toolbar toolbar = findViewById(R.id.main_top_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.main_drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Step 6: Drawer menu
        NavigationView navView = findViewById(R.id.main_nav_drawer);
        View header = navView.getHeaderView(0);
        //==================UI Components========================
        TextView userNameView = header.findViewById(R.id.nav_user_name);
        userNameView.setText(userName);
        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            // TODO:  handle menu items
            if (id == R.id.nav_menu_logout) {
                item.setChecked(true);
                FirebaseAuth.getInstance().signOut();
                repo.removeAllListeners();

                Intent intent = new Intent(MainActivity.this, LoginRegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


                finish();
            }
            drawerLayout.closeDrawers(); // Close drawer after selection
            return true;
        });

        // Step 7: get user bundle
        this.initUserData();
    }

    /**
     * Adapter for ViewPager2.
     */
    private static class MainPagerAdapter extends FragmentStateAdapter {
        private final MainActivity activity;

        public MainPagerAdapter(@NonNull MainActivity activity) {
            super(activity);
            this.activity = activity;
        }

        @Override
        public int getItemCount() {
            return 3; // 3 frag pages
        }

        /**
         * Create fragment based on user current position。
         */
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new EventPageFragment();
                case 1:
                    return activity.createCenterFragment();
                case 2:
                    return activity.createRightFragment();
                default:
                    throw new IndexOutOfBoundsException("pos=" + position);
            }
        }
    }
    //================== Page Content Generators ========================
    /**
     * Generate content of center fragment based on user role
     */
    private Fragment createCenterFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        bundle.putLong("accountType", accountType);
        Log.d("MainActivity", "Passing to fragment: userName=" + userName + " | accountType=" + accountType);

        Fragment target;
        if (accountType == 0L) {
            target = new HomeAdminFragment();
        } else if (accountType == 1L) {
            target = new HomeHostFragment();
        } else if (accountType == 2L) {
            target = new HomeParticipantFragment();
        } else {
            target = new Fragment(); // Empty fragment
            Log.d("MainActivity", "Unknown user role: " + accountType);
        }
        target.setArguments(bundle);
        return target;
    }
    /**
     * Generate content of right fragment based on user role
     */
    private Fragment createRightFragment() {
        if (accountType == 0L) {
            return new HomeRightFragment_Cateogry();
        } else {
            return new HomeRightFragment_Notif();
        }
    }

    // Utility function: if bundle passed, get data from bundle.
    // if not, get from db.
    private void initUserData() {
        accountType = getIntent().getLongExtra("accountType", -1);
        userName = getIntent().getStringExtra("userName");

        if (userName == null || accountType == -1) {
            try {
                String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                repo.getUserInstance(currentUid)
                        .observe(MainActivity.this, new Observer<User>() {
                            @Override
                            public void onChanged(User currentUser) {
                                if (currentUser != null) {
                                    // 1. Passing values to activity
                                    userName    = currentUser.getUserName();
                                    accountType = currentUser.getAccountType();
                                    isDisbled   = currentUser.isSuspended();
                                    bundle = new Bundle();
                                    bundle.putSerializable("loggedIn_user", currentUser);

                                    viewPager.setAdapter(new MainPagerAdapter(MainActivity.this));
                                    viewPager.setCurrentItem(currentIndex);

                                /*UserDetailFragment detailFrag = new UserDetailFragment();
                                detailFrag.setArguments(bundle);
                                requireActivity()
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.frag_container, detailFrag)
                                        .addToBackStack(null)
                                        .commit();
                            });*/
                                }
                            }
                        });
            } catch (Exception e) {
                Log.e("MainActivity", "Error getting user info: " + e);
            }
        }
    }

}
