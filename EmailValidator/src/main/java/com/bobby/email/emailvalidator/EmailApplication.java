package com.bobby.email.emailvalidator;

import android.app.Application;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import java.util.Map;

public class EmailApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initAppsFlyer();
    }

    private void initAppsFlyer(){
        String key = "98sMEYEMxW7JvJXVb466wP";
        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {

            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {
                for (String attrName : conversionData.keySet()) {
                    Log.e( "out","初始化AppsFlyer成功 attribute: " + attrName + " = " + conversionData.get(attrName));
                }
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.e( "out","初始化AppsFlyer失败 error getting conversion data: " + errorMessage);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> conversionData) {
                for (String attrName : conversionData.keySet()) {
                    Log.e( "out","attribute: " + attrName + " = " + conversionData.get(attrName));
                }
            }

            @Override
            public void onAttributionFailure(String s) {
                Log.e( "out","error onAttributionFailure : " + s);
            }
        };
        AppsFlyerLib appsFlyerLib = AppsFlyerLib.getInstance();
        appsFlyerLib.init(key, conversionListener, this);
        //启动调试日志
        appsFlyerLib.setDebugLog(true);
        appsFlyerLib.start(this);
    }
}
