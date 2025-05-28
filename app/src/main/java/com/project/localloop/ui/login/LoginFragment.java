package com.project.localloop.ui.login;

import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.project.localloop.R;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// Import UI related libraries
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

public class LoginFragment extends Fragment {

    private LoginViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Link to: fragment_login.xml
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get Viewmodel
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        // Receiving inputs
        EditText emailInput = view.findViewById(R.id.login_emailEditText);
        EditText passwordInput = view.findViewById(R.id.login_passwordEditText);
        MaterialButton loginBtn = view.findViewById(R.id.user_loginBtn);
        MaterialButton signUpBtn = view.findViewById(R.id.user_signUpBtn);

        // Adding listeners for input changes
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

        //Button # 1: Attempt log in operations in ViewModel
        loginBtn.setOnClickListener(v -> viewModel.login());
        Log.d("LoginFragment", "Login button clicked");
        // Listening for LoginResult
        viewModel.getLoginResult().observe(getViewLifecycleOwner(), result -> {
            if (result.success) {
                // Go to main page
                if (getActivity() instanceof LoginRegisterActivity) {
                    ((LoginRegisterActivity) getActivity()).onLoginSuccess(result.userName, result.accountType);
                }
            } else {
                // Failed to log in
                Toast.makeText(getContext(), "Failed to log in " + result.error, Toast.LENGTH_SHORT).show();
            }
        });

        // Button #2:  Go to register
        Log.d("LoginFragment", "Sign up button clicked");
        signUpBtn.setOnClickListener(v -> {
            if (getActivity() instanceof LoginRegisterActivity) {
                ((LoginRegisterActivity) getActivity()).showRegisterFragment();
            }
        });
    }
}
