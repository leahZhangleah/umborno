package com.example.umborno.http;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.common.api.Api;

import java.io.IOException;
import java.util.Map;

import retrofit2.Response;

public class ApiResponse<T> {
    private static final String TAG = "ApiResponse";
    private final int code;
    @Nullable
    private final T body;
    @Nullable
    private final Throwable error;

    public ApiResponse(@Nullable Throwable error) {
        code = 500;
        body = null;
        this.error = error;
    }

    public ApiResponse(Response<T> response){
        code = response.code();
        if(response.isSuccessful()){
            body = response.body();
            error=null;
        }else{
            String msg = null;
            if(response.errorBody()!=null){
                try {
                    msg = response.errorBody().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "ApiResponse: error while parsing response");
                }
            }

            if(msg==null || msg.trim().length()==0){
                msg = response.message();
            }
            error = new IOException(msg);
            body=null;
        }
    }

    public boolean isSuccessful(){
        return code >=200 && code <300;
    }

    public int getCode() {
        return code;
    }

    @Nullable
    public T getBody() {
        return body;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }
}