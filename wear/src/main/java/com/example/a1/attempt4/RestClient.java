package com.example.a1.attempt4;

import com.loopj.android.http.*;

public class RestClient {
    private static final String BASE_URL = "https://api.particle.io/v1/devices/53ff71066667574857452367/";

    private static AsyncHttpClient client = new AsyncHttpClient(false, 80, 443);

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl + "?access_token=a076afbb8dac01d59f7aa8d2561d7fdc2b394a6b";
    }
}
