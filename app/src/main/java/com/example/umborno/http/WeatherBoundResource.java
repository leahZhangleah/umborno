package com.example.umborno.http;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public abstract class WeatherBoundResource<LocalType> {
    private static final String TAG = "WeatherBoundResource";
    private Maybe<LocalType> result;

    public WeatherBoundResource() {
        /*result = loadFromDB()
                .subscribeOn(Schedulers.io())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "error on fecthing weather from db "+throwable.getMessage());
                    }
                })
                .concatMap(new Function<LocalType, Publisher<? extends LocalType>>() {
                    @Override
                    public Publisher<? extends LocalType> apply(LocalType localType) throws Exception {
                        if(localType==null||shouldFetch(localType)){

                            return createCall().subscribeOn(Schedulers.io())
                                    .doOnSuccess(new Consumer<LocalType>() {
                                        @Override
                                        public void accept(LocalType localType) throws Exception {
                                            saveCallResult(localType);
                                        }
                                    })
                                    .doOnError(new Consumer<Throwable>() {
                                        @Override
                                        public void accept(Throwable throwable) throws Exception {
                                            Log.d(TAG, "error on fetching weather from internet "+throwable.getMessage());
                                            onFetchFailed(throwable);
                                        }
                                    });
                        }
                        return Flowable.just(localType);
                    }
                });
*/
        /*result =loadFromDB()
                .subscribeOn(Schedulers.io())

                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "error on fecthing weather from db "+throwable.getMessage());
                    }
                })
                .filter(new Predicate<LocalType>() {
            @Override
            public boolean test(LocalType localTypes) throws Exception {
                if(localTypes==null){
                    return false;
                }else{
                    if(shouldFetch(localTypes)){
                        return false;
                    }
                    return true;
                }
            }
        })
                .switchIfEmpty(
                createCall().subscribeOn(Schedulers.io())
                        .doOnNext(new Consumer<LocalType>() {
                            @Override
                            public void accept(LocalType localType) throws Exception {
                                saveCallResult(localType);
                            }
                        })
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d(TAG, "error on fetching weather from internet "+throwable.getMessage());
                                onFetchFailed(throwable);
                            }
                        })
        );*/
    }

    //called when fetch from network fails.
    protected abstract void onFetchFailed(Throwable throwable);

    //called to get cached data from db
    @NonNull
    protected abstract Maybe<LocalType> loadFromDB();

    //called to create api call
    @NonNull
    protected abstract Maybe<LocalType> createCall();

    //called to save the result of api response into db
    protected abstract void saveCallResult(@NonNull LocalType item);

    //called with data in db to decide whether to fetch updated data from network or not
    protected abstract boolean shouldFetch(@Nullable LocalType resultType);

    protected Maybe<LocalType> getResult(){
        return result;
    }

}
