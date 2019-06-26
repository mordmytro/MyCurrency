package com.example.mycurrency;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.mycurrency.MainActivity.firstSelected;
import static com.example.mycurrency.MainActivity.secondSelected;


public class GraphActivity extends AppCompatActivity {


    List<Float> points1 = new ArrayList<Float>();
    List<Float> points2= new ArrayList<Float>();
    LineGraphSeries<DataPoint> series;

    boolean bool = true;
    int days = -90;
    int daysToShow = days;
    String text = "Choose period";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        final GraphActivity graphActivity = this;

        TextView textView = (TextView)findViewById(R.id.textView4);
        textView.setText(firstSelected + " to " + secondSelected + " graph");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://bank.gov.ua/NBUStatService/v1/statdirectory/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GraphAPI graphAPI = retrofit.create(GraphAPI.class);

        for (int i = days; i <= 0; i++) {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, i);
            Date current = calendar.getTime();


            String pattern = "yyyyMMdd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String dateString = simpleDateFormat.format(current);

            if (firstSelected.equals("UAH")) {
                this.points1.add(new Float(1));
                this.updateGraph();
            } else {

                Call<List<GraphPoint>> call = graphAPI.getDataByDate(dateString, "", firstSelected);
                call.enqueue(new Callback<List<GraphPoint>>() {
                    @Override
                    public void onResponse(Call<List<GraphPoint>> call, Response<List<GraphPoint>> response) {
                        List<GraphPoint> data = response.body();
                        GraphPoint point = data.get(0);
                        graphActivity.points1.add(point.rate);
                        graphActivity.updateGraph();
                    }

                    @Override
                    public void onFailure(Call<List<GraphPoint>> call, Throwable t) {
                    }
                });
            }

            if (MainActivity.firstSelected.equals(MainActivity.secondSelected)) {
                bool = false;
            }

            if (MainActivity.secondSelected.equals("UAH")) {
                this.points2.add(new Float(1));
                this.updateGraph();
            } else {

                Call<List<GraphPoint>> call = graphAPI.getDataByDate(dateString, "", MainActivity.secondSelected);
                call.enqueue(new Callback<List<GraphPoint>>() {
                    @Override
                    public void onResponse(Call<List<GraphPoint>> call, Response<List<GraphPoint>> response) {
                        List<GraphPoint> data = response.body();
                        GraphPoint point = data.get(0);
                        Log.e("fy", String.valueOf(point.rate));
                        graphActivity.points2.add(point.rate);
                        graphActivity.updateGraph();
                    }

                    @Override
                    public void onFailure(Call<List<GraphPoint>> call, Throwable t) {
                    }
                });
            }
        }
    }

    public double min = 1000;
    public double max = -1000;

    public void updateGraph () {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        List<DataPoint> dataPointArr = new ArrayList<DataPoint>();

        int i = 0;
        for (Float p : this.points1) {
            if(this.points2.size() <= i){
                break;
            }
            if(-i < daysToShow) break;
            double d = p / this.points2.get(i);
            if(d > max) max = d;
            if(d < min) min = d;
            dataPointArr.add(new DataPoint(i, d));
            i++;
        }
        DataPoint[] dataPoints = dataPointArr.toArray(new DataPoint[dataPointArr.size()]);
        LineGraphSeries<DataPoint> seriesGraph = new LineGraphSeries<DataPoint>(dataPoints);
        graph.addSeries(seriesGraph);
        graph.getViewport().setMaxX(90);
        if (bool) {
            graph.getViewport().setMaxY(max);
            graph.getViewport().setMinY(min);
        } else {
            graph.getViewport().setMaxY(2);
            graph.getViewport().setMinY(0);
        }
    }

    public void updateGraph(int points){
        GraphView graph = (GraphView) findViewById(R.id.graph);
        List<DataPoint> dataPointArr = new ArrayList<DataPoint>();
        int i = 0;
        for (Float p : this.points1) {
            if(this.points2.size() <= i){
                break;
            }
            if(-i < daysToShow) break;
            double d = p / this.points2.get(i);
            dataPointArr.add(new DataPoint(i, d));
            i++;
            Log.e("maxmin", max + " " + min);
        }
        LineGraphSeries<DataPoint> seriesGraph = new LineGraphSeries<DataPoint>((DataPoint[])dataPointArr.toArray(new DataPoint[dataPointArr.size()]));
        graph.addSeries(seriesGraph);
        graph.getViewport().setMaxX(points);

        if (bool) {
            graph.getViewport().setMaxY(max);
            graph.getViewport().setMinY(min);
        } else {
            graph.getViewport().setMaxY(2);
            graph.getViewport().setMinY(0);
        }
    }

    public void on7 (View view) {
        daysToShow = 7;
        updateGraph(daysToShow);
        text = firstSelected + " to " + MainActivity.secondSelected + " plot " + "for 7 days";
        TextView textView = (TextView)findViewById(R.id.textView4);
        textView.setText(text);
    }

    public void on30 (View view) {
        daysToShow = 30;
        updateGraph(daysToShow);
        text = firstSelected + " to " + MainActivity.secondSelected + " plot " + "for 30 days";
        TextView textView = (TextView)findViewById(R.id.textView4);
        textView.setText(text);
    }

    public void on90 (View view) {
        daysToShow = 90;
        updateGraph(daysToShow);
        text = firstSelected + " to " + MainActivity.secondSelected + " plot " + "for 90 days";
        TextView textView = (TextView)findViewById(R.id.textView4);
        textView.setText(text);
    }
}