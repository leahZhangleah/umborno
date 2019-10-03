package com.example.umborno.http;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.umborno.http.ApiResponse;
import com.example.umborno.http.Resource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class NetworkBoundResource<ResultType,RequestType> {
    private static final String TAG = "NetworkBoundResource";
    private AppExecutors executor;
    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    public NetworkBoundResource(AppExecutors executor) {
        this.executor = executor;
        result.setValue((Resource<ResultType>) Resource.loading(null));
        final LiveData<ResultType> dbSource = loadFromDB();
        result.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(ResultType resultType) {
                result.removeSource(dbSource);
                if(shouldFetch(resultType)){
                    fetchFromNetwork(dbSource);
                }else{
                    result.addSource(dbSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(ResultType resultType) {
                            setValue(Resource.success(resultType));
                        }
                    });
                }
            }
        });
    }

    private void setValue(Resource<ResultType> newValue){
        if(result.getValue()!= newValue){
            result.setValue(newValue);
        }
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        final LiveData<ApiResponse<RequestType>> apiResponse = createCall();

        //show data from db temporarily before new data is fetched from network
        result.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(ResultType resultType) {
                setValue(Resource.loading(resultType));
            }
        });

        result.addSource(apiResponse, new Observer<ApiResponse<RequestType>>() {
            @Override
            public void onChanged(final ApiResponse<RequestType> response) {
                result.removeSource(apiResponse);
                result.removeSource(dbSource);
                if(response.isSuccessful()){
                    executor.getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if(response.getBody()!=null){
                                saveCallResult(response.getBody());
                            }
                            executor.getMainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    result.addSource(loadFromDB(), new Observer<ResultType>() {
                                        @Override
                                        public void onChanged(ResultType resultType) {
                                            setValue(Resource.success(resultType));
                                        }
                                    });
                                }
                            });
                        }
                    });
                }else{
                    onFetchFailed();
                    Log.d(TAG, "fetch from internet failed: "+response.getError().getLocalizedMessage());
                    result.addSource(dbSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(ResultType resultType) {
                            setValue(Resource.error(response.getError().getMessage(),resultType));
                        }
                    });
                }
            }
        });
    }

    //called when fetch from network fails.
    protected abstract void onFetchFailed();

    //called to get cached data from db
    @NonNull
    protected abstract LiveData<ResultType> loadFromDB();

    //called to create api call
    @NonNull
    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    //called to save the result of api response into db
    protected abstract void saveCallResult(@NonNull RequestType item);

    //called with data in db to decide whether to fetch updated data from network or not
    protected abstract boolean shouldFetch(@Nullable ResultType resultType);

    public final LiveData<Resource<ResultType>> asLiveData(){
        return result;
    }
}
