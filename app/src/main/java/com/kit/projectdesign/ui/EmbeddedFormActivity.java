package com.kit.projectdesign.ui;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.kit.projectdesign.net.ApiClient;
import com.kit.projectdesign.net.EntriesApi;
import com.kit.projectdesign.util.MyTokenStore;

public class EmbeddedFormActivity extends AppCompatActivity {
    private WebView webView;

    // ⚙️ 修改为你的实际地址（开发可用 http://10.0.2.2:8080/）
    private static final String BASE_API = "http://pd.yagu1125.com:3031/";
    private static final String FORM_URL = "file:///android_asset/form.html";

    private String getAccessToken(){ return MyTokenStore.get(); }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(false);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);

        webView.setWebViewClient(new WebViewClient(){
            @Override public boolean shouldOverrideUrlLoading(WebView view, android.webkit.WebResourceRequest request) {
                String hostForm = Uri.parse(FORM_URL).getHost();
                String hostReq  = request.getUrl().getHost();
                return hostForm != null && hostReq != null && !hostForm.equalsIgnoreCase(hostReq);
            }
        });

        EntriesApi api = ApiClient.create(BASE_API, this::getAccessToken);
        webView.addJavascriptInterface(new JsBridge(this, webView, api), "Android");
        webView.loadUrl(FORM_URL);
    }

    @Override protected void onDestroy() {
        if (webView != null) webView.destroy();
        super.onDestroy();
    }
}
