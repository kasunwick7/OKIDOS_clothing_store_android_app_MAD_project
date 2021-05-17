package com.store.okidosmobileapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.store.okidosmobileapp.Interface.AdminItemClickListner;
import com.store.okidosmobileapp.R;

public class AdminProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView admintxtProductName, admintxtProductDescription, admintxtProductPrice;
    public ImageView adminimageView;
    public AdminItemClickListner listner;

    public AdminProductViewHolder(@NonNull View itemView) {
        super(itemView);

        adminimageView = (ImageView) itemView.findViewById(R.id.admin_display_product_image);
        admintxtProductName = (TextView) itemView.findViewById(R.id.admin_display_product_name);
        admintxtProductDescription = (TextView) itemView.findViewById(R.id.admin_display_product_description);
        admintxtProductPrice = (TextView) itemView.findViewById(R.id.admin_display_product_price);

    }

    public void setItemClickListner(AdminItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {

        listner.onClick(view, getAdapterPosition(), false);

    }
}
