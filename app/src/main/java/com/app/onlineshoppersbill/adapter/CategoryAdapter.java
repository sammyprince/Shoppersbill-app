package com.app.onlineshoppersbill.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.onlineshoppersbill.Constant;
import com.app.onlineshoppersbill.R;
import com.app.onlineshoppersbill.model.Category;
import com.app.onlineshoppersbill.model.Product;
import com.app.onlineshoppersbill.networking.ApiClient;
import com.app.onlineshoppersbill.networking.ApiInterface;
import com.app.onlineshoppersbill.settings.categories.AddCategoryActivity;
import com.app.onlineshoppersbill.settings.categories.CategoriesActivity;
import com.app.onlineshoppersbill.settings.categories.EditCategoryActivity;
import com.app.onlineshoppersbill.utils.Utils;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype.Slidetop;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {


    private List<Category> categoryData;
    private Context context;
    SharedPreferences sp;
    Utils utils;


    public CategoryAdapter(Context context, List<Category> categoryData) {
        this.context = context;
        this.categoryData = categoryData;
        utils = new Utils();
        sp = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

    }


    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final CategoryAdapter.MyViewHolder holder, int position) {


        String categoryName = categoryData.get(position).getProductCategoryName();
        holder.txtCategoryName.setText(categoryName);



        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryId = categoryData.get(holder.getAdapterPosition()).getProductCategoryId();

                NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(context);
                dialogBuilder
                        .withTitle(context.getString(R.string.delete))
                        .withMessage(context.getString(R.string.want_to_delete_product))
                        .withEffect(Slidetop)
                        .withDialogColor("#2979ff") //use color code for dialog
                        .withButton1Text(context.getString(R.string.yes))
                        .withButton2Text(context.getString(R.string.cancel))
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (utils.isNetworkAvailable(context)) {
                                    deleteCategory(categoryId);
                                    categoryData.remove(holder.getAdapterPosition());
                                    // Notify that item at position has been removed
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    dialogBuilder.dismiss();
                                } else {
                                    dialogBuilder.dismiss();
                                    Toasty.error(context, R.string.no_network_connection, Toast.LENGTH_SHORT).show();
                                }


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
    }

    @Override
    public int getItemCount() {
        return categoryData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtCategoryName;
        ImageView imgDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCategoryName = itemView.findViewById(R.id.txt_category_name);

            imgDelete = itemView.findViewById(R.id.img_delete);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, EditCategoryActivity.class);
            i.putExtra("category_id", categoryData.get(getAdapterPosition()).getProductCategoryId());
            i.putExtra("category_name", categoryData.get(getAdapterPosition()).getProductCategoryName());
            i.putExtra("category_code", categoryData.get(getAdapterPosition()).getProductCategoryCode());
            i.putExtra("category_description", categoryData.get(getAdapterPosition()).getProductCategoryDescription());

            context.startActivity(i);
        }
    }

    //delete from server
    private void deleteCategory(String categoryId) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<String> call;
        String auth_token = sp.getString(Constant.SP_AUTH_TOKEN, "");
        String staffId = sp.getString(Constant.SP_STAFF_ID, "");
        call = apiInterface.deleteCategory(auth_token, staffId, categoryId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body().equals(Constant.KEY_SUCCESS)) {
                    Toasty.success(context, R.string.category_deleted, Toast.LENGTH_SHORT).show();
                }
                else if(response.body().equals(Constant.KEY_EXIST))
                {
                    Toasty.error(context, R.string.exist, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toasty.error(context, R.string.failed, Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toasty.error(context, R.string.failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
