package com.app.onlineshoppersbill.product;

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
import com.app.onlineshoppersbill.customers.AddCustomersActivity;
import com.app.onlineshoppersbill.model.BusinessLocation;
import com.app.onlineshoppersbill.model.Category;
import com.app.onlineshoppersbill.model.Product;
import com.app.onlineshoppersbill.model.Suppliers;
import com.app.onlineshoppersbill.model.WeightUnit;
import com.app.onlineshoppersbill.networking.ApiClient;
import com.app.onlineshoppersbill.networking.ApiInterface;
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

public class AddProductActivity extends BaseActivity {


    ProgressDialog loading;
    SharedPreferences sp;

    public static EditText etxtProductCode;
    EditText etxtProductName,etxtProductStock, etxtProductCategory, etxtProductDescription, etxtProductSellPrice, etxtProductSupplier, etxtProductLocation, etxtProdcutWeightUnit, etxtProductWeight;
    TextView txtAddProdcut, txtChooseImage;
    ImageView imgProduct, imgScanCode;
    String mediaPath, encodedImage = "N/A";
    List<Category> productCategory;
    List<Suppliers> productSuppliers;
    List<WeightUnit> weightUnits;
    List<BusinessLocation> businessLocations;
    ArrayAdapter<String> categoryAdapter, supplierAdapter, weightUnitAdapter, locationAdapter;
    List<String> categoryNames, supplierNames, weightUnitNames, locationNames;

    String selectedCategoryID, selectedSupplierID, selectedWeightUnitID, selectedLocationID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);


        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.add_product);
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        etxtProductName = findViewById(R.id.etxt_product_name);
        etxtProductCode = findViewById(R.id.etxt_product_code);
        etxtProductCategory = findViewById(R.id.etxt_product_category);
        etxtProductDescription = findViewById(R.id.etxt_product_description);
        etxtProductSellPrice = findViewById(R.id.etxt_product_sell_price);
        etxtProductSupplier = findViewById(R.id.etxt_supplier);
        etxtProdcutWeightUnit = findViewById(R.id.etxt_product_weight_unit);
        etxtProductWeight = findViewById(R.id.etxt_product_weight);
        etxtProductStock = findViewById(R.id.etxt_product_stock);
        etxtProductLocation = findViewById(R.id.etxt_location);

        txtAddProdcut = findViewById(R.id.txt_add_product);
        imgProduct = findViewById(R.id.image_product);
        imgScanCode = findViewById(R.id.img_scan_code);
        txtChooseImage = findViewById(R.id.txt_choose_image);


        getProductCategory();
        getProductSuppliers();
        getWeightUnits();
        getBusinessLocations();

        imgScanCode.setOnClickListener(v -> {
            Intent intent = new Intent(AddProductActivity.this, ScannerViewActivity.class);
            startActivity(intent);
        });


        txtChooseImage.setOnClickListener(v -> {

            Intent intent = new Intent(AddProductActivity.this, ImageSelectActivity.class);
            intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
            startActivityForResult(intent, 1213);
        });

        imgProduct.setOnClickListener(v -> {

            Intent intent = new Intent(AddProductActivity.this, ImageSelectActivity.class);
            intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
            startActivityForResult(intent, 1213);
        });



        etxtProductCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryAdapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_list_item_1);
                categoryAdapter.addAll(categoryNames);

                AlertDialog.Builder dialog = new AlertDialog.Builder(AddProductActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);

                Button dialogButton = dialogView.findViewById(R.id.dialog_button);
                EditText dialogInput = dialogView.findViewById(R.id.dialog_input);
                TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
                ListView dialogList = dialogView.findViewById(R.id.dialog_list);


                dialogTitle.setText(R.string.product_category);
                dialogList.setVerticalScrollBarEnabled(true);
                dialogList.setAdapter(categoryAdapter);

                dialogInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        Log.d("data", s.toString());
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        categoryAdapter.getFilter().filter(charSequence);
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
                        final String selectedItem = categoryAdapter.getItem(position);

                        String categoryId = "0";
                        etxtProductCategory.setText(selectedItem);


                        for (int i = 0; i < categoryNames.size(); i++) {
                            if (categoryNames.get(i).equalsIgnoreCase(selectedItem)) {
                                // Get the ID of selected Country
                                categoryId = productCategory.get(i).getProductCategoryId();
                            }
                        }

                        selectedCategoryID = categoryId;

                    }
                });
            }
        });


        etxtProductSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supplierAdapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_list_item_1);
                supplierAdapter.addAll(supplierNames);

                AlertDialog.Builder dialog = new AlertDialog.Builder(AddProductActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);

                Button dialogButton = (Button) dialogView.findViewById(R.id.dialog_button);
                EditText dialogInput = (EditText) dialogView.findViewById(R.id.dialog_input);
                TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
                ListView dialogList = (ListView) dialogView.findViewById(R.id.dialog_list);
                dialogTitle.setText(R.string.suppliers);
                dialogList.setVerticalScrollBarEnabled(true);
                dialogList.setAdapter(supplierAdapter);

                dialogInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        Log.d("data", s.toString());
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        supplierAdapter.getFilter().filter(charSequence);
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
                        final String selectedItem = supplierAdapter.getItem(position);

                        String supplierId = "0";
                        etxtProductSupplier.setText(selectedItem);


                        for (int i = 0; i < supplierNames.size(); i++) {
                            if (supplierNames.get(i).equalsIgnoreCase(selectedItem)) {
                                // Get the ID of selected Country
                                supplierId = productSuppliers.get(i).getSuppliersId();
                            }
                        }


                        selectedSupplierID = supplierId;

                    }
                });
            }
        });


        etxtProdcutWeightUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weightUnitAdapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_list_item_1);
                weightUnitAdapter.addAll(weightUnitNames);

                AlertDialog.Builder dialog = new AlertDialog.Builder(AddProductActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);

                Button dialogButton = (Button) dialogView.findViewById(R.id.dialog_button);
                EditText dialogInput = (EditText) dialogView.findViewById(R.id.dialog_input);
                TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
                ListView dialogList = (ListView) dialogView.findViewById(R.id.dialog_list);


                dialogTitle.setText(R.string.product_weight_unit);
                dialogList.setVerticalScrollBarEnabled(true);
                dialogList.setAdapter(weightUnitAdapter);

                dialogInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        Log.d("data", s.toString());
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        weightUnitAdapter.getFilter().filter(charSequence);
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
                        final String selectedItem = weightUnitAdapter.getItem(position);

                        String weightUnitId = "0";
                        etxtProdcutWeightUnit.setText(selectedItem);


                        for (int i = 0; i < weightUnitNames.size(); i++) {
                            if (weightUnitNames.get(i).equalsIgnoreCase(selectedItem)) {
                                // Get the ID of selected Country
                                weightUnitId = weightUnits.get(i).getWeightUnitId();
                            }
                        }
                        selectedWeightUnitID = weightUnitId;
                    }
                });
            }
        });

        etxtProductLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationAdapter = new ArrayAdapter<>(AddProductActivity.this, android.R.layout.simple_list_item_1);
                locationAdapter.addAll(locationNames);

                AlertDialog.Builder dialog = new AlertDialog.Builder(AddProductActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_list_search, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);

                Button dialogButton = (Button) dialogView.findViewById(R.id.dialog_button);
                EditText dialogInput = (EditText) dialogView.findViewById(R.id.dialog_input);
                TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
                ListView dialogList = (ListView) dialogView.findViewById(R.id.dialog_list);


                dialogTitle.setText(R.string.product_location);
                dialogList.setVerticalScrollBarEnabled(true);
                dialogList.setAdapter(locationAdapter);

                dialogInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        Log.d("data", s.toString());
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        locationAdapter.getFilter().filter(charSequence);
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
                        final String selectedItem = locationAdapter.getItem(position);

                        String locationId = "0";
                        etxtProductLocation.setText(selectedItem);


                        for (int i = 0; i < locationNames.size(); i++) {
                            if (locationNames.get(i).equalsIgnoreCase(selectedItem)) {
                                // Get the ID of selected Country
                                locationId = businessLocations.get(i).getLocationId();
                            }
                        }
                        selectedLocationID = locationId;
                    }
                });
            }
        });

        txtAddProdcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String productName = etxtProductName.getText().toString();
                String productCode = etxtProductCode.getText().toString();
                String productCategoryName = etxtProductCategory.getText().toString();
                String productCategoryId = selectedCategoryID;
                String productDescription = etxtProductDescription.getText().toString();

                String productStock = etxtProductStock.getText().toString();

                String productSellPrice = etxtProductSellPrice.getText().toString();

                String productSupplierName = etxtProductSupplier.getText().toString();
                String productSupplierId = selectedSupplierID;
                String productWeightUnitName = etxtProdcutWeightUnit.getText().toString();
                String productWeightUnitId = selectedWeightUnitID;
                String productWeight = etxtProductWeight.getText().toString();
                String productLocationName = etxtProductLocation.getText().toString();
                String productLocationId = selectedLocationID;


                if (productName.isEmpty()) {
                    etxtProductName.setError(getString(R.string.product_name_cannot_be_empty));
                    etxtProductName.requestFocus();
                } else if (productCode.isEmpty()) {
                    etxtProductCode.setError(getString(R.string.product_code_cannot_be_empty));
                    etxtProductCode.requestFocus();
                } else if (productCategoryName.isEmpty() || productCategoryId.isEmpty()) {
                    etxtProductCategory.setError(getString(R.string.product_category_cannot_be_empty));
                    etxtProductCategory.requestFocus();
                } else if (productSellPrice.isEmpty()) {
                    etxtProductSellPrice.setError(getString(R.string.product_sell_price_cannot_be_empty));
                    etxtProductSellPrice.requestFocus();
                } else if (productWeightUnitName.isEmpty() || productWeight.isEmpty()) {
                    etxtProductWeight.setError(getString(R.string.product_weight_cannot_be_empty));
                    etxtProductWeight.requestFocus();
                } else if (productLocationName.isEmpty() || productLocationId.isEmpty()) {
                    etxtProductWeight.setError(getString(R.string.product_location_cannot_be_empty));
                    etxtProductWeight.requestFocus();
                } else {
                    addProduct(productName, productCode, productCategoryId, productDescription, productSellPrice,  productWeight,productWeightUnitId,productSupplierId,productStock, productLocationId);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            // When an Image is picked
            if (requestCode == 1213 && resultCode == RESULT_OK && null != data) {


                mediaPath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
                Bitmap selectedImage = BitmapFactory.decodeFile(mediaPath);
                imgProduct.setImageBitmap(selectedImage);


            }


        } catch (Exception e) {
            Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
        }

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







    // Uploading Image/Video
    private void  addProduct(String productName,String productCode,String productCategoryId,String productDescription, String productSellPrice, String productWeight,String productWeightUnitId,String productSupplierId,String productStock, String productLocationId) {

        loading=new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();

        String staffId = sp.getString(Constant.SP_STAFF_ID, "");
        String auth_token = sp.getString(Constant.SP_AUTH_TOKEN, "");
        Call<Product> call;

        // Map is used to multipart the file using okhttp3.RequestBody
        if(mediaPath != null) {
            ApiInterface getResponse = ApiClient.getApiClient_multipart().create(ApiInterface.class);
            File file = new File(mediaPath);
            // Parsing any Media type file
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), staffId);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), productName);
            RequestBody code = RequestBody.create(MediaType.parse("text/plain"), productCode);
            RequestBody category = RequestBody.create(MediaType.parse("text/plain"), productCategoryId);
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"),  productDescription);
            RequestBody sellPrice = RequestBody.create(MediaType.parse("text/plain"), productSellPrice);
            RequestBody weight = RequestBody.create(MediaType.parse("text/plain"), productWeight);
            RequestBody weightUnitId = RequestBody.create(MediaType.parse("text/plain"), productWeightUnitId);
            RequestBody supplierId = RequestBody.create(MediaType.parse("text/plain"), productSupplierId);
            RequestBody stock = RequestBody.create(MediaType.parse("text/plain"),productStock);
            RequestBody locationId = RequestBody.create(MediaType.parse("text/plain"),productLocationId);
            call = getResponse.addProduct(auth_token, userId, filename,name,code,category,description,sellPrice,weight,weightUnitId,supplierId,stock,locationId,fileToUpload);
        }
        else
        {
            ApiInterface getResponse = ApiClient.getApiClient().create(ApiInterface.class);
            call = getResponse.addProduct_without_file(auth_token, staffId, productName, productCode, productCategoryId, productDescription, productSellPrice, productWeight, productWeightUnitId, productSupplierId, productStock, productLocationId);
        }



        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {

                if (response.isSuccessful() && response.body() != null) {

                    loading.dismiss();
                    String value = response.body().getValue();
                    if (value.equals(Constant.KEY_SUCCESS)) {
                        Toasty.success(getApplicationContext(), R.string.product_successfully_added, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddProductActivity.this, ProductActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else if (value.equals(Constant.KEY_SUBSCRIBE)) {

                        loading.dismiss();
                        Toasty.error(AddProductActivity.this, R.string.please_subscribe, Toast.LENGTH_SHORT).show();
                        finish();

                    }
                    else if (value.equals(Constant.KEY_LIMIT)) {
                        loading.dismiss();
                        Toasty.error(AddProductActivity.this, R.string.production_limited_by_subscription, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else if (value.equals(Constant.KEY_FAILURE)) {

                            loading.dismiss();

                            Toasty.error(AddProductActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            loading.dismiss();
                            Toasty.error(AddProductActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        }

                }

                else
                {
                    loading.dismiss();
                    Log.d("Error",response.errorBody().toString());
                }

            }



            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                loading.dismiss();
                Log.d("Error! ", t.toString());
                Toasty.error(AddProductActivity.this, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
            }
        });
    }





    public void getProductCategory() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<List<Category>> call;

        String auth_token = sp.getString(Constant.SP_AUTH_TOKEN, "");
        String staffId = sp.getString(Constant.SP_STAFF_ID, "");
        call = apiInterface.getCategory(auth_token, staffId);

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {


                if (response.isSuccessful() && response.body() != null) {

                    productCategory = response.body();

                    categoryNames = new ArrayList<>();

                    for (int i = 0; i < productCategory.size(); i++) {

                        categoryNames.add(productCategory.get(i).getProductCategoryName());

                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {

                //write own action
            }
        });


    }



    public void getProductSuppliers() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<List<Suppliers>> call;

        String staffId = sp.getString(Constant.SP_STAFF_ID, "");
        String auth_token = sp.getString(Constant.SP_AUTH_TOKEN, "");
        call = apiInterface.getSuppliers(auth_token, staffId, "");

        call.enqueue(new Callback<List<Suppliers>>() {
            @Override
            public void onResponse(@NonNull Call<List<Suppliers>> call, @NonNull Response<List<Suppliers>> response) {


                if (response.isSuccessful() && response.body() != null) {

                    productSuppliers = response.body();

                    supplierNames = new ArrayList<>();

                    for (int i = 0; i < productSuppliers.size(); i++) {

                        supplierNames.add(productSuppliers.get(i).getSuppliersName());

                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<Suppliers>> call, @NonNull Throwable t) {

                //write own action
            }
        });


    }



    public void getWeightUnits() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<List<WeightUnit>> call;

        String auth_token = sp.getString(Constant.SP_AUTH_TOKEN, "");
        call = apiInterface.getWeightUnits(auth_token, "");

        call.enqueue(new Callback<List<WeightUnit>>() {
            @Override
            public void onResponse(@NonNull Call<List<WeightUnit>> call, @NonNull Response<List<WeightUnit>> response) {


                if (response.isSuccessful() && response.body() != null) {

                    weightUnits = response.body();

                    weightUnitNames = new ArrayList<>();

                    for (int i = 0; i < weightUnits.size(); i++) {

                        weightUnitNames.add(weightUnits.get(i).getWeightUnitName());

                    }



                }



            }

            @Override
            public void onFailure(@NonNull Call<List<WeightUnit>> call, @NonNull Throwable t) {

                //write own action
            }
        });


    }

    public void getBusinessLocations() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<List<BusinessLocation>> call;

        String auth_token = sp.getString(Constant.SP_AUTH_TOKEN, "");
        String staffId = sp.getString(Constant.SP_STAFF_ID, "");
        call = apiInterface.getBusinessLocations(auth_token, staffId, "");

        call.enqueue(new Callback<List<BusinessLocation>>() {
            @Override
            public void onResponse(@NonNull Call<List<BusinessLocation>> call, @NonNull Response<List<BusinessLocation>> response) {


                if (response.isSuccessful() && response.body() != null) {

                    businessLocations = response.body();

                    locationNames = new ArrayList<>();

                    for (int i = 0; i < businessLocations.size(); i++) {

                        locationNames.add(businessLocations.get(i).getLocationName());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BusinessLocation>> call, @NonNull Throwable t) {

                //write own action
            }
        });


    }


}



