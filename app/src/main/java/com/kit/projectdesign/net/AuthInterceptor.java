package com.kit.projectdesign.net;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    public interface TokenProvider { String getToken(); }
    private final TokenProvider provider;

    public AuthInterceptor(TokenProvider provider){ this.provider = provider; }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder b = chain.request().newBuilder();
        String t = provider.getToken();
        if (t != null && !t.isEmpty()) {
            b.header("Authorization", "Bearer " + t);
        }
        return chain.proceed(b.build());
    }
}

