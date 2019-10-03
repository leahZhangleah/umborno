package com.example.umborno.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ArrayTypeConverter {

    @TypeConverter
    public String arrToString(Weather[] arr){
        Gson gson = new Gson();
        String json = gson.toJson(arr);
        return json;
    }

    @TypeConverter
    public Weather[] stringToArr(String json){
        Type arrType = new TypeToken<Weather[]>(){}.getType();
        return new Gson().fromJson(json,arrType);
    }
}
