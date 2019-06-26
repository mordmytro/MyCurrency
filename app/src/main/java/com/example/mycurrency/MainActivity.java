package com.example.mycurrency;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String[] data1 = {"USD", "EUR", "GBP", "UAH", "RUB", "JPY"};
    String[] data2 = {"USD", "EUR", "GBP", "UAH", "RUB", "JPY"};

    public static String toEUR, toGBP, toUAH, toRUB, toJPY;
    public static String firstSelected, secondSelected;

    APIData data;
    boolean dataloaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MainActivity mainActivity = this;

        Bundle arguments = getIntent().getExtras();
        String log = arguments.get("1").toString();

        EditText Num1 = (EditText)findViewById(R.id.Num);

        TextView username = (TextView)findViewById(R.id.textView);
        username.setText("username: " + log);

        Num1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mainActivity.updateCurrency();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // адаптер
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);

        spinner1.setAdapter(adapter1);
        // заголовок
        spinner1.setPrompt("Currency 1");
        // выделяем элемент
        spinner1.setSelection(0);

        // устанавливаем обработчик нажатия
        spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.updateCurrency();
                mainActivity.firstSelected = mainActivity.data1[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        // адаптер
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(adapter2);
        // заголовок
        spinner2.setPrompt("Currency 2");
        // выделяем элемент
        spinner2.setSelection(3);

        // устанавливаем обработчик нажатия
        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.updateCurrency();
                mainActivity.secondSelected = mainActivity.data2[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.apilayer.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrencyAPI currencyAPI = retrofit.create(CurrencyAPI.class);

        Call<APIData> call = currencyAPI.currency();

        Callback<APIData> callback = new Callback<APIData>() {
            @Override
            public void onResponse(Call<APIData> call, Response<APIData> response) {
                APIData data = response.body();
                mainActivity.onJSONLoaded(data);
            }

            @Override
            public void onFailure(Call<APIData> call, Throwable t) {
                mainActivity.onJSONError(t);
            }
        };

        call.enqueue(callback);
    }

    protected void onJSONLoaded(APIData data) {
        dataloaded = true;
        this.data = data;
        TextView textView = findViewById(R.id.valText2);
        textView.setText(data.source);
        Toast.makeText(getBaseContext(), "Updated", Toast.LENGTH_SHORT).show();
        this.updateCurrency();
    }

    protected void onJSONError(Throwable t) {
        Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, NoConnection.class);
        startActivity(intent);
    }

    protected void updateCurrency() {
        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        EditText element = (EditText)findViewById(R.id.Num);
        TextView valText1 = findViewById(R.id.valText1);
        TextView valText2 = findViewById(R.id.valText2);

        double num = 1;

        if (element.getText().length() != 0) {
            num = Double.parseDouble(element.getText().toString());
        } else {
            num = 1;
        }

        if(!this.dataloaded) return;
        int id1 = spinner1.getSelectedItemPosition();
        int id2 = spinner2.getSelectedItemPosition();
        double val1 = 0;
        double val2 = 1;
        switch(id1){
            case 0:
                val1 = Double.parseDouble(this.data.quotes.USDUSD);
                break;
            case 1:
                val1 = Double.parseDouble(this.data.quotes.USDEUR);
                break;
            case 2:
                val1 = Double.parseDouble(this.data.quotes.USDGBP);
                break;
            case 3:
                val1 = Double.parseDouble(this.data.quotes.USDUAH);
                break;
            case 4:
                val1 = Double.parseDouble(this.data.quotes.USDRUB);
                break;
            case 5:
                val1 = Double.parseDouble(this.data.quotes.USDJPY);
                break;
        }
        switch(id2){
            case 0:
                val2 = Double.parseDouble(this.data.quotes.USDUSD);
                break;
            case 1:
                val2 = Double.parseDouble(this.data.quotes.USDEUR);
                break;
            case 2:
                val2 = Double.parseDouble(this.data.quotes.USDGBP);
                break;
            case 3:
                val2 = Double.parseDouble(this.data.quotes.USDUAH);
                break;
            case 4:
                val2 = Double.parseDouble(this.data.quotes.USDRUB);
                break;
            case 5:
                val2 = Double.parseDouble(this.data.quotes.USDJPY);
                break;
        }
        toEUR = this.data.quotes.USDEUR;
        toGBP = this.data.quotes.USDGBP;
        toUAH = this.data.quotes.USDUAH;
        toRUB = this.data.quotes.USDRUB;
        toJPY = this.data.quotes.USDJPY;

        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);

        double res = num * val2/val1;

        valText1.setText("1 " + spinner1.getSelectedItem().toString() + " = " + String.valueOf(df.format(res / num)) + " " + spinner2.getSelectedItem().toString());
        valText2.setText(String.valueOf(df.format(res)));
    }



    public void onUpdateButtonClick (View v) {
        final MainActivity mainActivity = this;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.apilayer.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrencyAPI currencyAPI = retrofit.create(CurrencyAPI.class);

        Call<APIData> call = currencyAPI.currency();

        Callback<APIData> callback = new Callback<APIData>() {
            @Override
            public void onResponse(Call<APIData> call, Response<APIData> response) {
                APIData data = response.body();
                mainActivity.onJSONLoaded(data);
            }

            @Override
            public void onFailure(Call<APIData> call, Throwable t) {
                mainActivity.onJSONError(t);
            }
        };
        call.enqueue(callback);
        this.updateCurrency();
    }



    public void onChangeButtonClick (View view) {
        if (dataloaded == true) {
            Intent intent = new Intent(this, Main2Activity.class);
            intent.putExtra("1", toEUR);
            intent.putExtra("2", toGBP);
            intent.putExtra("3", toUAH);
            intent.putExtra("4", toRUB);
            intent.putExtra("5", toJPY);
            startActivity(intent);
        }
    }

    public void onGraphButtonClick (View view) {
        if (dataloaded == true) {
            Intent intent = new Intent(this, GraphActivity.class);
            startActivity(intent);
        }
    }

    public void onVideoButtonClick (View view) {
        if (dataloaded == true) {
            Intent intent = new Intent(this, VideoActivity.class);
            startActivity(intent);
        }
    }
}