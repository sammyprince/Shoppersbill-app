package com.app.onlineshoppersbill.settings.subscribe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.app.onlineshoppersbill.Constant;
import com.app.onlineshoppersbill.R;
import com.app.onlineshoppersbill.model.Package;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.rave_java_commons.Meta;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
                ArrayList meta = new ArrayList();
                meta.add(new Meta("gateway", "Flutterwave"));
                meta.add(new Meta("user_id", sp.getString(Constant.SP_STAFF_ID, "0")));
                meta.add(new Meta("package_id", packageData.getPackageId()));
                new RaveUiManager(PurchseSubscribeActivity.this)
                        .setAmount(Double.parseDouble(packageData.getPackagePrice()))
                        .setEmail(sp.getString(Constant.SP_SHOP_EMAIL, ""))
                        .setCurrency(sp.getString(Constant.SP_CURRENCY_NAME, ""))
                        .setfName(sp.getString(Constant.SP_SHOP_NAME, ""))
                        .setNarration("Purchase Subscription")
                        .setPublicKey(sp.getString(Constant.SP_PUBLIC_KEY, ""))
                        .setEncryptionKey(sp.getString(Constant.SP_ENCRYPT_KEY, ""))
                        .setTxRef(System.currentTimeMillis() + "Ref")
                        .setMeta(meta)
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
