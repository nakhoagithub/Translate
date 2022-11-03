package com.khoab1809593.translate.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khoab1809593.translate.R;
import com.khoab1809593.translate.adapter.HistoryAdapter;
import com.khoab1809593.translate.inf.HistoryInterface;
import com.khoab1809593.translate.model.HistoryTranslate;
import com.khoab1809593.translate.presenter.HistoryPresenter;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements HistoryInterface {
    ImageButton btBack, btDeleteHis;
    RecyclerView recyclerHistory;
    ArrayList<HistoryTranslate> arrayHistory;
    HistoryAdapter adapter;
    HistoryPresenter historyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Init();

        btBack.setOnClickListener(v-> onBackPressed());
        historyPresenter.getData(arrayHistory, btDeleteHis);
    }

    private void Init() {
        btBack = findViewById(R.id.bt_back_history);
        btDeleteHis = findViewById(R.id.bt_delete_history);
        recyclerHistory = findViewById(R.id.recycle_history);

        historyPresenter = new HistoryPresenter(this, this);

        arrayHistory = new ArrayList<>();
        adapter = new HistoryAdapter(this, arrayHistory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerHistory.setLayoutManager(layoutManager);
        recyclerHistory.setAdapter(adapter);
        recyclerHistory.setHasFixedSize(true);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void success(String code) {
        if (!code.equals("GET_DATA_SUCCESS")) {
            arrayHistory.clear();
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void fail() {

    }
}