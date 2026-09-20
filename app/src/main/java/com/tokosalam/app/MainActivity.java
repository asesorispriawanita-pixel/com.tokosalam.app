package com.tokosalam.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private WebView webView;

    private ValueCallback<Uri[]> filePathCallback;

    private static final int FILE_CHOOSER_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Membuat WebView
        webView = new WebView(this);
        setContentView(webView);

        // Pengaturan WebView
        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        // Izinkan akses file
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        // WebView biasa
        webView.setWebViewClient(new WebViewClient());

        // File chooser Android
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onShowFileChooser(
                    WebView webView,
                    ValueCallback<Uri[]> filePathCallback,
                    FileChooserParams fileChooserParams) {

                // Batalkan callback sebelumnya jika masih ada
                if (MainActivity.this.filePathCallback != null) {
                    MainActivity.this.filePathCallback.onReceiveValue(null);
                }

                MainActivity.this.filePathCallback =
                        filePathCallback;

                // Buat pemilih file Android
                Intent intent =
                        fileChooserParams.createIntent();

                try {

                    startActivityForResult(
                            intent,
                            FILE_CHOOSER_REQUEST
                    );

                } catch (Exception e) {

                    MainActivity.this.filePathCallback = null;

                    return false;
                }

                return true;
            }
        });

        // Buka halaman toko
        webView.loadUrl(
                "file:///android_asset/index.html"
        );
    }


    // Hasil dari Galeri / File Manager
    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (requestCode == FILE_CHOOSER_REQUEST) {

            if (filePathCallback == null) {
                return;
            }

            Uri[] results = null;

            // Jika user memilih foto
            if (resultCode == RESULT_OK && data != null) {

                Uri uri = data.getData();

                if (uri != null) {

                    results =
                            new Uri[]{uri};
                }
            }

            // Kirim foto kembali ke WebView
            filePathCallback.onReceiveValue(results);

            filePathCallback = null;
        }
    }


    // Tombol kembali HP
    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {

            webView.goBack();

        } else {

            super.onBackPressed();
        }
    }
}
