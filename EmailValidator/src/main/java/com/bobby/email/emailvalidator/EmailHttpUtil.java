package com.bobby.email.emailvalidator;

import android.text.TextUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class EmailHttpUtil {
    private static OkHttpClient okHttpClient = null;

    public interface HttpRequestCallback {
        void success(String value);

        void fail(String message);

        void complete();
    }

    public static OkHttpClient getInstant(){
        if (okHttpClient == null){

            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30*1000, TimeUnit.SECONDS)
                    .readTimeout(30*1000, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }

    public static void baseGet( String url, final HttpRequestCallback httpRequestCallback) {
        Request request = new Request.Builder().url(url).get().build();
        Call call = getInstant().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                httpRequestCallback.fail(e.getMessage());
                httpRequestCallback.complete();
            }
            @Override
            public void onResponse(Call call, Response response){
                if (response.body() != null) {
                    String value = "";
                    try {
                        value = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (TextUtils.isEmpty(value)) {
                        httpRequestCallback.fail("");
                        httpRequestCallback.complete();
                    } else {
                        httpRequestCallback.success(value);
                        httpRequestCallback.complete();
                    }
                }else{
                    httpRequestCallback.fail("");
                    httpRequestCallback.complete();
                }
            }
        });
    }
}
