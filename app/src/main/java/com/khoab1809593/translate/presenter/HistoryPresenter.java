package com.khoab1809593.translate.presenter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khoab1809593.translate.R;
import com.khoab1809593.translate.inf.HistoryInterface;
import com.khoab1809593.translate.model.HistoryTranslate;

import java.util.ArrayList;

public class HistoryPresenter {
    Context context;
    HistoryInterface anInterface;

    public HistoryPresenter(Context context, HistoryInterface anInterface) {
        this.context = context;
        this.anInterface = anInterface;
    }

    public void getData(ArrayList<HistoryTranslate> arrayHistory, ImageButton btDeleteHis){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);
        if (acct != null) {
            String id = acct.getId();
            getHistory(id, arrayHistory, btDeleteHis);
        } else {
            btDeleteHis.setVisibility(View.GONE);
            Toast.makeText(context, context.getResources().getString(R.string.login_view_history), Toast.LENGTH_SHORT).show();
        }
    }

    private void getHistory(String id, ArrayList<HistoryTranslate> arrayHistory, ImageButton btDeleteHis){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(id).child("history");
        database.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()){
                btDeleteHis.setVisibility(View.VISIBLE);
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    HistoryTranslate historyTranslate = snapshot.getValue(HistoryTranslate.class);
                    if(historyTranslate != null){
                        HistoryTranslate historyTranslate1 = new HistoryTranslate(
                                historyTranslate.getFrom(),
                                historyTranslate.getTo(),
                                historyTranslate.getSource(),
                                historyTranslate.getContent(),
                                snapshot.getKey()
                        );
                        arrayHistory.add(historyTranslate1);
                        anInterface.success("GET_DATA_SUCCESS");
                        Log.d("zxc", "data: " + snapshot.getKey());
                    }
                }

                btDeleteHis.setOnClickListener(v -> showDialogDeleteHistory(id, context.getResources().getString(R.string.question_delete_history)));

            } else {
                btDeleteHis.setVisibility(View.GONE);
            }

        });
    }

    private void showDialogDeleteHistory(String id, String message){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_question);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.BOTTOM;
            window.setAttributes(windowAttributes);

            dialog.setCancelable(false);

            TextView testMessage = dialog.findViewById(R.id.text_message_dialog);
            TextView yes = dialog.findViewById(R.id.bt_yes_dialog);
            TextView no = dialog.findViewById(R.id.bt_no_dialog);

            testMessage.setText(message);

            yes.setOnClickListener(v -> {
                deleteHistory(dialog, id);
            });

            no.setOnClickListener(v -> dialog.dismiss());
        }
        dialog.show();
    }

    private void deleteHistory(Dialog dialog, String id){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(id).child("history");
        database.setValue(null);
        dialog.dismiss();
        anInterface.success("DELETE_SUCCESS");
    }
}
