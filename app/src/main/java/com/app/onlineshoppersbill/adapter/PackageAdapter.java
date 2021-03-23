package com.app.onlineshoppersbill.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.onlineshoppersbill.Constant;
import com.app.onlineshoppersbill.R;
import com.app.onlineshoppersbill.model.Package;
import com.app.onlineshoppersbill.settings.subscribe.PurchseSubscribeActivity;
import android.content.SharedPreferences;

import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.MyViewHolder> {

    private List<Package> packageData;
    private Context context;
    SharedPreferences sp;

    public PackageAdapter(Context context, List<Package> packageData) {
        this.context = context;
        this.packageData = packageData;
        sp = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }


    @NonNull
    @Override
    public PackageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final PackageAdapter.MyViewHolder holder, int position) {
        String packageName = packageData.get(position).getPackageName();
        holder.txtPackageName.setText(packageName);
        String subscription_id = sp.getString(Constant.SP_SUBSCRIPTION_ID, "");
        String package_id = packageData.get(position).getPackageId();
        if(subscription_id.equals(package_id))
            holder.viewCard.setBackgroundColor(Color.parseColor("#80FF80"));
    }

    @Override
    public int getItemCount() {
        return packageData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtPackageName;
        CardView viewCard;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPackageName = itemView.findViewById(R.id.txt_package_name);
            viewCard = itemView.findViewById(R.id.package_card);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, packageData.get(getAdapterPosition()).getPackageName(), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, PurchseSubscribeActivity.class);
            i.putExtra(Constant.KEY_PACKAGE_DATA, packageData.get(getAdapterPosition()));
            context.startActivity(i);
        }
    }
}
