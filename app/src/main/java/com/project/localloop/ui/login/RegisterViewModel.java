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
public class RegisterViewModel extends ViewModel {
    private final MutableLiveData<String> email     = new MutableLiveData<>();
    private final MutableLiveData<String> userName  = new MutableLiveData<>();
    private final MutableLiveData<String> password  = new MutableLiveData<>();
    private final MutableLiveData<String> repeatPassword  = new MutableLiveData<>();
    private final MutableLiveData<AuthResult> registerResult = new MutableLiveData<>();

    public void setEmail(String email) { this.email.setValue(email); }
    public void setPassword(String pwd) { this.password.setValue(pwd); }
    public void setUserName(String name) { this.userName.setValue(name); }

    public LiveData<String> getEmail() { return email; }
    public LiveData<String> getPassword() { return password; }
    public LiveData<String> getUserName() { return userName; }
    public LiveData<AuthResult> getRegisterResult() { return registerResult; }

    public void register() {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email.getValue(), password.getValue())
                .addOnSuccessListener(result -> {
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Map<String, Object> userDoc = new HashMap<>();
                    userDoc.put("userId", uid);
                    userDoc.put("userName", userName.getValue());
                    userDoc.put("email", email.getValue());
                    userDoc.put("accountType", 1);
                    userDoc.put("isSuspended", false);

                    FirebaseFirestore.getInstance().collection("users")
                            .document(uid)
                            .set(userDoc)
                            .addOnSuccessListener(d -> {
                                registerResult.setValue(new AuthResult(true, null));
                            })
                            .addOnFailureListener(e -> {
                                registerResult.setValue(new AuthResult(false, e.getMessage()));
                            });
                })
                .addOnFailureListener(e -> {
                    registerResult.setValue(new AuthResult(false, e.getMessage()));
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