package com.example.umborno.http;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.umborno.model.location_key_model.LocationKey;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public abstract class LocationBoundResource<LocalType> {
    private static final String TAG = "LocationBoundResource";
    private Maybe<LocalType> result;

    public LocationBoundResource() {

        result = loadFromDB().subscribeOn(Schedulers.io())
                .filter(new Predicate<LocalType>() {
                    @Override
                    public boolean test(LocalType localType) throws Exception {
                        if(localType==null||shouldFetch(localType)) return false;
                        return true;
                    }
                })
                .doOnSuccess(new Consumer<LocalType>() {
                    @Override
                    public void accept(LocalType localType) throws Exception {
                        Log.d(TAG, "success on fetching from db");
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "error when fetching from db "+throwable.getMessage());
                    }
                })
                .switchIfEmpty(
                        createCall().subscribeOn(Schedulers.io())
                                .doOnSuccess(new Consumer<LocalType>() {
                                    @Override
                                    public void accept(LocalType localType) throws Exception {
                                        Log.d(TAG, "success on fetching from internet");
                                        saveCallResult(localType);
                                    }
                                })
                                .doOnError(new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Log.d(TAG, "error on fetching from internet "+throwable.getMessage());
                                        onFetchFailed(throwable);
                                    }
                                })
                );
                  /*.concatMap(new Function<LocalType, MaybeSource<? extends LocalType>>() {
                    @Override
                    public MaybeSource<? extends LocalType> apply(LocalType localType) throws Exception {
                        if(localType==null||shouldFetch(localType)){
                            return createCall().subscribeOn(Schedulers.io())
                                    .doOnSuccess(new Consumer<LocalType>() {
                                        @Override
                                        public void accept(LocalType localType) throws Exception {
                                            Log.d(TAG, "success on fetching from internet");
                                            saveCallResult(localType);
                                        }
                                    })
                                    .doOnError(new Consumer<Throwable>() {
                                        @Override
                                        public void accept(Throwable throwable) throws Exception {
                                            Log.d(TAG, "error on fetching from internet "+throwable.getMessage());
                                            onFetchFailed(throwable);
                                        }
                                    });

                        }
                        return Maybe.just(localType);
                    }
                });
      result =loadFromDB()
                .subscribeOn(Schedulers.io())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "error when fetching location from db "+throwable.getMessage());
                    }
                })
                .filter(new Predicate<LocalType>() {
            @Override
            public boolean test(LocalType localTypes) throws Exception {
                if(localTypes==null){
                    Log.d(TAG, "db data:null");
                    return false;
                }
                Log.d(TAG, "db location available");
                return true;
            }
        })
                .switchIfEmpty(
                createCall().subscribeOn(Schedulers.io())
                .doOnSuccess(new Consumer<LocalType>() {
                    @Override
                    public void accept(LocalType localType) throws Exception {
                        saveCallResult(localType);
                    }
                })
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d(TAG, "error on fetching location from internet "+throwable.getMessage());
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
