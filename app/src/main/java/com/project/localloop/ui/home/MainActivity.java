package com.project.localloop.ui.home;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.localloop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    // User info: passed via Intent or Firebase, shared via Bundle
    private Long userRole;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Step 1: Get from login
        userRole = (long) getIntent().getIntExtra("accountType", -1);
        userName = getIntent().getStringExtra("userName");

        // Step 2: If intent fails, fallback to Firestore
        if (userRole == null || userRole == -1) {
            try {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(uid)
                        .get()
                        .addOnSuccessListener(doc -> {
                            if (doc.exists()) {
                                userRole = doc.getLong("accountType");
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
            int id = item.getItemId();
            if (id == R.id.botNav_left) {
                target = new EventPageFragment();
            } else if (id == R.id.botNav_center) {
                target = createCenterFragment();
            } else if (id == R.id.botNav_right) {
                target = createRightFragment();
            } else {
                return false;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, target)
                    .commit();
            return true;
        });

        // Step 4: Default center fragment
        nav.setSelectedItemId(R.id.botNav_center);
    }

    /**
     * Generate content of center fragment based on user role
     * */
    private Fragment createCenterFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        bundle.putLong("accountType", userRole);

        Fragment target;

        if (userRole == 0L) {
            target = new HomeAdminFragment();
        } else if (userRole == 1L) {
            target = new HomeHostFragment();
        } else {
            target = new HomeParticipantFragment();
        }

        target.setArguments(bundle);
        return target;
    }

    /**
     * Generate content of right fragment based on user role
     * */
    private Fragment createRightFragment() {
        if (userRole == 0L) {
            return new HomeRightFragment_UL();
        } else {
            return new HomeRightFragment_Notif();
        }
    }
}
