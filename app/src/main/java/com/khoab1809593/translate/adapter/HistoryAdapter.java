package com.khoab1809593.translate.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khoab1809593.translate.R;
import com.khoab1809593.translate.model.HistoryTranslate;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private final Context context;
    private final List<HistoryTranslate> array;

    public HistoryAdapter(Context context, List<HistoryTranslate> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryTranslate historyTranslate = array.get(position);
        if(historyTranslate != null){
            holder.from.setText(historyTranslate.getFrom() + ": " + historyTranslate.getSource());
            holder.to.setText(historyTranslate.getTo() + ": " + historyTranslate.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return array != null ? array.size() : 0;
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder{

        TextView from;
        TextView to;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.from_history_item);
            to = itemView.findViewById(R.id.to_history_item);
        }
    }
}
