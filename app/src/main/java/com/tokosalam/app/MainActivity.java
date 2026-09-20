package com.tokosalam.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.webkit.WebResourceRequest;
import android.os.Build;

public class MainActivity extends Activity {

    private WebView webView;

    // NOMOR / URL WHATSAPP TOKO SALAM
    private static final String WHATSAPP_URL =
            "https://wa.me/111111111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    String url) {

                return bukaLink(url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    WebResourceRequest request) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    return bukaLink(request.getUrl().toString());
                }

                return false;
            }
        });

        // GANTI DENGAN URL WEBSITE/APK PEMBELI ANDA
        webView.loadUrl("https://meme-project-cab7f.web.app");
    }

    private boolean bukaLink(String url) {

        if (url == null) {
            return false;
        }

        // WhatsApp
        if (url.startsWith("https://wa.me/") ||
            url.startsWith("https://api.whatsapp.com/") ||
            url.startsWith("whatsapp://")) {

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;

            } catch (Exception e) {
                return false;
            }
        }

        // Link telepon
        if (url.startsWith("tel:")) {

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;

            } catch (Exception e) {
                return false;
            }
        }

        // Link SMS
        if (url.startsWith("sms:")) {

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;

            } catch (Exception e) {
                return false;
            }
        }

        // Link biasa tetap dibuka di WebView
        return false;
    }

    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
