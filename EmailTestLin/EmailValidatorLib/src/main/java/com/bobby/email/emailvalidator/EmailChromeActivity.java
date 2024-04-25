package com.bobby.email.emailvalidator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

public class EmailChromeActivity extends AppCompatActivity{
    private WebView webView;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private final String webUrlLike = "webUrl";

    private static EmailPoiListener mEmailPoiListener;
    public void setPointListener(EmailPoiListener emailPoiListener){
        if(emailPoiListener != null){
            mEmailPoiListener = emailPoiListener;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_chrome);

        webView = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.web_progress_bar);
        initWebLoad();
    }

    private void initWebLoad() {
        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        Intent intent = getIntent();
        if (intent != null) {
            initWebView();
            initListener();
            String mUrl = intent.getStringExtra(webUrlLike);
            if (!TextUtils.isEmpty(mUrl)) {
                loadUrl(mUrl);
            }
        }
    }

    private void loadUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("http:") || url.startsWith("https:")) {
                webView.loadUrl(url);
            } else {
                webView.loadData(url, "text/html; charset=UTF-8", null);
            }
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void initWebView() {
        WebSettings settings = webView.getSettings();
        webView.getSettings().setDefaultTextEncodingName("UTF-8");//设置默认为utf-8
        settings.setJavaScriptEnabled(true);     //支持js脚本
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); //设置缓存
        settings.setDomStorageEnabled(true);    //开启DomStorage缓存
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportZoom(true); //是否支持缩放
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setSupportMultipleWindows(true);
        settings.setAllowFileAccess(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setLoadWithOverviewMode(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.addJavascriptInterface(this , "jsBridge");

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                EmailIntentHelper.openInChrome(EmailChromeActivity.this, url);
            }
        });
    }

    private void initListener() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (progressBar != null) {
                    progressBar.setProgress(newProgress);
                    if (newProgress == 100) {
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView newWebView = new WebView(view.getContext());
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();

                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        if (sharedPreferences.getString("h5Type", "0").equals("1")) {
                            Intent intent = new Intent(EmailChromeActivity.this, EmailChromeActivity.class);
                            intent.putExtra(webUrlLike, url);
                            startActivity(intent);
                        } else {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                            browserIntent.setData(Uri.parse(url));
                            startActivity(browserIntent);
                        }
                        return true;
                    }
                });

                return true;
            }
        });


        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("eee", " url2  = " + url);
                WebView.HitTestResult hitTestResult = view.getHitTestResult();
                if (!TextUtils.isEmpty(url) && hitTestResult == null) {
                    view.loadUrl(url);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            //加载错误的时候会回调，在其中可做错误处理，比如再请求加载一次，或者提示404的错误页面
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
    }


    boolean isGoBack = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            isGoBack = true;
            webView.goBack();
            return true;
        } else {
            EmailChromeActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @JavascriptInterface
    public void postMessage(String eventName, String params) {
        if(mEmailPoiListener != null) mEmailPoiListener.PointResult(eventName, params);
        JSONObject json = null;
        try {
            json = new JSONObject(params);

            if (eventName.equals("openWindow") && json.has("url")) {
                EmailIntentHelper.openInChrome(EmailChromeActivity.this, json.getString("url"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("out", "eventName:" + eventName + "   params:" + params);
    }

    public static void entry(Context context, String url, EmailPoiListener emailPoiListener) {
        if(emailPoiListener != null){
            mEmailPoiListener = emailPoiListener;
        }
        Intent intent = new Intent(context, EmailChromeActivity.class);
        intent.putExtra("webUrl", url);
        context.startActivity(intent);
    }
}