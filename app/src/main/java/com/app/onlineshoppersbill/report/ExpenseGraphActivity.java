package com.app.onlineshoppersbill.report;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.app.onlineshoppersbill.Constant;
import com.app.onlineshoppersbill.R;
import com.app.onlineshoppersbill.model.MonthData;
import com.app.onlineshoppersbill.networking.ApiClient;
import com.app.onlineshoppersbill.networking.ApiInterface;
import com.app.onlineshoppersbill.utils.BaseActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseGraphActivity extends BaseActivity {


    int mYear = 2020;
    BarChart barChart;
    TextView txtTotalExpense, txtSelectYear;
    ArrayList<BarEntry> barEntries;
    private ShimmerFrameLayout mShimmerViewContainer;

    SharedPreferences sp;
    String currency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_graph);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.monthly_expense_in_graph);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        currency = sp.getString(Constant.SP_CURRENCY_SYMBOL, "N/A");

        barChart = findViewById(R.id.barchart);
        txtTotalExpense = findViewById(R.id.txt_total_sales);
        txtSelectYear = findViewById(R.id.txt_select_year);

        txtTotalExpense.setVisibility(View.INVISIBLE);
        barChart.setDrawBarShadow(false);

        barChart.setDrawValueAboveBar(true);

        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);

        barChart.setVisibility(View.INVISIBLE);


        String currentYear = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        txtSelectYear.setText(getString(R.string.year) + currentYear);


        getMonthlyExpense();

    }


    public void getGraphData() {


        String[] monthList = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthList));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(12);

        BarDataSet barDataSet = new BarDataSet(barEntries, getString(R.string.monthly_expense_report));
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);

        barChart.setData(barData);

        barChart.setScaleEnabled(false);  //for fixed bar chart,no zoom



    }




    public void getMonthlyExpense() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<MonthData> call;
        String staffId = sp.getString(Constant.SP_STAFF_ID, "");
        String auth_token = sp.getString(Constant.SP_AUTH_TOKEN, "");
        call = apiInterface.getMonthlyExpense(auth_token, staffId);

        call.enqueue(new Callback<MonthData>() {
            @Override
            public void onResponse(@NonNull Call<MonthData> call, @NonNull Response<MonthData> response) {


                if (response.isSuccessful() && response.body() != null) {
                    MonthData monthDataList;
                    monthDataList = response.body();


                    if (monthDataList == null) {


                        Log.d("Data", "Empty");


                    } else {

                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        barEntries = new ArrayList<>();
                        float jan=Float.parseFloat(monthDataList.getJan());
                        float feb=Float.parseFloat(monthDataList.getFeb());
                        float mar=Float.parseFloat(monthDataList.getMar());
                        float apr=Float.parseFloat(monthDataList.getApr());
                        float may=Float.parseFloat(monthDataList.getMay());
                        float jun=Float.parseFloat(monthDataList.getJun());
                        float jul=Float.parseFloat(monthDataList.getJul());
                        float aug=Float.parseFloat(monthDataList.getAug());
                        float sep=Float.parseFloat(monthDataList.getSep());
                        float oct=Float.parseFloat(monthDataList.getOct());
                        float nov=Float.parseFloat(monthDataList.getNov());
                        float dec=Float.parseFloat(monthDataList.getDec());



                        barEntries.add(new BarEntry(1, jan));
                        barEntries.add(new BarEntry(2, feb));
                        barEntries.add(new BarEntry(3, mar));
                        barEntries.add(new BarEntry(4, apr));
                        barEntries.add(new BarEntry(5, may));
                        barEntries.add(new BarEntry(6, jun));
                        barEntries.add(new BarEntry(7, jul));
                        barEntries.add(new BarEntry(8, aug));
                        barEntries.add(new BarEntry(9, sep));
                        barEntries.add(new BarEntry(10, oct));
                        barEntries.add(new BarEntry(11, nov));
                        barEntries.add(new BarEntry(12, dec));

                        getGraphData();

                        barChart.setVisibility(View.VISIBLE);
                        txtTotalExpense.setVisibility(View.VISIBLE);

                        float totalExpense=jan+feb+mar+apr+may+jun+jul+aug+sep+oct+nov+dec;
                        txtTotalExpense.setText(getString(R.string.total_expense)+currency+totalExpense);

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<MonthData> call, @NonNull Throwable t) {

                Toast.makeText(ExpenseGraphActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });


    }


    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
