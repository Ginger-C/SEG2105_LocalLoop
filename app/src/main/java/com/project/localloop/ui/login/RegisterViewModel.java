package com.project.localloop.ui.login;

//Import UI related libraries
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
//Import firebase related libraries
import com.project.localloop.repository.UserRepository; //firebase encapsulated
public class RegisterViewModel extends ViewModel {
    // Firebase encapsulated
    private final UserRepository repo = UserRepository.getInstance();
    // Mutable Liva Data: listen for changes
    private final MutableLiveData<String> email     = new MutableLiveData<>();
    private final MutableLiveData<String> userName  = new MutableLiveData<>();
    private final MutableLiveData<String> password  = new MutableLiveData<>();
    private final MutableLiveData<String> confirmPassword  = new MutableLiveData<>();
    // Register Result. Inner class declared at bottom.
    private final MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    // Setter and Getter
    public void setEmail(String email) { this.email.setValue(email); }
    public void setPassword(String pwd) { this.password.setValue(pwd); }
    public void setUserName(String name) { this.userName.setValue(name); }

    public LiveData<String> getEmail() { return email; }
    public LiveData<String> getPassword() { return password; }
    public LiveData<String> getUserName() { return userName; }
    public LiveData<RegisterResult> getRegisterResult() { return registerResult; }
    public void register(String email, String password,String username,int accountType) {
        repo.registerUser(email, password,username,accountType, new UserRepository.Callback() {
            @Override
            public void onSuccess() {
                registerResult.setValue(new RegisterViewModel.RegisterResult(true, null,username,accountType));
            }

            @Override
            public void onError(String error) {
                registerResult.setValue(new RegisterViewModel.RegisterResult(false, error,null,-1));
            }
        });
    }

    public static class RegisterResult {
        public final boolean success;
        public final String error;
        public final String userName;
        public final int accountType;

        public RegisterResult(boolean success, String error,String userName, int accountType) {
            this.success = success;
            this.error = error;
            this.userName = userName;
            this.accountType = accountType;
        }
    }

}