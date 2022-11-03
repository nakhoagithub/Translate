package com.khoab1809593.translate.presenter;

import android.util.Log;

import com.khoab1809593.translate.inf.TranslateInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TranslatePresenter {
    String resp = null;
    String tempSource = "";
    TranslateInterface anInterface;
    SaveHistoryPresenter saveHistoryPresenter;

    public TranslatePresenter(TranslateInterface anInterface, SaveHistoryPresenter saveHistoryPresenter) {
        this.anInterface = anInterface;
        this.saveHistoryPresenter = saveHistoryPresenter;
    }

    public void translate(String langFrom, String langTo, String word){
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() ->{
            //1
            try {
                String url = "https://translate.googleapis.com/translate_a/single?" + "client=gtx&" + "sl=" +
                        langFrom + "&tl=" + langTo + "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestProperty("User-Agent", "Mozilla/5.0");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                resp = response.toString();
            } catch (Exception e) {
                Log.d("zxc", "Lỗi catch");
                e.printStackTrace();
            }

            //2
            StringBuilder content = new StringBuilder();
            if (resp == null) {
                anInterface.fail("NETWORK_ERROR");
            } else {
                try {
                    JSONArray main = new JSONArray(resp);
                    JSONArray total = (JSONArray) main.get(0);
                    for (int i = 0; i < total.length(); i++) {
                        JSONArray currentLine = (JSONArray) total.get(i);
                        content.append(currentLine.get(0).toString());
                    }
                    Log.d("zxc", "Kết quả: " + content);
                    anInterface.success(content.toString());
                    if (!tempSource.contains(content)){
                        saveHistoryPresenter.saveHistory(langFrom, langTo, word, content.toString());
                        tempSource = content.toString();
                    }
                } catch (JSONException e) {
                    anInterface.fail(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        });
    }
}
