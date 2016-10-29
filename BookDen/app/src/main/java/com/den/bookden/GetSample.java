package com.den.bookden;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;

import com.den.bookden.app.AppConfig;

/**
 * Created by Shubhi on 5/13/2016.
 */
public class GetSample extends AppCompatActivity {

    private static String url = AppConfig.URL_START;
    private static String link;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getsample);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             link = extras.getString("link");
            url = AppConfig.URL_START;
            url += link;
            //this.setTitle(category + " " + type);
        }
       // String doc="<iframe src='https://docs.google.com/gview?embedded=true&url=http://uniqueenterprises.in/amisha/csapp/uploads/20160516105329am.pdf' width='100%' height='100%' style='border: none;'></iframe>";
        WebView webview = (WebView) findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setAllowFileAccess(true);
        //String pdf = "http://www.adobe.com/devnet/acrobat/pdfs/pdf_open_parameters.pdf";
        webview.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);
        //Toast.makeText(GetSample.this,link,Toast.LENGTH_LONG).show();

    }
}
