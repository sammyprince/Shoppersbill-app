package com.app.onlinesmartpos.orders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.onlinesmartpos.Constant;
import com.app.onlinesmartpos.R;
import com.app.onlinesmartpos.adapter.OrderDetailsAdapter;
import com.app.onlinesmartpos.database.DatabaseAccess;
import com.app.onlinesmartpos.model.OrderDetails;
import com.app.onlinesmartpos.networking.ApiClient;
import com.app.onlinesmartpos.networking.ApiInterface;
import com.app.onlinesmartpos.pdf_report.BarCodeEncoder;
import com.app.onlinesmartpos.pdf_report.PDFTemplate;
import com.app.onlinesmartpos.utils.BaseActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends BaseActivity {


    ImageView imgNoProduct;
    TextView txtNoProducts, txtSubTotalPrice, txtTax, txtDiscount, txtTotalCost;
    String invoiceId,shopName, orderDate,orderTime, orderPrice, customerName, tax, discount,shopAddress,shopEmail,shopContact;
    double calculatedTotalPrice,getOrderPrice,getTax,getDiscount;

    Button btnPdfReceipt;
    List<OrderDetails> orderDetails;
    DecimalFormat f;

    //how many headers or column you need, add here by using ,
    //headers and get clients para meter must be equal
    private String[] header = {"Description", "Price"};


    String longText, shortText, userName;

    private PDFTemplate templatePDF;
    SharedPreferences sp;
    String currency;
    Bitmap bm = null;
    ProgressDialog loading;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        recyclerView = findViewById(R.id.recycler);
        imgNoProduct = findViewById(R.id.image_no_product);
        txtSubTotalPrice = findViewById(R.id.txt_total_price);
        txtTax = findViewById(R.id.txt_tax);
        txtDiscount = findViewById(R.id.txt_discount);
        txtTotalCost = findViewById(R.id.txt_total_cost);
        btnPdfReceipt = findViewById(R.id.btn_pdf_receipt);
        txtNoProducts = findViewById(R.id.txt_no_products);
        f = new DecimalFormat("#0.00");
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        shopName = sp.getString(Constant.SP_SHOP_NAME, "N/A");
        shopEmail = sp.getString(Constant.SP_SHOP_EMAIL, "N/A");
        shopContact = sp.getString(Constant.SP_SHOP_CONTACT, "N/A");
        shopAddress = sp.getString(Constant.SP_SHOP_ADDRESS, "N/A");
        userName = sp.getString(Constant.SP_STAFF_NAME, "N/A");
        currency = sp.getString(Constant.SP_CURRENCY_SYMBOL, "N/A");



        orderPrice = getIntent().getExtras().getString(Constant.ORDER_PRICE);
        tax = getIntent().getExtras().getString(Constant.TAX);
        orderDate = getIntent().getExtras().getString(Constant.ORDER_DATE);
        orderTime = getIntent().getExtras().getString(Constant.ORDER_TIME);
        discount = getIntent().getExtras().getString(Constant.DISCOUNT);
        invoiceId = getIntent().getExtras().getString(Constant.INVOICE_ID);
        customerName = getIntent().getExtras().getString(Constant.CUSTOMER_NAME);

        getProductsData(invoiceId);

        calculatedTotalPrice=Double.parseDouble(orderPrice)+Double.parseDouble(tax)-Double.parseDouble(discount);

        double totalPrice=Double.parseDouble(orderPrice)+Double.parseDouble(tax)-Double.parseDouble(discount);

        txtTotalCost.setText(getString(R.string.total_price)+": "+currency+f.format(totalPrice));


        imgNoProduct.setVisibility(View.GONE);
        txtNoProducts.setVisibility(View.GONE);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle(R.string.order_details);


        // set a GridLayoutManager with default vertical orientation and 3 number of columns
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        recyclerView.setHasFixedSize(true);


        getOrderPrice=Double.parseDouble(orderPrice);
        getTax=Double.parseDouble(tax);
        getDiscount=Double.parseDouble(discount);

        txtSubTotalPrice.setText(getString(R.string.sub_total)+": "+currency+f.format(getOrderPrice));
        txtTax.setText(getString(R.string.total_tax) + " : " + currency + f.format(getTax));
        txtDiscount.setText(getString(R.string.discount) + " : " + currency+ f.format(getDiscount));

        OrderDetailsAdapter.subTotalPrice=0;


        //for pdf report
        shortText = "Customer Name: Mr/Mrs. " + customerName;
        longText = "<<<<< Have a nice day :)  Visit again >>>>>";
        templatePDF = new PDFTemplate(getApplicationContext());


        BarCodeEncoder qrCodeEncoder = new BarCodeEncoder();
        try {
            bm = qrCodeEncoder.encodeAsBitmap(invoiceId, BarcodeFormat.CODE_128, 600, 300);
        } catch (WriterException e) {
            Log.d("Data", e.toString());
        }


        btnPdfReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                templatePDF.openDocument();
                templatePDF.addMetaData(Constant.ORDER_RECEIPT, Constant.ORDER_RECEIPT, "Smart POS");
                templatePDF.addTitle(shopName, shopAddress+ "\n Email: " + shopEmail + "\nContact: " + shopContact + "\nInvoice ID:" + invoiceId, orderDate + " " + orderTime+"\nServed By: "+userName);
                templatePDF.addParagraph(shortText);

                templatePDF.createTable(header, getPDFReceipt());
                templatePDF.addImage(bm);

                templatePDF.addRightParagraph(longText);

                templatePDF.closeDocument();
                templatePDF.viewPDF();


            }
        });


    }


    //for pdf
    private ArrayList<String[]> getPDFReceipt() {
        ArrayList<String[]> rows = new ArrayList<>();

        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(OrderDetailsActivity.this);
        databaseAccess.open();

        String name, price, qty, weight;
        double costTotal;

        for (int i = 0; i < orderDetails.size(); i++) {

            name = orderDetails.get(i).getProductName();

            price = orderDetails.get(i).getProductPrice();
            qty = orderDetails.get(i).getProductQuantity();
            weight = orderDetails.get(i).getProductWeight();

            costTotal = Integer.parseInt(qty) * Double.parseDouble(price);

            rows.add(new String[]{name + "\n" + weight + "\n" + "(" + qty + "x" + currency + price + ")", currency + f.format(costTotal)});


        }
        rows.add(new String[]{"..........................................", ".................................."});
        rows.add(new String[]{"Sub Total: ", "(+)"+currency + f.format(getOrderPrice)});
        rows.add(new String[]{"Total Tax: ", "(+)"+currency + f.format(getTax)});
        rows.add(new String[]{"Discount: ", "(-)"+currency + f.format(getTax)});
        rows.add(new String[]{"..........................................", ".................................."});
        rows.add(new String[]{"Total Price: ", currency + f.format(calculatedTotalPrice)});

//        you can add more row above format
        return rows;
    }





    public void getProductsData(String invoiceId) {


        loading=new ProgressDialog(OrderDetailsActivity.this);
        loading.setCancelable(false);
        loading.setMessage(getString(R.string.please_wait));
        loading.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<OrderDetails>> call;
        String auth_token = sp.getString(Constant.SP_AUTH_TOKEN, "");
        call = apiInterface.OrderDetailsByInvoice(auth_token, invoiceId);

        call.enqueue(new Callback<List<OrderDetails>>() {
            @Override
            public void onResponse(@NonNull Call<List<OrderDetails>> call, @NonNull Response<List<OrderDetails>> response) {


                if (response.isSuccessful() && response.body() != null) {

                    orderDetails = response.body();
                    loading.dismiss();


                    if (orderDetails.isEmpty()) {


                        Toasty.warning(OrderDetailsActivity.this, R.string.no_product_found, Toast.LENGTH_SHORT).show();


                    } else {

                        OrderDetailsAdapter orderDetailsAdapter = new OrderDetailsAdapter(OrderDetailsActivity.this, orderDetails);

                        recyclerView.setAdapter(orderDetailsAdapter);




                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<OrderDetails>> call, @NonNull Throwable t) {

                loading.dismiss();
                Toast.makeText(OrderDetailsActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
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

