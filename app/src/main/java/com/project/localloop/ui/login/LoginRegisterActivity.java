package com.project.localloop.ui.login;
import com.project.localloop.ui.home.MainActivity;

import android.os.Bundle;
import com.project.localloop.R;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;

public class LoginRegisterActivity extends AppCompatActivity {

    // Fragment tags: to identify which fragment is currently displayed
    private static final String LOGIN_FRAGMENT_TAG = "login_fragment";
    private static final String REGISTER_FRAGMENT_TAG = "register_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);

        // On login fragment by default
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.userLogIn_fragContainer, new LoginFragment(), LOGIN_FRAGMENT_TAG)
                    .commit();
        }
    }

    // Fragment Jump: register page
    public void showRegisterFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.userLogIn_fragContainer, new RegisterFragment(), REGISTER_FRAGMENT_TAG)
                .addToBackStack(null) // Can return
                .commit();
    }
    // Fragment Jump: jump to MainActivity.java once login successfully
    public void onLoginSuccess(String userName, int accountType) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userName", userName);// bring username to MainActivity
        intent.putExtra("accountType", accountType);
        Log.d("LoginRegisterActivity", "onLoginSuccess: " + userName + " " + accountType);
        startActivity(intent);
        finish(); // don't allow going back to login-in at this point
    }

}
