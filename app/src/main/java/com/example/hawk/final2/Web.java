package com.example.hawk.final2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class Web extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("url");
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setAppCacheMaxSize( 10 * 1024 * 1024 );
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG);
        webView.loadUrl(message);
    }
}
