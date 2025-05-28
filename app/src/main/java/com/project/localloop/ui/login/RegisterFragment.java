package com.project.localloop.ui.login;

import android.os.Bundle;
import com.project.localloop.R;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//Import UI related libraries
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.button.MaterialButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

public class RegisterFragment extends Fragment {

    private RegisterViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);

        // Receiving inputs
        EditText emailInput = view.findViewById(R.id.signUp_emailEditText);
        EditText userNameInput = view.findViewById(R.id.signUp_usernameEditText);
        EditText passwordInput = view.findViewById(R.id.signUp_passwordEditText);
        MaterialButton signUpBtn = view.findViewById(R.id.signUp_submitButton);
        MaterialButton cancelBtn = view.findViewById(R.id.signUp_cancelButton);

        // Adding listeners for input changes
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setEmail(s.toString());
            }
        });
        userNameInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setUserName(s.toString());
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
        signUpBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim(); //in case accident \n or \t
            String pwd = passwordInput.getText().toString().trim();
            String name = userNameInput.getText().toString().trim();
            int accountType = 1; // TODO: get from spinner

            viewModel.register(email, pwd, name, accountType);
        });
        Log.d("RegisterFragment", "Register clicked");
        // Listening for LoginResult
        viewModel.getRegisterResult().observe(getViewLifecycleOwner(), result -> {
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

        // Button #2:  Go back
        Log.d("RegisterFragment", "Cancelled");
        cancelBtn.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }
}
