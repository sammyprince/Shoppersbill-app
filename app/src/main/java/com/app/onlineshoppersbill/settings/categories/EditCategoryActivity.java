package com.app.onlineshoppersbill.settings.categories;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.app.onlineshoppersbill.Constant;
import com.app.onlineshoppersbill.R;
import com.app.onlineshoppersbill.database.DatabaseAccess;
import com.app.onlineshoppersbill.networking.ApiClient;
import com.app.onlineshoppersbill.networking.ApiInterface;
import com.app.onlineshoppersbill.utils.BaseActivity;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCategoryActivity extends BaseActivity {


    EditText etxtCategoryName, etxtCategoryCode, etxtCategoryDescription;
    TextView txtUpdateCategory, txtEdit;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.update_category);

        txtEdit = findViewById(R.id.txt_edit);
        txtUpdateCategory = findViewById(R.id.txt_update_category);
        etxtCategoryName = findViewById(R.id.etxt_category_name);
        etxtCategoryCode = findViewById(R.id.etxt_category_code);
        etxtCategoryDescription = findViewById(R.id.etxt_category_description);
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String categoryId = getIntent().getExtras().getString("category_id");
        String categoryName = getIntent().getExtras().getString("category_name");
        String categoryCode = getIntent().getExtras().getString("category_code");
        String categoryDescription = getIntent().getExtras().getString("category_description");


        etxtCategoryName.setText(categoryName);
        etxtCategoryName.setEnabled(false);
        etxtCategoryCode.setText(categoryCode);
        etxtCategoryCode.setEnabled(false);
        etxtCategoryDescription.setText(categoryDescription);
        etxtCategoryDescription.setEnabled(false);
        txtUpdateCategory.setVisibility(View.INVISIBLE);


        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etxtCategoryName.setEnabled(true);
                etxtCategoryCode.setEnabled(true);
                etxtCategoryDescription.setEnabled(true);
                txtUpdateCategory.setVisibility(View.VISIBLE);
                etxtCategoryName.setTextColor(Color.RED);
                etxtCategoryCode.setTextColor(Color.RED);
                etxtCategoryDescription.setTextColor(Color.RED);

                txtEdit.setVisibility(View.GONE);

            }
        });


        txtUpdateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = etxtCategoryName.getText().toString().trim();
                String categoryCode = etxtCategoryCode.getText().toString().trim();
                String categoryDescription = etxtCategoryDescription.getText().toString().trim();

                if (categoryName.isEmpty()) {
                    etxtCategoryName.setError(getString(R.string.enter_category_name));
                    etxtCategoryName.requestFocus();
                }
                else if (categoryCode.isEmpty()) {
                    etxtCategoryCode.setError(getString(R.string.enter_category_code));
                    etxtCategoryCode.requestFocus();
                }
                else if (categoryDescription.isEmpty()) {
                    etxtCategoryDescription.setError(getString(R.string.enter_category_description));
                    etxtCategoryDescription.requestFocus();
                } else {

                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                    Call<String> call;
                    String auth_token = sp.getString(Constant.SP_AUTH_TOKEN, "");
                    String staffId = sp.getString(Constant.SP_STAFF_ID, "");
                    call = apiInterface.updateCategory(auth_token, staffId, categoryId, categoryName, categoryCode, categoryDescription);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            if (response.isSuccessful() && response.body().equals(Constant.KEY_SUCCESS)) {
                                Toasty.success(EditCategoryActivity.this, R.string.successfully_added, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditCategoryActivity.this, CategoriesActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            else{
                                Toasty.error(EditCategoryActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            Toasty.error(EditCategoryActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
