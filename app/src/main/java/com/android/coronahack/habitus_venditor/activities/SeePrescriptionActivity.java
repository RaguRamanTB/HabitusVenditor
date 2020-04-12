package com.android.coronahack.habitus_venditor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.android.coronahack.habitus_venditor.R;

public class SeePrescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_prescription);

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("link");

        Toolbar toolbar = findViewById(R.id.see_prescription);
        toolbar.setTitle("Prescription");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        WebView webView = findViewById(R.id.webView);
        webView.loadUrl(url);
    }
}
