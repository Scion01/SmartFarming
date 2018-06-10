package com.example.hauntarl.smartfarming;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewForRef extends AppCompatActivity {

    private String URL;
    private WebView webView;
    private int check=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_for_ref);
        Bundle extras= getIntent().getExtras();
        URL = extras.getString("URL");
        webView = findViewById(R.id.webviewForRef);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(URL);


        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        check=0;
        if (webView.canGoBack()) {
            if(check==1)
                finish();
            if(webView.getUrl().equals(URL))
                check=1;
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
