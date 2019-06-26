package com.example.mycurrency;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface CurrencyAPI {
    @GET("live?access_key=1043b6b41db493130e26be85d9f90978&currencies=USD,EUR,GBP,UAH,RUB,JPY&format=1")
    Call<APIData> currency();
}

class APIData {
    public boolean success;
    public String terms;
    public String privacy;
    public int timestamp;
    public String source;

    class CurrencyData {
        public String USDUSD;
        public String USDEUR;
        public String USDGBP;
        public String USDUAH;
        public String USDRUB;
        public String USDJPY;
    }

    public CurrencyData quotes;
}

interface GraphAPI {
    @GET("exchange")
    Call<List<GraphPoint>> getDataByDate(@Query("date") String date, @Query("json") String json, @Query("valcode") String currency);
}

class GraphPoint {
    public String txt;
    public float rate;
    public String cc;
    public String exchangedate;
    public String message;
}
