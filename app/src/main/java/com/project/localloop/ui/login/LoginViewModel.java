package com.project.localloop.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.project.localloop.repository.UserRepository;

public class LoginViewModel extends ViewModel {
    // Firebase encapsulated
    private final UserRepository repo = UserRepository.getInstance();
    // Mutable Liva Data: listen for changes
    private final MutableLiveData<String> email    = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    // Login Result. Inner class declared at bottom.
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    // Setter and Getter
    public void setEmail(String email) { this.email.setValue(email); }
    public void setPassword(String password) { this.password.setValue(password); }

    public LiveData<String> getEmail() { return email; }
    public LiveData<String> getPassword() { return password; }
    public LiveData<LoginResult> getLoginResult() { return loginResult; }

    // Login
    public void login() {
        String emailStr = email.getValue();
        String pwdStr   = password.getValue();
        // 1. Case empty
        if (emailStr == null || pwdStr == null) {
            loginResult.setValue(new LoginResult(false, "Please enter email and password",null, -1));
            return;
        }
        // 2. Case succeed: login and load user data
        repo.loginAndLoadUserData(emailStr, pwdStr, new UserRepository.UserDataCallback() {
            @Override
            public void onSuccess(String userName, int accountType) {
                loginResult.setValue(new LoginResult(true, null, userName, accountType));
            }
        // 3. Case failed/invalid
            @Override
            public void onError(String error) {
                loginResult.setValue(new LoginResult(false, error, null, -1));
            }
        });
    }

    public static class LoginResult {
        public final boolean success;
        public final String error;
        public final String userName;
        public final int accountType;

        public LoginResult(boolean success, String error, String userName, int accountType) {
            this.success = success;
            this.error = error;
            this.userName = userName;
            this.accountType = accountType;
        }
    }
}
