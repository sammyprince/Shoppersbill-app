package com.app.onlineshoppersbill.settings.subscribe;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;

import com.app.onlineshoppersbill.Constant;
import com.app.onlineshoppersbill.R;
import com.app.onlineshoppersbill.adapter.PackageAdapter;
import com.app.onlineshoppersbill.model.Package;
import com.app.onlineshoppersbill.networking.ApiClient;
import com.app.onlineshoppersbill.networking.ApiInterface;
import com.app.onlineshoppersbill.utils.BaseActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribeActivity extends BaseActivity {
    private RecyclerView recyclerView;
    SharedPreferences sp;

    ImageView imgNoProduct;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.subscribe);

        recyclerView = findViewById(R.id.recycler_view);
        imgNoProduct = findViewById(R.id.image_no_product);


        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);


        // set a GridLayoutManager with default vertical orientation and 3 number of columns
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView


        recyclerView.setHasFixedSize(true);

        getSubscriptionPackages();
    }




    public void getSubscriptionPackages() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<List<Package>> call;

        String auth_token = sp.getString(Constant.SP_AUTH_TOKEN, "");
        call = apiInterface.getPackages(auth_token);

        call.enqueue(new Callback<List<Package>>() {
            @Override
            public void onResponse(@NonNull Call<List<Package>> call, @NonNull Response<List<Package>> response) {


                if (response.isSuccessful() && response.body() != null) {

                    List<Package> subcriptionPackage;
                    subcriptionPackage = response.body();

                    if (subcriptionPackage.isEmpty())
                    {
                        Toasty.info(SubscribeActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                        imgNoProduct.setImageResource(R.drawable.no_data);

                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }

                    else {


                        //Stopping Shimmer Effects
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        PackageAdapter packageAdapter = new PackageAdapter(SubscribeActivity.this, subcriptionPackage);

                        recyclerView.setAdapter(packageAdapter);

                    }



                }

            }

            @Override
            public void onFailure(@NonNull Call<List<Package>> call, @NonNull Throwable t) {

                //write own action
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
