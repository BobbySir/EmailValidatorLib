package com.bobby.email.emailvalidator;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
    public static void basePost(String posUrl, HashMap<String, String> requestBody2, final HttpRequestCallback httpRequestCallback) {
        Request.Builder builder = new Request.Builder();
        builder.url(posUrl);
        builder.addHeader("Content-Type","application/json");
//        FormBody.Builder requestBody1 = new FormBody.Builder();
//        for (Map.Entry<String, String> entry : requestBody2.entrySet()) {
//            requestBody1.add(entry.getKey(), entry.getValue());
//        }
        RequestBody requestBody =null;
        JSONObject json = new JSONObject(requestBody2);
        String str=   json.toString();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        requestBody = RequestBody.create(JSON, str);
        builder.post(requestBody);
        Call call = getInstant().newCall(builder.build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                httpRequestCallback.fail(e.getMessage());
                httpRequestCallback.complete();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    String value = response.body().string();
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
