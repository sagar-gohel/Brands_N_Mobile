package com.shagii.brandsnmobile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Toolbar toolbar;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        /*this Way we Test Our Event passed by Post Adapter is working or not by generating post
        Toast.makeText(this, getIntent().getStringExtra("url"), Toast.LENGTH_SHORT).show();*/
        //Here we are Setting Our Toolbar, ProgressBar & DetailView
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        webView = findViewById(R.id.detailView);

        setSupportActionBar(toolbar);
        //We set Web View invisibal till the page Load like and also enable JavaScript of WebView
        webView.setVisibility(View.INVISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        //To Handel WebView events page Load, Finesh we need to create setCromeClients as
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Toast.makeText(DetailActivity.this, "Page Started Loafing", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //After complete loading the webview need to visible and porgressbar gone
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                Toast.makeText(DetailActivity.this, "Page Loaded", Toast.LENGTH_SHORT).show();
                //This way the extra toolbar which seen in app that will be hided using Java script below  we can do more improvise in view by this way
                String javaScript = "javascript: (function() {var a=document.getElementsByTagName('header'); a[0].hidden='true'; a=document.getElementsClassName('page_body'); a[0].style.padding='0px'})()";
                webView.loadUrl(javaScript);
            }
        });
        //Here we Pass Url in the Web View that need to run
        webView.loadUrl(getIntent().getStringExtra("url"));


    }
}