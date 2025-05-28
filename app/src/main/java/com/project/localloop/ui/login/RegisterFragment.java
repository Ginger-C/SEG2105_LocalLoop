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
import com.google.android.material.button.MaterialButton;
import android.widget.EditText;
import androidx.lifecycle.ViewModelProvider;

//Import firebase related libraries
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegisterFragment extends Fragment {

    private RegisterViewModel viewModel;

    //Firebase
    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private MaterialButton signUpBtn;


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

        // 注册输入框示例
        EditText emailInput = view.findViewById(R.id.signUp_emailEditText);
        EditText passwordInput = view.findViewById(R.id.signUp_passwordEditText);

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
    }
}
