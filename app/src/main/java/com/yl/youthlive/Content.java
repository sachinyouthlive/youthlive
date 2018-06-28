package com.yl.youthlive;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

public class Content extends AppCompatActivity {

    Toolbar toolbar;
    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Toast.makeText(this, "content.java", Toast.LENGTH_SHORT).show();

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        web = (WebView)findViewById(R.id.web);

        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl("file:///android_asset/youthlive.htm");

        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);


        String title = getIntent().getStringExtra("title");

        toolbar.setTitle(title);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
