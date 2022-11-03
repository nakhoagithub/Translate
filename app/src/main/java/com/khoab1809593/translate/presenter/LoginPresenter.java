package com.khoab1809593.translate.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResult;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khoab1809593.translate.R;
import com.khoab1809593.translate.inf.LoginInterface;
import com.khoab1809593.translate.model.User;

public class LoginPresenter {

    Context context;
    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;
    LoginInterface anInterface;


    public LoginPresenter(LoginInterface anInterface, Context context, FirebaseAuth auth) {
        this.anInterface = anInterface;
        this.context = context;
        this.auth = auth;
    }

    public FirebaseUser getUser() {
        return auth.getCurrentUser();
    }

    public void conf() {
        //cấu hình google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getResources().getString(R.string.client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public void signIn(BetterActivityResult<Intent, ActivityResult> activityLauncher) {
        Intent signInIntent = googleSignInClient.getSignInIntent();

        activityLauncher.launch(signInIntent, result -> {
            Intent data = result.getData();
            if (data != null)
                login(data);
        });
    }

    private void login(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            //Log.d("zxc", "firebaseAuthWithGoogle:" + account.getId());
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            Log.d("zxc", "Google sign in failed", e);
            anInterface.loginFail();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d("zxc", "signInWithCredential:success");
                        FirebaseUser user = auth.getCurrentUser();
                        anInterface.loginSuccess(user);

                        //lấy id và email người dùng
                        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);
                        if (acct != null) {
                            String personEmail = acct.getEmail();
                            String personId = acct.getId();
                            //push dữ liệu đầu tiền vào database
                            pushDataUserFirst(personId, personEmail);
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        //Log.d("zxc", "signInWithCredential:failure", task.getException());
                        anInterface.loginFail();
                    }
                });
    }

    public void pushDataUserFirst(String id, String email) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(id);
        database.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.getValue() == null){
                //push dữ liệu đầu tiên vào server
                User user = new User(email, id);
                database.setValue(user);
            }
        });
    }

    public void signOut() {
        googleSignInClient.signOut();
        FirebaseAuth.getInstance().signOut();
    }
}
