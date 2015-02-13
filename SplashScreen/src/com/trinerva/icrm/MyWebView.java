package com.trinerva.icrm;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Xml.Encoding;
import android.webkit.WebView;

public class MyWebView extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
        String googleDocsUrl = "http://docs.google.com/viewer?url=";
        WebView mWebView=new WebView(MyWebView.this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        String pdfPath = googleDocsUrl + Uri.encode(Uri.parse("file://" + getFilesDir() + "/help.pdf").getPath());
        System.out.println("display path = "+pdfPath);
//        mWebView.getSettings().setPluginsEnabled(true);
        mWebView.loadUrl(pdfPath);
        setContentView(mWebView);
	}

}
