package com.app.onlinesmartpos.customers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.onlinesmartpos.Constant;
import com.app.onlinesmartpos.R;
import com.app.onlinesmartpos.adapter.CustomerAdapter;
import com.app.onlinesmartpos.model.Customer;
import com.app.onlinesmartpos.networking.ApiClient;
import com.app.onlinesmartpos.networking.ApiInterface;
import com.app.onlinesmartpos.utils.BaseActivity;
import com.app.onlinesmartpos.utils.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomersActivity extends BaseActivity {



    private RecyclerView recyclerView;
    ImageView imgNoProduct;
    EditText etxtSearch;
    FloatingActionButton fabAdd;
    private ShimmerFrameLayout mShimmerViewContainer;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);


        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.all_customer);
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mSwipeRefreshLayout =findViewById(R.id.swipeToRefresh);
        //set color of swipe refresh
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        recyclerView = findViewById(R.id.recycler_view);
        imgNoProduct = findViewById(R.id.image_no_product);
        etxtSearch = findViewById(R.id.etxt_customer_search);
        fabAdd = findViewById(R.id.fab_add);

        // set a GridLayoutManager with default vertical orientation and 3 number of columns
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView


        recyclerView.setHasFixedSize(true);

        Utils utils=new Utils();


        //swipe refresh listeners
        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            if (utils.isNetworkAvailable(CustomersActivity.this))
            {
                getCustomerData("");
            }
            else
            {
                Toasty.error(CustomersActivity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
            }


            //after shuffle id done then swife refresh is off
            mSwipeRefreshLayout.setRefreshing(false);
        });


        if (utils.isNetworkAvailable(CustomersActivity.this))
        {
            //Load data from server
            getCustomerData("");
        }
        else
        {
            recyclerView.setVisibility(View.GONE);
            imgNoProduct.setVisibility(View.VISIBLE);
            imgNoProduct.setImageResource(R.drawable.not_found);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            //Stopping Shimmer Effects
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
            Toasty.error(this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
        }


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomersActivity.this, AddCustomersActivity.class);
                startActivity(intent);
            }
        });


        etxtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                Log.d("data", s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 1) {

                    //search data from server
                    getCustomerData(s.toString());
                } else {
                    getCustomerData("");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.d("data", s.toString());
            }


        });
    }


    public void getCustomerData(String searchText) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Customer>> call;
        String staffId = sp.getString(Constant.SP_STAFF_ID, "");
        String auth_token = sp.getString(Constant.SP_AUTH_TOKEN, "");
        call = apiInterface.getCustomers(staffId, searchText, auth_token);

        call.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(@NonNull Call<List<Customer>> call, @NonNull Response<List<Customer>> response) {


                if (response.isSuccessful() && response.body() != null) {
                    List<Customer> customerList;
                    customerList = response.body();


                    if (customerList.isEmpty()) {

                        recyclerView.setVisibility(View.GONE);
                        imgNoProduct.setVisibility(View.VISIBLE);
                        imgNoProduct.setImageResource(R.drawable.not_found);
                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);


                    } else {


                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        recyclerView.setVisibility(View.VISIBLE);
                        imgNoProduct.setVisibility(View.GONE);
                        CustomerAdapter customerAdapter = new CustomerAdapter(CustomersActivity.this, customerList);

                        recyclerView.setAdapter(customerAdapter);

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Customer>> call, @NonNull Throwable t) {

                Toast.makeText(CustomersActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                Log.d("Error : ", t.toString());
            }
        });


    }




    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
