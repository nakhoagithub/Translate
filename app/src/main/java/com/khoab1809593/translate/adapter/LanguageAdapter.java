package com.khoab1809593.translate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khoab1809593.translate.R;
import com.khoab1809593.translate.model.Language;

import java.util.ArrayList;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {
    private final Context context;
    private final ArrayList<Language> array;
    private final View.OnClickListener onClickListener;

    public LanguageAdapter(Context context, ArrayList<Language> array, View.OnClickListener onClickListener) {
        this.context = context;
        this.array = array;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_language, parent, false);
        v.setOnClickListener(onClickListener);
        return new LanguageViewHolder(v, array);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        Language language = array.get(position);
        if (language != null){
            holder.lang.setText(language.getName());
        }
    }

    @Override
    public int getItemCount() {
        return array != null ? array.size() : 0;
    }

    static class LanguageViewHolder extends RecyclerView.ViewHolder{
        TextView lang;
        ArrayList<Language> array;
        public LanguageViewHolder(@NonNull View itemView, ArrayList<Language> array) {
            super(itemView);
            this.array = array;
            lang = itemView.findViewById(R.id.text_language);
        }
    }
}
