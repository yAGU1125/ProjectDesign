package com.kit.projectdesign.ui;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.kit.projectdesign.data.ApiResp;
import com.kit.projectdesign.data.EntryReq;
import com.kit.projectdesign.net.EntriesApi;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JsBridge {
    private final Activity activity;
    private final WebView webView;
    private final EntriesApi api;
    private final Gson gson = new Gson();

    public JsBridge(Activity activity, WebView webView, EntriesApi api){
        this.activity = activity;
        this.webView = webView;
        this.api = api;
    }

    @JavascriptInterface
    public void submit(String json){
        try{
            EntryReq req = gson.fromJson(json, EntryReq.class);
            if (req == null || req.name == null || req.name.trim().isEmpty()){
                sendBack("error","name is required"); return;
            }
            if (req.qty < 0) req.qty = 0;

            api.submit(req).enqueue(new Callback<ApiResp>() {
                @Override public void onResponse(Call<ApiResp> call, Response<ApiResp> resp){
                    if (resp.isSuccessful() && resp.body()!=null && resp.body().ok){
                        sendBack("ok","submitted: id=" + resp.body().id);
                    } else {
                        String msg = (resp.body()!=null && resp.body().error!=null)
                                ? resp.body().error : "server error";
                        sendBack("error", msg);
                    }
                }
                @Override public void onFailure(Call<ApiResp> call, Throwable t){
                    sendBack("error","network failed: " + t.getMessage());
                }
            });
        }catch(Exception e){
            sendBack("error","invalid json");
        }
    }

    private void sendBack(String status, String msg){
        String js = "window.onNativeResult && window.onNativeResult('"
                + escape(status) + "', '" + escape(msg) + "');";
        activity.runOnUiThread(() -> webView.evaluateJavascript(js, null));
    }

    private String escape(String s){
        return s.replace("\\","\\\\").replace("'","\\'").replace("\n","\\n");
    }
}

