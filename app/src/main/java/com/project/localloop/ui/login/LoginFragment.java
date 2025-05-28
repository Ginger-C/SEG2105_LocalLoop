package com.project.localloop.ui.login;

import android.os.Bundle;

import com.project.localloop.R;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//Import UI related libraries
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.lifecycle.ViewModelProvider;

//Import firebase related libraries
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginFragment extends Fragment {

    private LoginViewModel viewModel;

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

        // Get Viewmodel
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        // Receiving inputs
        EditText emailInput = view.findViewById(R.id.login_emailEditText);
        EditText passwordInput = view.findViewById(R.id.login_passwordEditText);

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

        // Log in failed, go to register
        Button signUpButton = view.findViewById(R.id.user_signUpBtn);
        signUpButton.setOnClickListener(v -> {
            if (getActivity() instanceof LoginRegisterActivity) {
                ((LoginRegisterActivity) getActivity()).showRegisterFragment();
            }
        });
    }
}
