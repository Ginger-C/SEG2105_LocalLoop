package com.project.localloop.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.localloop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
/**
 * MainActivity
 * This is home page after log-in.
 * The page binds corresponding Fragments to a BottomNavigationView
 * and handles fragment switching based on the currently authenticated user’s role.
 *
 * <p><strong>Fragment assignment logic:</strong></p>
 * <ul>
 *   <li><b>Left:</b> {@code EventPageFragment} — shows events/allow event search and browsing.</li>
 *   <li><b>Center:</b> {@link #createCenterFragment()} — chooses one of:
 *     <ul>
 *       <li><b>ADMIN:</b> {@code CategoryFragment} (category management UI)</li>
 *       <li><b>HOST:</b> Fragment for managing events created by this host</li>
 *       <li><b>PARTICIPANT:</b> Fragment for managing events the user has joined</li>
 *     </ul>
 *     <p>The center page also supports filtering events by status:
 *       <ol>
 *         <li>Open for registration</li>
 *         <li>Created / Joined / Pending</li>
 *         <li>Closed / Ended / Participated/ Cancelled events will be moved to event history</li>
 *         <li>Event history => in Navigation Bar</li>
 *       </ol>
 *     </p>
 *   </li>
 *   <li><b>Right:</b> {@link #createRightFragment()} — chooses one of:
 *     <ul>
 *       <li><b>ADMIN:</b> {@code UserListFragment} (user management UI)</li>
 *       <li><b>HOST & PARTICIPANT:</b> {@code NotificationCenterFragment} (pending requests/notifications)</li>
 *     </ul>
 *   </li>
 * </ul>
 * @author Ginger-C
 * @since 2025-05-28
 */
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_ROLE = "accountType";
    public static final String EXTRA_USERNAME = "userName";
    private Long userRole; // In case not passed by intent, long for firebase
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Getting user role and user name
        // 1. Try from Intent
        userRole = (long) getIntent().getIntExtra(EXTRA_ROLE,-1);
        userName = getIntent().getStringExtra(EXTRA_USERNAME);
        // 2. If param not passed, get from firebase
        if (userRole == null || userRole == -1) {
            try{
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                // From users collection, get account type
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(uid)
                        .get()
                        .addOnSuccessListener(doc -> {
                            if (doc.exists()) {
                                userRole = doc.getLong(EXTRA_ROLE);
                                userName = doc.getString(EXTRA_USERNAME);
                                // After getting account type, start fragment based on type
                                findViewById(R.id.main_fragment_container)
                                        .post(() ->
                                                ((BottomNavigationView) findViewById(R.id.main_bottom_nav))
                                                        .setSelectedItemId(R.id.botNav_center)
                                        );
                            }
                        });
            }catch(Exception e)
            {
                Log.e("MainActivity", "ErrorError getting user role: " + e);
            }
        }

        // 3. Binding BottomNavigation + fragment
        BottomNavigationView nav = findViewById(R.id.main_bottom_nav);
        nav.setOnItemSelectedListener(item -> {
            Fragment target;
            int id = item.getItemId();
            if (id == R.id.botNav_left) {
                //Generate content of Left fragment
                //for all users, left fragment  = event page
                    target = new EventPageFragment();
                } else if (id == R.id.botNav_center) {
                // Center fragment
                    target = createCenterFragment();
                } else if (id == R.id.botNav_right) {
                // Right fragment
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

        // 4. Select center fragment when launching
        nav.setSelectedItemId(R.id.botNav_center);
    }

    /**
     * Generate content of center fragment based on user role
     * */
    private Fragment createCenterFragment() {
        if (userRole.intValue() == 0) { //ADMIN
            return new HomeAdminFragment();
        } else if (userRole.intValue() == 1) { //HOST
            return new HomeHostFragment();
        } else if (userRole.intValue() == 2){
            return new HomeParticipantFragment();
        }
        else
        {
            Log.e("MainActivity", "Invalid user role");
            return null;
        }
    }

    /**
     * Generate content of right fragment based on user role
     * */
    private Fragment createRightFragment() {
        if (userRole.intValue() == 0){
            // Admin : User List Page
            return new HomeRightFragment_UL();
        } else {
            // Host & Participant : Notification Center
            return new HomeRightFragment_Notif();
        }
    }
}
