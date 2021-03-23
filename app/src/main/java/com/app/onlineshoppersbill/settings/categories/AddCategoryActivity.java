package com.app.onlineshoppersbill.settings.categories;

import android.content.Context;
import android.content.Intent;
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
import com.app.onlineshoppersbill.adapter.CategoryAdapter;
import com.app.onlineshoppersbill.database.DatabaseAccess;
import com.app.onlineshoppersbill.model.Category;
import com.app.onlineshoppersbill.networking.ApiClient;
import com.app.onlineshoppersbill.networking.ApiInterface;
import com.app.onlineshoppersbill.utils.BaseActivity;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategoryActivity extends BaseActivity {


    EditText etxtCategory, etxtCategoryCode, etxtCategoryDescription;
    TextView txtAddCategory;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.add_category);

        etxtCategory = findViewById(R.id.etxt_category_name);
        etxtCategoryCode = findViewById(R.id.etxt_category_code);
        etxtCategoryDescription = findViewById(R.id.etxt_category_description);
        txtAddCategory = findViewById(R.id.txt_add_category);
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);


        txtAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String categoryName = etxtCategory.getText().toString().trim();
                String categoryCode = etxtCategoryCode.getText().toString().trim();
                String categoryDescription = etxtCategoryDescription.getText().toString().trim();

                if (categoryName.isEmpty()) {
                    etxtCategory.setError(getString(R.string.enter_category_name));
                    etxtCategory.requestFocus();
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
                    call = apiInterface.addCategory(auth_token, staffId, categoryName, categoryCode, categoryDescription);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Toasty.success(AddCategoryActivity.this, R.string.successfully_added, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddCategoryActivity.this, CategoriesActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else{
                                Toasty.error(AddCategoryActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                            Toasty.error(AddCategoryActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
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
