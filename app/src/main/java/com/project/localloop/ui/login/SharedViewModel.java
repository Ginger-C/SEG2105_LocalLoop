package com.project.localloop.ui.login;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// 用于在 LoginFragment 和 RegisterFragment 之间共享数据
public class SharedViewModel extends ViewModel {
    // 监听用户邮箱输入
    private final MutableLiveData<String> email = new MutableLiveData<>();
    // 监听用户密码输入
    private final MutableLiveData<String> password = new MutableLiveData<>();

    public void setEmail(String value) {
        email.setValue(value);
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public void setPassword(String value) {
        password.setValue(value);
    }

    public LiveData<String> getPassword() {
        return password;
    }
}
