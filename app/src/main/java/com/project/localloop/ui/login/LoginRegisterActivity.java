package com.project.localloop.ui.login;

import android.os.Bundle;
import com.project.localloop.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class LoginRegisterActivity extends AppCompatActivity {

    // Fragment 标签（用于识别当前显示的是哪个 fragment）
    private static final String LOGIN_FRAGMENT_TAG = "login_fragment";
    private static final String REGISTER_FRAGMENT_TAG = "register_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);

        // 默认显示 LoginFragment
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
