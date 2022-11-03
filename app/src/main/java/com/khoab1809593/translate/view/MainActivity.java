package com.khoab1809593.translate.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.khoab1809593.translate.R;
import com.khoab1809593.translate.adapter.LanguageAdapter;
import com.khoab1809593.translate.inf.LoginInterface;
import com.khoab1809593.translate.inf.TranslateInterface;
import com.khoab1809593.translate.model.Language;
import com.khoab1809593.translate.presenter.BetterActivityResult;
import com.khoab1809593.translate.presenter.LoginPresenter;
import com.khoab1809593.translate.presenter.SaveHistoryPresenter;
import com.khoab1809593.translate.presenter.TranslatePresenter;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements TranslateInterface, LoginInterface {

    EditText editContent;
    Button btTranslate;
    TextView content, codeFrom, codeTo, nameUserHeader, mailUserHeader;
    LinearLayout btLangFrom, btLangTo;
    ImageButton btMic, btSpeak, btNavigation, btLogoutHeader;
    DrawerLayout drawerLayout;
    ImageView imageUserHeader;
    NavigationView navigationMain;
    TextToSpeech textToSpeech;

    String from;
    String to;
    boolean listen;

    //translate
    SaveHistoryPresenter saveHistoryPresenter = new SaveHistoryPresenter(this);
    TranslatePresenter translatePresenter = new TranslatePresenter(this, saveHistoryPresenter);

    //login
    FirebaseAuth auth = FirebaseAuth.getInstance();
    LoginPresenter loginPresenter = new LoginPresenter(this, this, auth);

    //save history

    //activity for result
    protected final BetterActivityResult<Intent, ActivityResult> activityLauncher = BetterActivityResult.registerActivityForResult(this);

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        updateUI(user);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();

        navigationMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_info:
                        startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                        break;
                }
                return false;

            }
        });

        codeFrom.setText("Vietnamese");
        from = "vi";
        codeTo.setText("English");
        to = "en";
        checkLangVoice(to);

        btLangFrom.setOnClickListener(v -> showDialogSetLang(true));
        btLangTo.setOnClickListener(v -> showDialogSetLang(false));
        btTranslate.setOnClickListener(v -> {
            String content = editContent.getText().toString();
            if (!content.isEmpty()) {
                translatePresenter.translate(from, to, content);
            } else {
                editContent.setText("");
            }
        });
        btMic.setOnClickListener(v -> speak());
        btSpeak.setOnClickListener(v -> {
            if (listen){
                listen(content.getText().toString());
            } else {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.lang_not_support), Toast.LENGTH_SHORT).show();
            }
        });
        //khởi tạo TextToSpeech kiểm tra ngôn ngữ được hổ trợ hay không


        btNavigation.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        btLogoutHeader.setOnClickListener(v -> openDialogLogout(getResources().getString(R.string.question_logout)));
        //xử lý đăng nhập
        imageUserHeader.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                loginPresenter.signIn(activityLauncher);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    private void checkLangVoice(String code) {
        //khởi tạo textToSpeech
        textToSpeech = new TextToSpeech(this, i -> {
            if (i == TextToSpeech.SUCCESS) {
                //Log.d("zxc", "" + textToSpeech.isLanguageAvailable(Locale.ENGLISH));
                int lang = textToSpeech.setLanguage(new Locale(code));//textToSpeech.setLanguage(Locale.US);
                if (lang == TextToSpeech.LANG_MISSING_DATA || lang == TextToSpeech.LANG_NOT_SUPPORTED) {
                    listen = false;
                } else {
                    listen = true;
                }
            }
        });
    }

    private void listen(String text) {
        if (!text.isEmpty()) {
            textToSpeech.setSpeechRate(0.5f);
            String utteranceId = UUID.randomUUID().toString();
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        }

    }

    private void speak() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hãy nói vài từ");

        startActivityForResult(i, 123);
    }

    private void openDialogLogout(String message) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_question);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.BOTTOM;
            window.setAttributes(windowAttributes);

            TextView testMessage = dialog.findViewById(R.id.text_message_dialog);
            TextView yes = dialog.findViewById(R.id.bt_yes_dialog);
            TextView no = dialog.findViewById(R.id.bt_no_dialog);

            testMessage.setText(message);

            yes.setOnClickListener(v -> {
                loginPresenter.signOut();
                updateUI(FirebaseAuth.getInstance().getCurrentUser());
                dialog.dismiss();
                drawerLayout.closeDrawer(GravityCompat.START);
            });

            no.setOnClickListener(v -> dialog.dismiss());
        }
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 123:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editContent.setText(result.get(0));
                }
                break;
        }
    }

    private void showDialogSetLang(boolean setFrom) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_dialog_language);
        Window window = dialog.getWindow();

        TextView btCancel = dialog.findViewById(R.id.dialog_choose_language_cancel);
        RecyclerView recyclerLanguage = dialog.findViewById(R.id.recycle_language);


        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.BOTTOM;
            window.setAttributes(windowAttributes);

            dialog.setCancelable(false);

            //show list
            ArrayList<Language> listLang = new Language().listLanguage();
            View.OnClickListener onClickListener = view -> {
                int position = recyclerLanguage.getChildAdapterPosition(view);
                if (setFrom) {
                    from = listLang.get(position).getCode();
                    codeFrom.setText(listLang.get(position).getName());
                } else {
                    to = listLang.get(position).getCode();
                    codeTo.setText(listLang.get(position).getName());
                    checkLangVoice(to);
                }


                //Toast.makeText(this, listLang.get(position).getName(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            };

            LanguageAdapter adapter = new LanguageAdapter(this, listLang, onClickListener);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerLanguage.setLayoutManager(layoutManager);
            recyclerLanguage.setAdapter(adapter);
            recyclerLanguage.setHasFixedSize(true);

            //bt cancel
            btCancel.setOnClickListener(v -> dialog.dismiss());
        }
        dialog.show();
    }

    private void Init() {
        //cấu hình login
        loginPresenter.conf();

        editContent = findViewById(R.id.edit_content);
        btTranslate = findViewById(R.id.bt_translate);
        content = findViewById(R.id.translate_content);
        btLangFrom = findViewById(R.id.lang_from);
        btLangTo = findViewById(R.id.lang_to);

        codeFrom = findViewById(R.id.code_from);
        codeTo = findViewById(R.id.code_to);
        btMic = findViewById(R.id.voice_to_text);
        btSpeak = findViewById(R.id.text_to_speak);

        drawerLayout = findViewById(R.id.drawer_main);
        btNavigation = findViewById(R.id.bt_nav);
        navigationMain = findViewById(R.id.nav_main);

        //header navigation
        View headerNav = navigationMain.getHeaderView(0);
        imageUserHeader = headerNav.findViewById(R.id.image_user_header);
        nameUserHeader = headerNav.findViewById(R.id.name_user_header);
        mailUserHeader = headerNav.findViewById(R.id.mail_user_header);
        btLogoutHeader = headerNav.findViewById(R.id.bt_logout_header);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            btLogoutHeader.setVisibility(View.VISIBLE);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                nameUserHeader.setText(personName);
                mailUserHeader.setText(personEmail);

                //load ảnh đại diện
                Glide.with(this).load(personPhoto).centerCrop().into(imageUserHeader);
                //push dữ liệu đầu tiền vào database
            }
        } else {
            btLogoutHeader.setVisibility(View.GONE);
            nameUserHeader.setText(getResources().getString(R.string.login));
            mailUserHeader.setText(getResources().getString(R.string.choose_image_login));
            imageUserHeader.setImageResource(R.drawable.ic_user_profile);
        }
    }

    @Override
    public void success(String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                content.setText(result);
            }
        });

    }

    @Override
    public void fail(String fail) {
        switch (fail) {
            case "NETWORK_ERROR":
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(this, getResources().getString(R.string.not_internet), Toast.LENGTH_SHORT).show());
            case "xyz":
                break;
        }
    }

    @Override
    public void loginSuccess(FirebaseUser user) {
        updateUI(user);
    }

    @Override
    public void loginFail() {

    }
}