package com.bobby.email.emailvalidator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import kotlin.jvm.JvmStatic;

public class EmailIntentHelper {
    @JvmStatic
    public static void openInChrome(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setPackage("com.android.chrome");
            context.startActivity(intent);
        } catch (Exception e) {
            // no install Chrome
            goToHttp(context, url);

            e.printStackTrace();
        }
    }

    private static void goToHttp(Context context, String url) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
