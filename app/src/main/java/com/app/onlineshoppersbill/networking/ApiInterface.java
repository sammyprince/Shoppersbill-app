package com.app.onlineshoppersbill.networking;


import com.app.onlineshoppersbill.Constant;
import com.app.onlineshoppersbill.model.Category;
import com.app.onlineshoppersbill.model.Package;
import com.app.onlineshoppersbill.model.Customer;
import com.app.onlineshoppersbill.model.Expense;
import com.app.onlineshoppersbill.model.ExpenseReport;
import com.app.onlineshoppersbill.model.Login;
import com.app.onlineshoppersbill.model.MonthData;
import com.app.onlineshoppersbill.model.OrderDetails;
import com.app.onlineshoppersbill.model.OrderList;
import com.app.onlineshoppersbill.model.Product;
import com.app.onlineshoppersbill.model.RegisterBaseInfo;
import com.app.onlineshoppersbill.model.RegisterInfo;
import com.app.onlineshoppersbill.model.SalesReport;
import com.app.onlineshoppersbill.model.ShopInformation;
import com.app.onlineshoppersbill.model.Suppliers;
import com.app.onlineshoppersbill.model.WeightUnit;
import com.app.onlineshoppersbill.model.Subscribe;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
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


    //for register
    @GET("register_base_info")
    Call<RegisterBaseInfo> getRegisterBaseInfo();

    @FormUrlEncoded
    @POST("register")
    Call<RegisterInfo> registerUser(
            @Field("name") String businessName,
            @Field("email") String email,
            @Field("country") String country,
            @Field("currency") String currency,
            @Field("time_zone") String timezone,
            @Field("first_name") String firstName,
            @Field("username") String userName,
            @Field("password") String password
    );

    //calling json array , need list
    @FormUrlEncoded
    @POST("orders/add_item")
    Call<String> submitOrders(
            @Header("Authorization") String auth,
            @Field(Constant.SP_STAFF_ID) String staffId,
            @Field("order_data") String ordersData
    );

    //get customers data
    @GET("customers/list")
    Call<List<Customer>> getCustomers(
            @Query(Constant.SP_STAFF_ID) String staffId,
            @Query(Constant.SEARCH_TEXT) String searchText,
            @Header("Authorization") String auth
    );


    //get customers data
    
    @GET("orders/list")
    Call<List<OrderList>> getOrders(
            @Header("Authorization") String auth,
            @Query(Constant.SP_STAFF_ID) String staffId,
            @Query(Constant.SEARCH_TEXT) String searchText
    );


    //get customers data
    @GET("products/list")
    Call<List<Product>> getProducts(
            @Header("Authorization") String auth,
            @Query(Constant.SP_STAFF_ID) String staffId,
            @Query(Constant.SEARCH_TEXT) String searchText
    );


    //get product data
    
    @GET("products/item")
    Call<Product> getProductById(
            @Header("Authorization") String auth,
            @Query(Constant.PRODUCT_ID) String productId
    );



    
    @GET("orders/list_invoice")
    Call<List<OrderDetails>> OrderDetailsByInvoice(
            @Header("Authorization") String auth,
            @Query(Constant.INVOICE_ID) String invoiceId
    );

    
    @GET("reports/sales_list")
    Call<List<OrderDetails>> getReportList(
            @Header("Authorization") String auth,
            @Query(Constant.SP_STAFF_ID) String staffId,
            @Query(Constant.KEY_TYPE) String type
    );


    
    @GET("shop_info")
    Call<ShopInformation> shopInformation(
            @Header("Authorization") String auth,
            @Query(Constant.SP_STAFF_ID) String staffId
    );



    
    @GET("reports/sales_total")
    Call<SalesReport> getSalesReport(
            @Header("Authorization") String auth,
            @Query(Constant.SP_STAFF_ID) String staffId,
            @Query(Constant.KEY_TYPE) String type
    );



    //get expense data
    
    @GET("reports/expense_total")
    Call<ExpenseReport> getExpenseReport(
            @Header("Authorization") String auth,
            @Query(Constant.SP_STAFF_ID) String staffId,
            @Query(Constant.KEY_TYPE) String type
    );


    //for monthly expense data
    
    @GET("reports/monthly_expenses")
    Call<MonthData> getMonthlyExpense(
            @Header("Authorization") String auth,
            @Query(Constant.SP_STAFF_ID) String staffId
    );


    //for monthly sales data
    
    @GET("reports/monthly_sales")
    Call<MonthData> getMonthlySales(
            @Header("Authorization") String auth,
            @Query(Constant.SP_STAFF_ID) String staffId
    );



    //for category data
    
    @GET("category")
    Call<List<Category>> getCategory(
            @Header("Authorization") String auth
    );

    @GET("pricing/packages")
    Call<List<Package>> getPackages(
            @Header("Authorization") String auth
    );



    //for product data
    
    @GET("products/list_category")
    Call<List<Product>> searchProductByCategory(
            @Header("Authorization") String auth,
            @Query(Constant.SP_STAFF_ID) String staffId,
            @Query(Constant.KEY_CATEGORY_ID) String categoryId
    );




    //add customer data to server
    @FormUrlEncoded
    @POST("customers/add_item")
    Call<Customer> addCustomer(
            @Header("Authorization") String auth,
            @Field(Constant.SP_STAFF_ID) String staffId,
            @Field(Constant.CUSTOMER_NAME) String name,
            @Field(Constant.CUSTOMER_CELL) String cell,
            @Field(Constant.CUSTOMER_EMAIL) String email,
            @Field(Constant.CUSTOMER_ADDRESS) String address);

    //add customer data to server
    @FormUrlEncoded
    @POST("pricing/purchase")
    Call<Subscribe> purchaseSubscribe(
            @Header("Authorization") String auth,
            @Field(Constant.SP_STAFF_ID) String staffId);


    //add expense data to server
    @FormUrlEncoded
    @POST("expenses/add_item")
    Call<Expense> addExpense(
            @Header("Authorization") String auth,
            @Field(Constant.SP_STAFF_ID) String staffId,
            @Field(Constant.EXPENSE_NAME) String name,
            @Field(Constant.EXPENSE_AMOUNT) String amount,
            @Field(Constant.EXPENSE_NOTE) String note,
            @Field(Constant.EXPENSE_DATE) String date,
            @Field(Constant.EXPENSE_TIME) String time);


    //update expense data to server
    @FormUrlEncoded
    @POST("expenses/update_item")
    Call<Expense> updateExpense(
            @Header("Authorization") String auth,
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
            @Header("Authorization") String auth,
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
            @Header("Authorization") String auth,
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
            @Header("Authorization") String auth,
            @Field(Constant.CUSTOMER_ID) String id,
            @Field(Constant.CUSTOMER_NAME) String name,
            @Field(Constant.CUSTOMER_CELL) String cell,
            @Field(Constant.CUSTOMER_EMAIL) String email,
            @Field(Constant.CUSTOMER_ADDRESS) String address);


    //delete customer
    @FormUrlEncoded
    @POST("customers/delete_item")
    Call<Customer> deleteCustomer(
            @Header("Authorization") String auth,
            @Field(Constant.CUSTOMER_ID) String customerId
    );


    //delete customer
    @FormUrlEncoded
    @POST("orders/delete_item")
    Call<OrderList> deleteOrder(
            @Header("Authorization") String auth,
            @Field(Constant.SP_STAFF_ID) String staffId,
            @Field(Constant.INVOICE_ID) String invoiceId
    );


    //delete product
    @FormUrlEncoded
    @POST("products/delete_item")
    Call<Product> deleteProduct(
            @Header("Authorization") String auth,
            @Field(Constant.SP_STAFF_ID) String staffId,
            @Field(Constant.PRODUCT_ID) String productId
    );


    //delete customer
    @FormUrlEncoded
    @POST("expenses/delete_item")
    Call<Expense> deleteExpense(
            @Header("Authorization") String auth,
            @Field(Constant.EXPENSE_ID) String expenseId
    );


    //delete supplier
    @FormUrlEncoded
    @POST("suppliers/delete_item")
    Call<Suppliers> deleteSupplier(
            @Header("Authorization") String auth,
            @Field(Constant.SUPPLIERS_ID) String suppliersId
    );


    //get suppliers data
    
    @GET("suppliers/list")
    Call<List<Suppliers>> getSuppliers(
            @Header("Authorization") String auth,
            @Query(Constant.SP_STAFF_ID) String staffId,
            @Query(Constant.SEARCH_TEXT) String searchText
    );


    //get weight unit
    
    @GET("weight_units")
    Call<List<WeightUnit>> getWeightUnits(
            @Header("Authorization") String auth,
            @Query(Constant.SEARCH_TEXT) String searchText
    );


    //for upload image and info
    @Multipart
    
    @POST("products/add_item")
    Call<Product> addProduct(
            @Header("Authorization") String auth,
            @Part MultipartBody.Part file,
            @Part(Constant.SP_STAFF_ID) String staffId,
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
    @FormUrlEncoded
    @POST("products/add_item_without_file")
    Call<Product> addProduct_without_file(
            @Header("Authorization") String auth,
            @Field(Constant.SP_STAFF_ID) String staffId,
            @Field(Constant.PRODUCT_NAME) String productName,
            @Field(Constant.PRODUCT_CODE) String productCode,
            @Field(Constant.CATEGORY_ID) String categoryId,
            @Field(Constant.PRODUCT_DESCRIPTION) String description,
            @Field(Constant.PRODUCT_SELL_PRICE) String sellPrice,
            @Field(Constant.PRODUCT_WEIGHT) String weight,
            @Field(Constant.PRODUCT_WEIGHT_UNIT_ID) String weightUnitId,
            @Field(Constant.SUPPLIERS_ID) String supplierId,
            @Field(Constant.PRODUCT_STOCK) String stock);


    //for upload image and info
    @Multipart
    
    @POST("products/update_item")
    Call<Product> updateProduct(
            @Header("Authorization") String auth,
            @Part MultipartBody.Part file,
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
            @Header("Authorization") String auth,
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
    
    @GET("expenses/list")
    Call<List<Expense>> getExpense(
            @Header("Authorization") String auth,
            @Query(Constant.SP_STAFF_ID) String staffId,
            @Query(Constant.SEARCH_TEXT) String searchText

    );



    //get expense data
    
    @GET("reports/expense_list")
    Call<List<Expense>> getAllExpense(
            @Header("Authorization") String auth,
            @Query(Constant.SP_STAFF_ID) String staffId,
            @Query(Constant.KEY_TYPE) String type
    );
}