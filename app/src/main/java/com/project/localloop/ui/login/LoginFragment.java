package com.project.localloop.ui.login;

import android.os.Bundle;
import com.project.localloop.R;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModelProvider;

public class LoginFragment extends Fragment {

    private SharedViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 关联 fragment_login.xml
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 获取 ViewModel（Activity 范围共享）
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // 找到输入框
        EditText emailInput = view.findViewById(R.id.login_emailEditText);
        EditText passwordInput = view.findViewById(R.id.login_passwordEditText);

        // 实时监听输入
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setEmail(s.toString());
            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setPassword(s.toString());
            }
        });

        // 登录失败 -> 点击“去注册”
        Button signUpButton = view.findViewById(R.id.user_signUpButton);
        signUpButton.setOnClickListener(v -> {
            if (getActivity() instanceof LoginRegisterActivity) {
                ((LoginRegisterActivity) getActivity()).showRegisterFragment();
            }
        });
    }
}
