package com.khoab1809593.translate.presenter;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khoab1809593.translate.model.HistoryTranslate;

public class SaveHistoryPresenter {
    Context context;

    public SaveHistoryPresenter(Context context) {
        this.context = context;
    }

    public void saveHistory(String from, String to, String source, String content) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            //lấy id và email người dùng
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);
            if (acct != null) {
                String id = acct.getId();
                save(id, from, to, source, content);
            }
        }
    }

    private void save(String id, String from, String to, String source, String content){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(id);
        HistoryTranslate historyTranslate = new HistoryTranslate(from, to, source, content);
        database.child("history").push().setValue(historyTranslate);
    }
}
