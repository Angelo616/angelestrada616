package com.example.reportell;

import android.app.DownloadManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import android.Manifest;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class MainActivity extends AppCompatActivity {

    private WebView wView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String[] totallyLegalPermissions;
    private Log log;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_main);

        // Inicializar permisos
        totallyLegalPermissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.POST_NOTIFICATIONS
        };

        // Vincular SwipeRefreshLayout y WebView
        swipeRefreshLayout = findViewById(R.id.swipeContainer);
        wView = findViewById(R.id.webview);

        // Configurar WebView
        wView.getSettings().setJavaScriptEnabled(true);
        wView.getSettings().setLoadWithOverviewMode(true);
        wView.getSettings().setUseWideViewPort(true);
        wView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        wView.setScrollbarFadingEnabled(true);
        wView.getSettings().setDomStorageEnabled(true); // Habilitar almacenamiento local

        // Soporte para descargas
        wView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "archivo_descargado");
            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            if (downloadManager != null) {
                downloadManager.enqueue(request);
                Toast.makeText(MainActivity.this, "Descarga iniciada...", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar Cliente Web
        wView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                swipeRefreshLayout.setRefreshing(false); // Detener animación de refresco
                wView.setVisibility(View.VISIBLE); // Hacer visible el WebView
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                wView.loadUrl("file:///android_asset/error.html"); // Cargar página de error local
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                wView.loadUrl("file:///android_asset/error.html"); // Cargar página de error local en caso de SSL
            }
        });

        // Soporte de WebChromeClient para mejorar compatibilidad con JavaScript
        wView.setWebChromeClient(new WebChromeClient());

        // Cargar URL
        wView.loadUrl(Constants.CHILITO);





        // Configurar SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(() -> wView.reload());
    }

    // Manejo del botón de retroceso en la WebView
    @Override
    public void onBackPressed() {
        if (wView.canGoBack()) {
            wView.goBack(); // Ir a la página anterior en la WebView
        } else {
            super.onBackPressed(); // Salir de la aplicación
        }
    }

    // Soporte de Ciclo de Vida
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        log.v("MyApp", "onDestroy");
    }
    @Override
    protected void onPause() {
        super.onPause();
        log.v("MyApp", "onPause");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        log.v("MyApp", "onRestart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        log.v("MyApp", "onResume");
    }

    @Override

    protected void onStart() {
        super.onStart();
        log.v("MyApp", "onStart");
    }
    @Override
    protected void onStop() {
        super.onStop();
        log.v("MyApp", "onStop");
    }

}