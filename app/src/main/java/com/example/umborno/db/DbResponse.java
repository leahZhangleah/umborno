package com.example.umborno.db;

import androidx.annotation.Nullable;

public class DbResponse<T> {
    private static final String TAG = "DbResponse";
    private final long resultCode;
    @Nullable
    private final T body;
    private DbFunction dbFunction;
    public static final int LOADING_CODE = -2;

    private DbResponse(long resultCode, @Nullable T body, DbFunction dbFunction) {
        this.resultCode = resultCode;
        this.body = body;
        this.dbFunction = dbFunction;
    }


    public static <T> DbResponse<T> success(long resultCode, T data, DbFunction dbFunction){
        return new DbResponse(resultCode,data, dbFunction);
    }

    public static <T> DbResponse<T> error(long resultCode, @Nullable T data, DbFunction dbFunction){
        return new DbResponse(resultCode,data, dbFunction);
    }

    public static <T> DbResponse<T> loading(@Nullable T data, DbFunction dbFunction){
        return new DbResponse(LOADING_CODE,data, dbFunction);
    }

    public long getResultCode() {
        return resultCode;
    }

    @Nullable
    public T getBody() {
        return body;
    }

    public DbFunction getDbFunction() {
        return dbFunction;
    }
}