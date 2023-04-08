package com.example.chatapp.Retrofit;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class AccessTokenInterceptor implements Interceptor {

    private final TokenManager tokenManager;

    public AccessTokenInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if (response.code() == 401) { // Unauthorized
            synchronized (tokenManager) {
                String newAccessToken = tokenManager.refreshAccessToken();

                if (newAccessToken != null) {
                    Request.Builder newRequestBuilder = request.newBuilder()
                            .header("Authorization", "Bearer " + tokenManager.getAccessToken());
                    return chain.proceed(newRequestBuilder.build());
                }
            }
        }

        return response;
    }
}
