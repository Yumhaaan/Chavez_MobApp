package com.example.asynctaskasynctaskloader;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class FetchBook extends AsyncTask<String, Void, String> {
    private WeakReference<TextView> titleText;
    private WeakReference<TextView> authorText;

    public FetchBook(TextView title, TextView author) {
        this.titleText = new WeakReference<>(title);
        this.authorText = new WeakReference<>(author);
    }

    @Override
    protected String doInBackground(String... params) {
        return NetworkUtils.getBookInfo(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            String title = null;
            String authors = null;

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                try {
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getJSONArray("authors").getString(0);
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (title != null && authors != null) {
                titleText.get().setText(title);
                authorText.get().setText(authors);
            } else {
                titleText.get().setText(R.string.no_results);
                authorText.get().setText("");
            }
        } catch (Exception e) {
            titleText.get().setText(R.string.no_results);
            authorText.get().setText("");
        }
    }
}
