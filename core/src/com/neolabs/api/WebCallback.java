package com.neolabs.api;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WebCallback<T extends ServerResponse> implements Callback<T> {
    private final Callback callback;

    public WebCallback(Callback callback) {
        this.callback = callback;
    }


    @Override
    public void success(T o, Response response) {
        if (o != null && o.isSuccess()) {
            try {
                callback.success(o, response);
            } catch (Throwable ignore) {
            }
        } else {
            failure(null);
        }
    }

    @Override
    public void failure(RetrofitError error) {
        try {
            callback.failure(error);
        } catch (Throwable ignore) {
        }
    }
}