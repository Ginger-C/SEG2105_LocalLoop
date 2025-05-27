package com.project.localloop.ui.login;

import android.os.Bundle;
import com.project.localloop.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

    // 供 Fragment 调用的跳转方法（例如 LoginFragment -> RegisterFragment）
    public void showRegisterFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.userLogIn_fragContainer, new RegisterFragment(), REGISTER_FRAGMENT_TAG)
                .addToBackStack(null) // 加入返回栈，用户可以返回
                .commit();
    }
}
