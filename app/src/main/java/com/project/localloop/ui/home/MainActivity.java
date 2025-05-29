package com.project.localloop.ui.home;
import com.project.localloop.ui.login.LoginRegisterActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.project.localloop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    // User info: passed via Intent or Firebase, shared via Bundle
    private Long accountType;
    private String userName;
    private TextView userNameView;
    private int currentIndex = 0; // Central fragment page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Step 1: Get from login
        accountType = getIntent().getLongExtra("accountType", -1);
        userName = getIntent().getStringExtra("userName");

        // Step 2: If intent fails, fallback to Firestore
        if (accountType == null || accountType == -1) {
            try {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(uid)
                        .get()
                        .addOnSuccessListener(doc -> {
                            if (doc.exists()) {
                                accountType = doc.getLong("accountType");
                                userName = doc.getString("userName");

                                // After retrieving data, trigger center page load
                                findViewById(R.id.main_fragment_container).post(() -> {
                                    BottomNavigationView nav = findViewById(R.id.main_bottom_nav);
                                    nav.setSelectedItemId(R.id.botNav_center);
                                });
                            }
                        });
            } catch (Exception e) {
                Log.e("MainActivity", "Error getting user info: " + e);
            }
        }

        // Step 3: Bottom nav fragment switch
        BottomNavigationView nav = findViewById(R.id.main_bottom_nav);
        nav.setOnItemSelectedListener(item -> {
            Fragment target;
            int newIndex = 0; // used for fragment switch animation

            int id = item.getItemId();
            if (id == R.id.botNav_left) {
                newIndex = 1;
                target = new EventPageFragment();
            } else if (id == R.id.botNav_center) {
                newIndex = 0;
                target = createCenterFragment();
            } else if (id == R.id.botNav_right) {
                newIndex = 1;
                target = createRightFragment();
            } else {
                return false;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.main_fragment_container, target)
                    .commit();

            currentIndex = newIndex;
            return true;
        });

        // Step 4: Default center fragment
        nav.setSelectedItemId(R.id.botNav_center);

        // Step 5: Drawer toggle
        Toolbar toolbar = findViewById(R.id.main_top_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // Step 6: Drawer menu
        NavigationView navView = findViewById(R.id.main_nav_drawer);
        View header = navView.getHeaderView(0);
        userNameView = header.findViewById(R.id.nav_user_name);
        userNameView.setText(userName);
        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            // TODO:  handle menu items
            if (id == R.id.nav_menu_logout) {
                item.setChecked(true);
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(MainActivity.this, LoginRegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finish();
            }
            drawerLayout.closeDrawers(); // Close drawer after selection
            return true;
        });
    }

    /**
     * Generate content of center fragment based on user role
     * */
    private Fragment createCenterFragment() {
        // Get user info from Bundle
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        bundle.putLong("accountType", accountType);
        Log.d("MainActivity", "Passing to fragment: userName=" + userName + " | accountType=" + accountType);

        Fragment target;

        if (accountType == 0L) {
            target = new HomeAdminFragment();
        } else if (accountType == 1L) {
            target = new HomeHostFragment();
        } else if (accountType == 2L){
            target = new HomeParticipantFragment();
        }
        else {
            target = new Fragment(); // Empty fragment
            Log.d("MainActivity", "Unknown user role: " + accountType);
        }

        target.setArguments(bundle);
        return target;
    }

    /**
     * Generate content of right fragment based on user role
     * */
    private Fragment createRightFragment() {
        if (accountType == 0L) {
            return new HomeRightFragment_UL();
        } else {
            return new HomeRightFragment_Notif();
        }
    }
}
