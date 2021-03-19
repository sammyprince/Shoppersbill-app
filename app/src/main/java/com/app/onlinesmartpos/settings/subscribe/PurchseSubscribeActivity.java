package com.app.onlinesmartpos.settings.subscribe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.app.onlinesmartpos.Constant;
import com.app.onlinesmartpos.R;
import com.app.onlinesmartpos.adapter.ExpenseAdapter;
import com.app.onlinesmartpos.expense.ExpenseActivity;
import com.app.onlinesmartpos.model.Subscribe;
import com.app.onlinesmartpos.model.Package;
import com.app.onlinesmartpos.model.Subscribe;
import com.app.onlinesmartpos.networking.ApiClient;
import com.app.onlinesmartpos.networking.ApiInterface;
import com.app.onlinesmartpos.product.EditProductActivity;
import android.app.ProgressDialog;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;

import java.util.List;

import es.dmoral.toasty.Toasty;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchseSubscribeActivity extends AppCompatActivity {

    TextView txt_name, txt_bl, txt_user, txt_product, txt_invoice, txt_td, txt_price, txt_interval, txt_description;
    Button btn_subscribe;
    SharedPreferences sp;
    Package packageData;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_subscribe);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.purchase_subscribe);
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        txt_name = findViewById(R.id.subscribe_name);
        txt_bl = findViewById(R.id.subscribe_business_location);
        txt_user = findViewById(R.id.subscribe_user);
        txt_product = findViewById(R.id.subscribe_product);
        txt_invoice = findViewById(R.id.subscribe_invoice);
        txt_td = findViewById(R.id.subscribe_trial_day);
        txt_price = findViewById(R.id.subscribe_price);
        txt_interval = findViewById(R.id.subscribe_interval);
        txt_description = findViewById(R.id.subscribe_description);
        btn_subscribe = findViewById(R.id.subscribe_subscribe);

        packageData = (Package)getIntent().getSerializableExtra(Constant.KEY_PACKAGE_DATA);

        txt_name.setText(packageData.getPackageName());
        txt_bl.setText(packageData.getPackageLocationCount() + " Business Locations");
        txt_user.setText(packageData.getPackageUserCount() + " Users");
        txt_product.setText(packageData.getPackageProductCount() + " Products");
        txt_invoice.setText(packageData.getPackageInvoiceCount() + " Invoices");
        txt_td.setText(packageData.getPackageTrialDays() + " Trial Days");
        String strCurrency = sp.getString(Constant.SP_CURRENCY_SYMBOL, "₦");
        txt_price.setText(packageData.getPackagePrice() == "0" ? "Free" : strCurrency + packageData.getPackagePrice());
        txt_interval.setText("/" + packageData.getPackageInterval());
        txt_description.setText(packageData.getPackageDescription());

        btn_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RaveUiManager(PurchseSubscribeActivity.this)
                        .setAmount(Double.parseDouble(packageData.getPackagePrice()))
                        .setEmail("Tuktarov2121@gmail.com")
                        .setCountry("Nigeria")
                        .setCurrency("USD")
                        .setfName("Tuktarov")
                        .setlName("Denis")
                        .setNarration("Purchase Subscription")
                        .setPublicKey("FLWPUBK_TEST-ae9bb6dd6b10e7325b1e00ac76a941d8-X")
                        .setEncryptionKey("FLWSECK_TESTcbe173cd539d")
                        .setTxRef(System.currentTimeMillis() + "Ref")
                        .acceptAccountPayments(true)
                        .acceptCardPayments(true)
                        .acceptMpesaPayments(true)
                        .onStagingEnv(true)
                        .shouldDisplayFee(true)
                        .showStagingLabel(true)
                        .initialize();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, "SUCCESS " + message, Toast.LENGTH_LONG).show();
            }
            else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR " + message, Toast.LENGTH_LONG).show();
            }
            else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "CANCELLED " + message, Toast.LENGTH_LONG).show();
            }
        }
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
