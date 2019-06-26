package com.example.mycurrency;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements Observer<List<String>> {



    private RecyclerView recyclerView;

    private currencyAdapter currencyAdapter1;

    private LiveData<List<String>> listLiveData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerView = findViewById(R.id.recyclerView);

        List<String> list0 = new ArrayList<>();
        currencyAdapter1 = new currencyAdapter(list0);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(currencyAdapter1);

        Bundle arguments = getIntent().getExtras();
        String toEUR = arguments.get("1").toString();
        String toGBP = arguments.get("2").toString();
        String toUAH = arguments.get("3").toString();
        String toRUB = arguments.get("4").toString();
        String toJPY = arguments.get("5").toString();

        List<String> currencies = new ArrayList<>();
        currencies.add("1 USD \n = \n" + toEUR + " EUR");
        currencies.add("1 USD \n = \n" + toGBP + " GBP");
        currencies.add("1 USD \n = \n" + toUAH + " UAH");
        currencies.add("1 USD \n = \n" + toRUB + " RUB");
        currencies.add("1 USD \n = \n" + toJPY + " JPY");

        listLiveData = new MutableLiveData<>();

        ((MutableLiveData<List<String>>) listLiveData).setValue(currencies);

        listLiveData.observe(this,this);
    }

    @Override
    public void onChanged(@Nullable List<String> list) {
        currencyAdapter1.func(list);
    }
}
