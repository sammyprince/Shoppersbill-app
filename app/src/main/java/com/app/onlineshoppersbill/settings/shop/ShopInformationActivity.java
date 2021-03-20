package com.app.onlineshoppersbill.settings.shop;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import com.app.onlineshoppersbill.Constant;
import com.app.onlineshoppersbill.R;
import com.app.onlineshoppersbill.model.ShopInformation;
import com.app.onlineshoppersbill.networking.ApiClient;
import com.app.onlineshoppersbill.networking.ApiInterface;
import com.app.onlineshoppersbill.utils.BaseActivity;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopInformationActivity extends BaseActivity {


    EditText etxtShopName, etxtTax, etxtShopContact, etxtShopEmail, etxtShopAddress;
    ProgressDialog loading;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_information);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.shop_information);
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);


        etxtShopName = findViewById(R.id.etxt_shop_name);
        etxtShopContact = findViewById(R.id.etxt_shop_contact);
        etxtShopEmail = findViewById(R.id.etxt_shop_email);
        etxtShopAddress = findViewById(R.id.etxt_shop_address);
        etxtTax = findViewById(R.id.etxt_tax);

        getShopInfo();

        etxtShopName.setEnabled(false);
        etxtShopContact.setEnabled(false);
        etxtShopEmail.setEnabled(false);
        etxtShopAddress.setEnabled(false);
        etxtTax.setEnabled(false);
    }



    public void getShopInfo() {

        loading=new ProgressDialog(this);
        loading.setMessage(getString(R.string.please_wait));
        loading.setCancelable(false);
        loading.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ShopInformation> call;
        String staffId = sp.getString(Constant.SP_STAFF_ID, "");
        String auth_token = sp.getString(Constant.SP_AUTH_TOKEN, "");
        call = apiInterface.shopInformation(auth_token, staffId);

        call.enqueue(new Callback<ShopInformation>() {
            @Override
            public void onResponse(@NonNull Call<ShopInformation> call, @NonNull Response<ShopInformation> response) {


                if (response.isSuccessful() && response.body() != null) {

                    loading.dismiss();
                    ShopInformation shopInformation;
                    shopInformation = response.body();



                    if (shopInformation == null) {


                        Toasty.warning(ShopInformationActivity.this, R.string.no_product_found, Toast.LENGTH_SHORT).show();


                    } else {


                        String shopName=shopInformation.getShopName();
                        String shopContact=shopInformation.getShopContact();
                        String shopEmail=shopInformation.getShopEmail();
                        String shopAddress=shopInformation.getShopAddress();
                        String tax=shopInformation.getTax();


                        etxtShopName.setText(shopName);
                        etxtShopContact.setText(shopContact);
                        etxtShopEmail.setText(shopEmail);
                        etxtShopAddress.setText(shopAddress);
                        etxtTax.setText(tax);

                        SharedPreferences.Editor editor = sp.edit();
                        //Adding values to editor

                        editor.putString(Constant.SP_SHOP_NAME, shopName);
                        editor.putString(Constant.SP_SHOP_ADDRESS, shopAddress);
                        editor.putString(Constant.SP_SHOP_EMAIL, shopEmail);
                        editor.putString(Constant.SP_SHOP_CONTACT, shopContact);
                        editor.putString(Constant.SP_TAX, tax);


                        //Saving values to Share preference
                        editor.apply();

                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ShopInformation> call, @NonNull Throwable t) {

                loading.dismiss();
                Toast.makeText(ShopInformationActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
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
