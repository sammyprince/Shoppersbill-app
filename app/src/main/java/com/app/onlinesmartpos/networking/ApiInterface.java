package com.app.onlinesmartpos.networking;


import com.app.onlinesmartpos.Constant;
import com.app.onlinesmartpos.model.Category;
import com.app.onlinesmartpos.model.Customer;
import com.app.onlinesmartpos.model.Expense;
import com.app.onlinesmartpos.model.ExpenseReport;
import com.app.onlinesmartpos.model.Login;
import com.app.onlinesmartpos.model.MonthData;
import com.app.onlinesmartpos.model.OrderDetails;
import com.app.onlinesmartpos.model.OrderList;
import com.app.onlinesmartpos.model.Product;
import com.app.onlinesmartpos.model.SalesReport;
import com.app.onlinesmartpos.model.ShopInformation;
import com.app.onlinesmartpos.model.Suppliers;
import com.app.onlinesmartpos.model.WeightUnit;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {



    //for login
    @FormUrlEncoded
    @POST("login")
    Call<Login> login(
            @Field(Constant.KEY_EMAIL) String email,
            @Field(Constant.KEY_PASSWORD) String password);



    //calling json array , need list
    @POST("order")
    Call<String> submitOrders(
            @Body RequestBody ordersData
    );



    //get customers data
    @GET("customers/list")
    Call<List<Customer>> getCustomers(
            @Query(Constant.SP_STAFF_ID) String staffId,
            @Query(Constant.SEARCH_TEXT) String searchText

    );


    //get customers data
    @GET("orders")
    Call<List<OrderList>> getOrders(
            @Query(Constant.SEARCH_TEXT) String searchText

    );


    //get customers data
    @GET("products/list")
    Call<List<Product>> getProducts(
            @Query(Constant.SP_STAFF_ID) String staffId,
            @Query(Constant.SEARCH_TEXT) String searchText
    );


    //get product data
    @GET("products/item")
    Call<Product> getProductById(
            @Query(Constant.PRODUCT_ID) String productId
    );



    //get order data
    @GET("order_details_by_invoice")
    Call<List<OrderDetails>> OrderDetailsByInvoice(
            @Query(Constant.INVOICE_ID) String invoiceId

    );

    //get order data
    @GET("sales_report_list")
    Call<List<OrderDetails>> getReportList(
            @Query(Constant.KEY_TYPE) String type

    );


    //get order data
    @GET("shop_information")
    Call<List<ShopInformation>> shopInformation(
            @Query(Constant.SHOP_ID) String shopId

    );



    //get order data
    @GET("sales_report")
    Call<List<SalesReport>> getSalesReport(

            @Query(Constant.KEY_TYPE) String type
    );



    //get expense data
    @GET("expense_report")
    Call<List<ExpenseReport>> getExpenseReport(

            @Query(Constant.KEY_TYPE) String type
    );


    //for monthly expense data
    @GET("monthly_expenses")
    Call<List<MonthData>> getMonthlyExpense();


    //for monthly sales data
    @GET("monthly_sales")
    Call<List<MonthData>> getMonthlySales();



    //for category data
    @GET("category")
    Call<List<Category>> getCategory();



    //for product data
    @GET("products/list_category")
    Call<List<Product>> searchProductByCategory(
            @Query(Constant.SP_STAFF_ID) String staffId,
            @Query(Constant.KEY_CATEGORY_ID) String categoryId
    );




    //add customer data to server
    @FormUrlEncoded
    @POST("customers/add_item")
    Call<Customer> addCustomer(
            @Field(Constant.SP_STAFF_ID) String staffId,
            @Field(Constant.CUSTOMER_NAME) String name,
            @Field(Constant.CUSTOMER_CELL) String cell,
            @Field(Constant.CUSTOMER_EMAIL) String email,
            @Field(Constant.CUSTOMER_ADDRESS) String address);


    //add expense data to server
    @FormUrlEncoded
    @POST("add_expense")
    Call<Expense> addExpense(
            @Field(Constant.EXPENSE_NAME) String name,
            @Field(Constant.EXPENSE_AMOUNT) String amount,
            @Field(Constant.EXPENSE_NOTE) String note,
            @Field(Constant.EXPENSE_DATE) String date,
            @Field(Constant.EXPENSE_TIME) String time);


    //update expense data to server
    @FormUrlEncoded
    @POST("update_expense")
    Call<Expense> updateExpense(
            @Field(Constant.EXPENSE_ID) String id,
            @Field(Constant.EXPENSE_NAME) String name,
            @Field(Constant.EXPENSE_AMOUNT) String amount,
            @Field(Constant.EXPENSE_NOTE) String note,
            @Field(Constant.EXPENSE_DATE) String date,
            @Field(Constant.EXPENSE_TIME) String time);


    //add suppliers data to server
    @FormUrlEncoded
    @POST("suppliers/add_item")
    Call<Suppliers> addSupplier(
            @Field(Constant.SP_STAFF_ID) String staffId,
            @Field(Constant.SUPPLIERS_NAME) String name,
            @Field(Constant.SUPPLIERS_CONTACT_PERSON) String contactPerson,
            @Field(Constant.SUPPLIERS_CELL) String cell,
            @Field(Constant.SUPPLIERS_EMAIL) String email,
            @Field(Constant.SUPPLIERS_ADDRESS) String address);


    //add suppliers data to server
    @FormUrlEncoded
    @POST("suppliers/update_item")
    Call<Suppliers> updateSupplier(
            @Field(Constant.SUPPLIERS_ID) String suppliersId,
            @Field(Constant.SUPPLIERS_NAME) String name,
            @Field(Constant.SUPPLIERS_CONTACT_PERSON) String contactPerson,
            @Field(Constant.SUPPLIERS_CELL) String cell,
            @Field(Constant.SUPPLIERS_EMAIL) String email,
            @Field(Constant.SUPPLIERS_ADDRESS) String address);


    //update customer data to server
    @FormUrlEncoded
    @POST("customers/update_item")
    Call<Customer> updateCustomer(
            @Field(Constant.CUSTOMER_ID) String id,
            @Field(Constant.CUSTOMER_NAME) String name,
            @Field(Constant.CUSTOMER_CELL) String cell,
            @Field(Constant.CUSTOMER_EMAIL) String email,
            @Field(Constant.CUSTOMER_ADDRESS) String address);


    //delete customer
    @FormUrlEncoded
    @POST("customers/delete_item")
    Call<Customer> deleteCustomer(
            @Field(Constant.CUSTOMER_ID) String customerId
    );


    //delete customer
    @FormUrlEncoded
    @POST("delete_order")
    Call<OrderList> deleteOrder(
            @Field(Constant.INVOICE_ID) String invoiceId
    );


    //delete product
    @FormUrlEncoded
    @POST("delete_product")
    Call<Product> deleteProduct(
            @Field(Constant.PRODUCT_ID) String productId
    );


    //delete customer
    @FormUrlEncoded
    @POST("delete_expense")
    Call<Expense> deleteExpense(
            @Field(Constant.EXPENSE_ID) String expenseId
    );


    //delete supplier
    @FormUrlEncoded
    @POST("suppliers/delete_item")
    Call<Suppliers> deleteSupplier(
            @Field(Constant.SUPPLIERS_ID) String suppliersId
    );


    //get suppliers data
    @GET("suppliers/list")
    Call<List<Suppliers>> getSuppliers(
            @Query(Constant.SP_STAFF_ID) String staffId,
            @Query(Constant.SEARCH_TEXT) String searchText
    );


    //get weight unit
    @GET("weight_units")
    Call<List<WeightUnit>> getWeightUnits(
            @Query(Constant.SEARCH_TEXT) String searchText
    );


    //for upload image and info
    @Multipart
    @POST("add_product")
    Call<Product> addProduct(@Part MultipartBody.Part file,
                             @Part(Constant.KEY_FILE) RequestBody name,
                             @Part(Constant.PRODUCT_NAME) RequestBody productName,
                             @Part(Constant.PRODUCT_CODE) RequestBody productCode,
                             @Part(Constant.CATEGORY_ID) RequestBody categoryId,
                             @Part(Constant.PRODUCT_DESCRIPTION) RequestBody description,
                             @Part(Constant.PRODUCT_SELL_PRICE) RequestBody sellPrice,
                             @Part(Constant.PRODUCT_WEIGHT) RequestBody weight,
                             @Part(Constant.PRODUCT_WEIGHT_UNIT_ID) RequestBody weightUnitId,
                             @Part(Constant.SUPPLIERS_ID) RequestBody supplierId,
                             @Part(Constant.PRODUCT_STOCK) RequestBody stock);


    //for upload image and info
    @Multipart
    @POST("products/update_product")
    Call<Product> updateProduct(@Part MultipartBody.Part file,
                                @Part(Constant.KEY_FILE) RequestBody name,
                                @Part(Constant.PRODUCT_NAME) RequestBody productName,
                                @Part(Constant.PRODUCT_CODE) RequestBody productCode,
                                @Part(Constant.CATEGORY_ID) RequestBody categoryId,
                                @Part(Constant.PRODUCT_DESCRIPTION) RequestBody description,
                                @Part(Constant.PRODUCT_SELL_PRICE) RequestBody sellPrice,
                                @Part(Constant.PRODUCT_WEIGHT) RequestBody weight,
                                @Part(Constant.PRODUCT_WEIGHT_UNIT_ID) RequestBody weightUnitId,
                                @Part(Constant.SUPPLIERS_ID) RequestBody supplierId,
                                @Part(Constant.PRODUCT_STOCK) RequestBody stock,
                                @Part(Constant.PRODUCT_ID) RequestBody product_id);


    //for upload image and info
    @Multipart
    @POST("products/update_product_without_image")
    Call<Product> updateProductWithoutImage(

            @Part(Constant.PRODUCT_NAME) RequestBody productName,
            @Part(Constant.PRODUCT_CODE) RequestBody productCode,
            @Part(Constant.CATEGORY_ID) RequestBody categoryId,
            @Part(Constant.PRODUCT_DESCRIPTION) RequestBody description,
            @Part(Constant.PRODUCT_SELL_PRICE) RequestBody sellPrice,
            @Part(Constant.PRODUCT_WEIGHT) RequestBody weight,
            @Part(Constant.PRODUCT_WEIGHT_UNIT_ID) RequestBody weightUnitId,
            @Part(Constant.SUPPLIERS_ID) RequestBody supplierId,
            @Part(Constant.PRODUCT_STOCK) RequestBody stock,
            @Part(Constant.PRODUCT_ID) RequestBody productId);


    //get expense data
    @GET("expenses")
    Call<List<Expense>> getExpense(
            @Query(Constant.SEARCH_TEXT) String searchText

    );



    //get expense data
    @GET("all_expense")
    Call<List<Expense>> getAllExpense(
            @Query(Constant.KEY_TYPE) String type

    );


}