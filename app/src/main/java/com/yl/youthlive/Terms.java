package com.yl.youthlive;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class Terms extends AppCompatActivity {

    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        web = (WebView)findViewById(R.id.web);

        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl("file:///android_asset/youthlive.htm");

    }
}
