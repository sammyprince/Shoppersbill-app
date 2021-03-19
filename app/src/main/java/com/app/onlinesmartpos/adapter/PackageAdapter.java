package com.app.onlinesmartpos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.onlinesmartpos.Constant;
import com.app.onlinesmartpos.R;
import com.app.onlinesmartpos.model.Package;
import com.app.onlinesmartpos.settings.subscribe.PurchseSubscribeActivity;

import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.MyViewHolder> {

    private List<Package> packageData;
    private Context context;

    public PackageAdapter(Context context, List<Package> packageData) {
        this.context = context;
        this.packageData = packageData;
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
    }

    @Override
    public int getItemCount() {
        return packageData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtPackageName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPackageName = itemView.findViewById(R.id.txt_package_name);
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
