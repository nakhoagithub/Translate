package com.khoab1809593.translate.inf;

import com.google.firebase.auth.FirebaseUser;

public interface LoginInterface {
    void loginSuccess(FirebaseUser user);
    void loginFail();
}
