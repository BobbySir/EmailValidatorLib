package com.bobby.email.emailvalidator;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.appsflyer.AppsFlyerLib;
import com.appsflyer.attribution.AppsFlyerRequestListener;

import java.util.HashMap;
import java.util.Map;

public class EmailAfUtils {
    /**
     * AF应用内事件
     */
    public static void trackEvent(Context context, String eventName, Object object) {
        Map<String, Object> eventValues = new HashMap<>();
        eventValues.put(eventName,object);
        AppsFlyerLib.getInstance().logEvent(context, eventName, eventValues, new AppsFlyerRequestListener() {
            @Override
            public void onSuccess() {
                Log.e("out", "trackEvent Success:" + eventName);
            }

            @Override
            public void onError(int i, @NonNull String s) {
                Log.e("out", "trackEvent Error:" + eventName);
            }
        });
    }
}



