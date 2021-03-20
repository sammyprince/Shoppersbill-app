package com.app.onlineshoppersbill.register;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.app.onlineshoppersbill.Constant;
import com.app.onlineshoppersbill.R;
import com.app.onlineshoppersbill.model.RegisterBaseInfo;
import com.app.onlineshoppersbill.model.RegisterInfo;
import com.app.onlineshoppersbill.networking.ApiClient;
import com.app.onlineshoppersbill.networking.ApiInterface;
import com.app.onlineshoppersbill.product.ProductActivity;
import com.app.onlineshoppersbill.utils.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    ProgressDialog loading;
    SharedPreferences sp;

    EditText etxtBusinessName, etxtEmail, etxtCountry, etxtCurrency, etxtTimeZone, etxtFirstName, etxtUserName, etxtPassword;
    TextView txtRegisterUser;
    ArrayAdapter<String> currencyAdapter, timezoneAdapter;
    RegisterBaseInfo baseInfo;
    String selectedCurrency, selectedTimezone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.register);
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        etxtBusinessName = findViewById(R.id.etxt_business_name);
        etxtEmail = findViewById(R.id.etxt_register_email);
        etxtCountry = findViewById(R.id.etxt_country);
        etxtCurrency = findViewById(R.id.etxt_currency);
        etxtTimeZone = findViewById(R.id.etxt_time_zone);
        etxtFirstName = findViewById(R.id.etxt_first_name);
        etxtUserName = findViewById(R.id.etxt_user_name);
        etxtPassword = findViewById(R.id.etxt_register_password);

        txtRegisterUser = findViewById(R.id.txt_register_user);

        getRegisterBaseInfo();

        etxtCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currencyAdapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_list_item_1);
                currencyAdapter.addAll(baseInfo.getCurrencyList());

                AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);

                Button dialogButton = dialogView.findViewById(R.id.dialog_button);
                EditText dialogInput = dialogView.findViewById(R.id.dialog_input);
                TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
                ListView dialogList = dialogView.findViewById(R.id.dialog_list);


                dialogTitle.setText(R.string.currency);
                dialogList.setVerticalScrollBarEnabled(true);
                dialogList.setAdapter(currencyAdapter);

                dialogInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        Log.d("data", s.toString());
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        currencyAdapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.d("data", s.toString());
                    }
                });


                final AlertDialog alertDialog = dialog.create();

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();


                dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        alertDialog.dismiss();
                        final String selectedItem = currencyAdapter.getItem(position);

                        etxtCurrency.setText(selectedItem);
                        for (int i = 0; i < baseInfo.getCurrencyList().size(); i++) {
                            if (baseInfo.getCurrencyList().get(i).equalsIgnoreCase(selectedItem)) {
                                // Get the ID of selected Country
                                selectedCurrency = baseInfo.getCurrencyList().get(i);
                            }
                        }

                    }
                });
            }
        });


        etxtTimeZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timezoneAdapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_list_item_1);
                timezoneAdapter.addAll(baseInfo.getTimezoneList());

                AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);

                Button dialogButton = (Button) dialogView.findViewById(R.id.dialog_button);
                EditText dialogInput = (EditText) dialogView.findViewById(R.id.dialog_input);
                TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
                ListView dialogList = (ListView) dialogView.findViewById(R.id.dialog_list);
                dialogTitle.setText(R.string.time_zone);
                dialogList.setVerticalScrollBarEnabled(true);
                dialogList.setAdapter(timezoneAdapter);

                dialogInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        Log.d("data", s.toString());
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        timezoneAdapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.d("data", s.toString());
                    }
                });


                final AlertDialog alertDialog = dialog.create();

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();


                dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        alertDialog.dismiss();
                        final String selectedItem = timezoneAdapter.getItem(position);

                        etxtTimeZone.setText(selectedItem);


                        for (int i = 0; i < baseInfo.getTimezoneList().size(); i++) {
                            if (baseInfo.getTimezoneList().get(i).equalsIgnoreCase(selectedItem)) {
                                // Get the ID of selected Country
                                selectedTimezone = baseInfo.getTimezoneList().get(i);
                            }
                        }
                    }
                });
            }
        });

        txtRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String businessName = etxtBusinessName.getText().toString();
                String email = etxtEmail.getText().toString();
                String country = etxtCountry.getText().toString();
                String currency = selectedCurrency;
                String timezone = selectedTimezone;
                String firstName = etxtFirstName.getText().toString();
                String userName = etxtUserName.getText().toString();
                String password = etxtPassword.getText().toString();

                if (businessName.isEmpty()) {
                    etxtBusinessName.setError(getString(R.string.business_name_cannot_be_empty));
                    etxtBusinessName.requestFocus();
                } else if (country.isEmpty()) {
                    etxtCountry.setError(getString(R.string.country_cannot_be_empty));
                    etxtCountry.requestFocus();
                } else if (currency.isEmpty()) {
                    etxtCurrency.setError(getString(R.string.currency_cannot_be_empty));
                    etxtCurrency.requestFocus();
                } else if (timezone.isEmpty()) {
                    etxtTimeZone.setError(getString(R.string.timezone_cannot_be_empty));
                    etxtTimeZone.requestFocus();
                } else if (firstName.isEmpty()) {
                    etxtFirstName.setError(getString(R.string.first_name_cannot_be_empty));
                    etxtFirstName.requestFocus();
                } else if (userName.isEmpty()) {
                    etxtUserName.setError(getString(R.string.user_name_cannot_be_empty));
                    etxtUserName.requestFocus();
                } else if (password.isEmpty()) {
                    etxtPassword.setError(getString(R.string.password_cannot_be_empty));
                    etxtPassword.requestFocus();
                } else {
                    registerUser(businessName, email, country, currency, timezone, firstName, userName, password);
                }
            }
        });

    }

    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; goto parent activity.
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void  registerUser(String businessName,
                               String email,
                               String country,
                               String currency,
                               String timezone,
                               String firstName,
                               String userName,
                               String password) {

        loading=new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();

        ApiInterface getResponse = ApiClient.getApiClient().create(ApiInterface.class);
        Call<RegisterInfo> call;

        call = getResponse.registerUser(businessName, email, country, currency, timezone, firstName, userName, password);

        call.enqueue(new Callback<RegisterInfo>() {
            @Override
            public void onResponse(@NonNull Call<RegisterInfo> call, @NonNull Response<RegisterInfo> response) {

                if (response.isSuccessful() && response.body() != null) {

                    loading.dismiss();
                    String value = response.body().getValue();
                    if (value.equals(Constant.KEY_SUCCESS)) {
                        Toasty.success(getApplicationContext(), R.string.user_successfully_registered, Toast.LENGTH_SHORT).show();
                        RegisterActivity.this.finish();
                    }

                    else if (value.equals(Constant.KEY_FAILURE)) {

                            loading.dismiss();

                            Toasty.error(RegisterActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            loading.dismiss();
                            Toasty.error(RegisterActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        }

                }

                else
                {
                    loading.dismiss();
                    Log.d("Error",response.errorBody().toString());
                }

            }



            @Override
            public void onFailure(@NonNull Call<RegisterInfo> call, @NonNull Throwable t) {
                loading.dismiss();
                Log.d("Error! ", t.toString());
                Toasty.error(RegisterActivity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getRegisterBaseInfo() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<RegisterBaseInfo> call;

        call = apiInterface.getRegisterBaseInfo();

        call.enqueue(new Callback<RegisterBaseInfo>() {
            @Override
            public void onResponse(@NonNull Call<RegisterBaseInfo> call, @NonNull Response<RegisterBaseInfo> response) {


                if (response.isSuccessful() && response.body() != null) {

                    baseInfo = response.body();
                }

            }

            @Override
            public void onFailure(@NonNull Call<RegisterBaseInfo> call, @NonNull Throwable t) {

                //write own action
            }
        });
    }
}



