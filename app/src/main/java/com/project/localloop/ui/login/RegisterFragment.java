package com.project.localloop.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.transition.MaterialSharedAxis;
import com.project.localloop.R;

import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {
    // Account type setting
    private static final long HOST = 1;
    private static final long PARTICIPANT = 2;

    // UI components
    private RegisterViewModel viewModel;
    private MaterialButton cancelBtn;
    private MaterialButton signUpBtn;
    private EditText emailInput;
    private EditText userNameInput;
    private EditText passwordInput;
    private EditText passwordConfirmInput;
    private TextInputLayout signUp_emailLayout;
    private TextInputLayout signUp_passwordLayout;
    private TextInputLayout signUp_usernameLayout;
    private TextInputLayout signUp_pwConfirmLayout;
    private MaterialButtonToggleGroup accountGroup;

    // Only display error message if touched the input field
    private boolean isEmailTouched = false;
    private boolean isUserNameTouched = false;
    private boolean isPasswordTouched = false;
    private boolean isPwdConfirmTouched = false;

    // Pattern for email validation
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

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
        emailInput = view.findViewById(R.id.signUp_emailEditText);
        userNameInput = view.findViewById(R.id.signUp_usernameEditText);
        passwordInput = view.findViewById(R.id.signUp_passwordEditText);
        passwordConfirmInput = view.findViewById(R.id.signUp_pwConfirmEditText);
        signUp_emailLayout = view.findViewById(R.id.signUp_emailLayout);
        signUp_usernameLayout = view.findViewById(R.id.signUp_usernameLayout);
        signUp_passwordLayout = view.findViewById(R.id.signUp_passwordLayout);
        signUp_pwConfirmLayout = view.findViewById(R.id.signUp_pwConfirmLayout);
        accountGroup = view.findViewById(R.id.signUp_accountTypeSelect);
        signUpBtn = view.findViewById(R.id.signUp_submitButton);
        cancelBtn = view.findViewById(R.id.signUp_cancelButton);

        // Disable sign-up by default
        signUpBtn.setEnabled(false);

        // Focus change listeners: mark touched and validate
        emailInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                isEmailTouched = true;
                validateEmail(emailInput.getText().toString().trim());
                updateSignUpButton();
            }
        });
        userNameInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                isUserNameTouched = true;
                validateUsername(userNameInput.getText().toString().trim());
                updateSignUpButton();
            }
        });
        passwordInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                isPasswordTouched = true;
                validatePasswordDesign(passwordInput.getText().toString().trim());
                updateSignUpButton();
            }
        });
        passwordConfirmInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                isPwdConfirmTouched = true;
                validatePasswordIdentical();
                updateSignUpButton();
            }
        });

        // Text change listeners: validate & update button
        // Button only valid when all fields are validated
        TextWatcher watcher = new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isEmailTouched) {
                    validateEmail(emailInput.getText().toString().trim());
                }
                if (isUserNameTouched) {
                    validateUsername(userNameInput.getText().toString().trim());
                }
                if (isPasswordTouched) {
                    validatePasswordDesign(passwordInput.getText().toString().trim());
                }
                if (isPwdConfirmTouched) {
                    validatePasswordIdentical();
                }
                updateSignUpButton();
            }
        };
        emailInput.addTextChangedListener(watcher);
        userNameInput.addTextChangedListener(watcher);
        passwordInput.addTextChangedListener(watcher);
        passwordConfirmInput.addTextChangedListener(watcher);

        accountGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                long type = (checkedId == R.id.btn_host) ? HOST : PARTICIPANT;
                viewModel.setAccountType(type);
            }
            updateSignUpButton();
        });

        // Sign-up button click: collect values
        signUpBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String pwd = passwordInput.getText().toString().trim();
            String name = userNameInput.getText().toString();
            long accountType = (accountGroup.getCheckedButtonId() == R.id.btn_host) ? HOST : PARTICIPANT;
            Log.d("RegisterFragment", "Register clicked");
            viewModel.register(email, pwd, name, accountType);
        });

        // Observe registration result
        viewModel.getRegisterResult().observe(getViewLifecycleOwner(), result -> {
            if (result.success) {
                if (getActivity() instanceof LoginRegisterActivity) {
                    ((LoginRegisterActivity) getActivity())
                            .onLoginSuccess(result.userName, result.accountType);
                }
            } else {
                Toast.makeText(getContext(), "Failed to register: " + result.error,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Cancel button
        cancelBtn.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    // Fragment Transfer Motion
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // on X axis
        setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true));
        setReturnTransition(new MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false));
    }

    // Unified button state update
    private void updateSignUpButton() {
        boolean ok = validateEmail(emailInput.getText().toString().trim())
                && validateUsername(userNameInput.getText().toString().trim())
                && validatePasswordDesign(passwordInput.getText().toString().trim())
                && validatePasswordIdentical()
                && accountGroup.getCheckedButtonId() != View.NO_ID;
        signUpBtn.setEnabled(ok);
    }

    // Password design validation
    private boolean validatePasswordDesign(String pwd) {
        if (pwd.length() < 6) {
            signUp_passwordLayout.setError("Should contain at least 6 characters");
            return false;
        }
        if (pwd.length() > 20) {
            signUp_passwordLayout.setError("Should contain at most 20 characters");
            return false;
        }
        if (!pwd.matches(".*[A-Z].*")) {
            signUp_passwordLayout.setError("Please include at least 1 uppercase character");
            return false;
        }
        if (!pwd.matches(".*[a-z].*")) {
            signUp_passwordLayout.setError("Please include at least 1 lowercase character");
            return false;
        }
        if (!pwd.matches(".*\\d.*")) {
            signUp_passwordLayout.setError("Please include at least 1 numeric character");
            return false;
        }
        signUp_passwordLayout.setError(null);
        return true;
    }

    // Password identical validation
    private boolean validatePasswordIdentical() {
        String pw = passwordInput.getText().toString().trim();
        String pwConfirm = passwordConfirmInput.getText().toString().trim();
        if (!pw.equals(pwConfirm)) {
            signUp_pwConfirmLayout.setError("Passwords do not match.");
            return false;
        }
        signUp_pwConfirmLayout.setError(null);
        return true;
    }

    // Email validation
    private boolean validateEmail(String email) {
        if (email.isEmpty() || !EMAIL_PATTERN.matcher(email).matches()) {
            signUp_emailLayout.setError("Email invalid.");
            return false;
        }
        signUp_emailLayout.setError(null);
        return true;
    }

    // Username validation
    private boolean validateUsername(String username) {
        if (username.isEmpty()) {
            signUp_usernameLayout.setError("UserName required.");
            return false;
        }
        signUp_usernameLayout.setError(null);
        return true;
    }

    // Simplified TextWatcher
    abstract class SimpleTextWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(Editable s) {}
    }

}
