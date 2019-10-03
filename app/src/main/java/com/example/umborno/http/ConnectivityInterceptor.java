package com.example.umborno.http;

import android.content.Context;

import com.example.umborno.util.Utilities;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Response;

@Singleton
public class ConnectivityInterceptor implements Interceptor {
    private Context context;

    @Inject
    public ConnectivityInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if(!Utilities.isOnline(context)){
            //user is not online
            //todo
            throw new IOException();
        }
        return chain.proceed(chain.request());
    }
}
