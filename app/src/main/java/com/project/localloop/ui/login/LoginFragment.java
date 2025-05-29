package com.project.localloop.ui.login;

import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.transition.MaterialSharedAxis;
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
    private MaterialButton loginBtn;
    private MaterialButton signUpBtn;
    private EditText emailInput;
    private EditText passwordInput;
    private TextInputLayout passwordLayout;
    private TextInputLayout emailLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Link to: fragment_login.xml
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    // Fragment Transfer Motion
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // on X axis
        setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true));
        setReturnTransition(new MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get Viewmodel
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        // Assign UI elements for receiving inputs.
        emailInput = view.findViewById(R.id.login_emailEditText);
        emailLayout = view.findViewById(R.id.login_emailLayout);
        passwordInput = view.findViewById(R.id.login_passwordEditText);
        passwordLayout = view.findViewById(R.id.login_passwordLayout);
        loginBtn  = view.findViewById(R.id.user_loginBtn);
        signUpBtn = view.findViewById(R.id.user_signUpBtn);

        // Disable login by default
        loginBtn.setEnabled(false);

        // Adding listeners for input changes
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String emailSTR = s.toString().trim();
                viewModel.setEmail(s.toString());
                if (emailSTR.isEmpty()) {
                    emailLayout.setError("Please enter username.");
                }
                else
                {
                    emailLayout.setError(null);
                    loginBtn.setEnabled(true);
                }
            }
        });
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pwd = s.toString().trim();
                viewModel.setPassword(pwd);
                if (pwd.length() < 6) {
                    passwordLayout.setError("Password must be at least 6 characters.");
                }
                else if(pwd.length() > 20)
                {
                    passwordLayout.setError("Password must be at most 20 characters.");
                }
                else {
                    passwordLayout.setError(null);
                    loginBtn.setEnabled(true);
                }
            }
        });

        //Button # 1: Attempt log in operations in ViewModel
        loginBtn.setOnClickListener(v ->
            {
            Log.d("LoginFragment", "Login button clicked");
            viewModel.login();
            });

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
        signUpBtn.setOnClickListener(v -> {
            if (getActivity() instanceof LoginRegisterActivity) {
                Log.d("LoginFragment", "Sign up button clicked");
                ((LoginRegisterActivity) getActivity()).showRegisterFragment();
            }
        });
    }
}
