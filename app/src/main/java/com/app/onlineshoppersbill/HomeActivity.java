package com.app.onlineshoppersbill;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.app.onlineshoppersbill.about.AboutActivity;
import com.app.onlineshoppersbill.adapter.ExpenseAdapter;
import com.app.onlineshoppersbill.customers.CustomersActivity;
import com.app.onlineshoppersbill.expense.ExpenseActivity;
import com.app.onlineshoppersbill.login.LoginActivity;
import com.app.onlineshoppersbill.model.Expense;
import com.app.onlineshoppersbill.model.ExpenseReport;
import com.app.onlineshoppersbill.model.SalesReport;
import com.app.onlineshoppersbill.networking.ApiClient;
import com.app.onlineshoppersbill.networking.ApiInterface;
import com.app.onlineshoppersbill.orders.OrdersActivity;
import com.app.onlineshoppersbill.pos.PosActivity;
import com.app.onlineshoppersbill.product.ProductActivity;
import com.app.onlineshoppersbill.report.ExpenseReportActivity;
import com.app.onlineshoppersbill.report.ReportActivity;
import com.app.onlineshoppersbill.report.SalesReportActivity;
import com.app.onlineshoppersbill.settings.SettingsActivity;
import com.app.onlineshoppersbill.suppliers.SuppliersActivity;
import com.app.onlineshoppersbill.utils.BaseActivity;
import com.app.onlineshoppersbill.utils.LocaleManager;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.text.DecimalFormat;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype.Slidetop;

public class HomeActivity extends BaseActivity {


    CardView cardCustomers, cardProducts, cardSupplier, cardPos, cardOrderList, cardReport, cardSettings, cardExpense, cardAbout, cardLogout;
    //for double back press to exit
    private static final int TIME_DELAY = 2000;
    private static long backPressed;
    ImageView imgAvatar;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String userType;
    TextView txtTotalSale, txtTotalPrice, txtTotalExpense, txtTotalTax;

    @Override
    public void onActivityReenter(int resultCode, Intent data){
        updateSales();
        updateExpense();
    }

    private void updateSales(){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<SalesReport> call;
        String staffId = sp.getString(Constant.SP_STAFF_ID, "");
        String auth_token = sp.getString(Constant.SP_AUTH_TOKEN, "");
        call = apiInterface.getSalesReport(auth_token, staffId, "all");
        call.enqueue(new Callback<SalesReport>() {
            @Override
            public void onResponse(@NonNull Call<SalesReport> call, @NonNull Response<SalesReport> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SalesReport salesReport = response.body();
                    if (salesReport != null) {
                        String totalOrderPrice=salesReport.getTotalOrderPrice();
                        String totalTax=salesReport.getTotalTax();
                        String totalDiscount=salesReport.getTotalDiscount();
                        if (totalOrderPrice!=null) {


                            double orderPrice = Double.parseDouble(totalOrderPrice);
                            double getTax = Double.parseDouble(totalTax);
                            double getDiscount = Double.parseDouble(totalDiscount);
                            double netSales = orderPrice + getTax - getDiscount;

                            DecimalFormat f = new DecimalFormat("#0.00");
                            String currency = sp.getString(Constant.SP_CURRENCY_SYMBOL, "N/A");
                            txtTotalPrice.setText(getString(R.string.total_price) + "\n" + currency + f.format(orderPrice));
                            txtTotalTax.setText(getString(R.string.total_tax) + " :\n" + currency +f.format(getTax));
                            txtTotalSale.setText(getString(R.string.total_sales) + "\n" + currency + f.format(netSales));
                        }
                    }

                }
            }
            @Override
            public void onFailure(@NonNull Call<SalesReport> call, @NonNull Throwable t) {
            }
        });

    }

    private void updateExpense(){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ExpenseReport> call;
        String staffId = sp.getString(Constant.SP_STAFF_ID, "");
        String auth_token = sp.getString(Constant.SP_AUTH_TOKEN, "");
        call = apiInterface.getExpenseReport(auth_token, staffId, "all");
        call.enqueue(new Callback<ExpenseReport>() {
            @Override
            public void onResponse(@NonNull Call<ExpenseReport> call, @NonNull Response<ExpenseReport> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ExpenseReport expenseReports;
                    expenseReports = response.body();
                    if (expenseReports != null) {
                        String totalExpense = expenseReports.getTotalExpensePrice();
                        if (totalExpense!=null) {
                            DecimalFormat f = new DecimalFormat("#0.00");
                            String currency = sp.getString(Constant.SP_CURRENCY_SYMBOL, "N/A");
                            txtTotalExpense.setText(getString(R.string.total_expense) + "\n"+currency + totalExpense);
                        }
                    }
                 }
            }
            @Override
            public void onFailure(@NonNull Call<ExpenseReport> call, @NonNull Throwable t) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
        getSupportActionBar().setElevation(0);


        cardCustomers = findViewById(R.id.card_customers);
        cardSupplier = findViewById(R.id.card_suppliers);
        cardProducts = findViewById(R.id.card_products);
        cardPos = findViewById(R.id.card_pos);
        cardOrderList = findViewById(R.id.card_all_orders);
        cardReport = findViewById(R.id.card_reports);
        cardSettings = findViewById(R.id.card_settings);
        cardExpense = findViewById(R.id.card_expense);
        cardAbout = findViewById(R.id.card_about_us);
        cardLogout = findViewById(R.id.card_logout);
        imgAvatar = findViewById(R.id.avatarView);
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        txtTotalExpense = findViewById(R.id.txtTotalExpense);
        txtTotalSale = findViewById(R.id.txtTotalSale);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtTotalTax = findViewById(R.id.txtTotalTax);
        imgAvatar.setClipToOutline(true);
        editor = sp.edit();

        userType = sp.getString(Constant.SP_USER_TYPE, "");
        String shopName = sp.getString(Constant.SP_SHOP_NAME, "");
        String staffName = sp.getString(Constant.SP_STAFF_NAME, "");



        if (Build.VERSION.SDK_INT >= 23) //Android MarshMellow Version or above
        {
            requestPermission();

        }

        cardCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CustomersActivity.class);
                startActivity(intent);


            }
        });

        cardSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SuppliersActivity.class);
                startActivity(intent);


            }
        });


        cardProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
                startActivity(intent);


            }
        });


        cardPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PosActivity.class);
                startActivity(intent);


            }
        });

        cardOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, OrdersActivity.class);
                startActivity(intent);


            }
        });


        cardReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (userType.equals(Constant.ADMIN) || userType.equals(Constant.MANAGER)) {
                    Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
                    startActivity(intent);
//                } else {
//                    Toasty.error(HomeActivity.this, R.string.you_dont_have_permission_to_access_this_page, Toast.LENGTH_SHORT).show();
//                }

            }
        });


        cardExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (userType.equals(Constant.ADMIN) || userType.equals(Constant.MANAGER)) {
                    Intent intent = new Intent(HomeActivity.this, ExpenseActivity.class);
                    startActivity(intent);
//                } else {
//                    Toasty.error(HomeActivity.this, R.string.you_dont_have_permission_to_access_this_page, Toast.LENGTH_SHORT).show();
//                }
            }
        });


        cardSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (userType.equals(Constant.ADMIN)) {
                    Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                    startActivity(intent);
//                } else {
//                    Toasty.error(HomeActivity.this, R.string.you_dont_have_permission_to_access_this_page, Toast.LENGTH_SHORT).show();
//                }
            }
        });


        cardAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(intent);

            }
        });


        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(HomeActivity.this);
                dialogBuilder
                        .withTitle(getString(R.string.logout))
                        .withMessage(R.string.want_to_logout_from_app)
                        .withEffect(Slidetop)
                        .withDialogColor("#2979ff") //use color code for dialog
                        .withButton1Text(getString(R.string.yes))
                        .withButton2Text(getString(R.string.cancel))
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editor.putString(Constant.SP_PHONE, "");
                                editor.putString(Constant.SP_PASSWORD, "");
                                editor.putString(Constant.SP_USER_NAME, "");
                                editor.putString(Constant.SP_USER_TYPE, "");
                                editor.apply();

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                                dialogBuilder.dismiss();
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialogBuilder.dismiss();
                            }
                        })
                        .show();


            }
        });

        updateSales();
        updateExpense();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.language_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {


            case R.id.local_french:
                setNewLocale(this, LocaleManager.FRENCH);
                return true;


            case R.id.local_english:
                setNewLocale(this, LocaleManager.ENGLISH);
                return true;


            case R.id.local_bangla:
                setNewLocale(this, LocaleManager.BANGLA);
                return true;

            case R.id.local_spanish:
                setNewLocale(this, LocaleManager.SPANISH);
                return true;
            default:
                Log.d("Default", "default");

        }

        return super.onOptionsItemSelected(item);
    }


    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(this, language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    //double back press to exit
    @Override
    public void onBackPressed() {
        if (backPressed + TIME_DELAY > System.currentTimeMillis()) {

            finishAffinity();

        } else {
            Toasty.info(this, R.string.press_once_again_to_exit,
                    Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }


    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            //write your action if needed
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }


                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }
}
