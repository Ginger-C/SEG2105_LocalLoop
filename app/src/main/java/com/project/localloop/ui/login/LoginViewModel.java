package com.project.localloop.ui.login;

//Import UI related libraries
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

//Import firebase related libraries
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

// Used for sharing data between fragments.
// DB functionality added heere.

public class LoginViewModel extends ViewModel {
    //==================== UI LISTENER ==================
    // listen for user input:email.Mutable.
    private final MutableLiveData<String> email    = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final MutableLiveData<AuthResult> loginResult = new MutableLiveData<>();

    //==================== FIREBASE ==================
    private FirebaseAuth auth;
    private FirebaseFirestore database;

    public void setEmail(String email) { this.email.setValue(email); }
    public void setPassword(String pwd) { this.password.setValue(pwd); }


    public LiveData<String> getEmail() { return email; }
    public LiveData<String> getPassword() { return password; }
    public LiveData<AuthResult> getLoginResult() { return loginResult; }

    public void login() {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email.getValue(), password.getValue())
                .addOnSuccessListener(authResult -> {
                    loginResult.setValue(new AuthResult(true, null));
                })
                .addOnFailureListener(e -> {
                    loginResult.setValue(new AuthResult(false, e.getMessage()));
                });
    }

    public static class AuthResult {
        public boolean success;
        public String errorMessage;
        public AuthResult(boolean success, String errorMessage) {
            this.success = success;
            this.errorMessage = errorMessage;
        }
    }
}