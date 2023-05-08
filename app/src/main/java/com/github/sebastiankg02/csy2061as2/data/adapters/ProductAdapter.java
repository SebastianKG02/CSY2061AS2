package com.github.sebastiankg02.csy2061as2.data.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewCategoriesFragment;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    public ArrayList<Product> products;
    private Context context;

    public ProductAdapter(ArrayList<Product> prods, Context c){
        this.products = prods;
        this.context = c;

        Log.i("PRODUCT", "Category is: " + ViewCategoriesFragment.currentCategoryID);
        for(Product p: prods){
            Log.i("PRODUCT", "Loading product: " + p.getName() + " from category " + p.getCategory());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.item_product, parent, false);
        ViewHolder holder = new ViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product currentProduct = products.get(position);

        holder.mainText.setText(currentProduct.getName());
        holder.descText.setText(currentProduct.getDesc());
        holder.priceText.setText("£"+new DecimalFormat("#.0#").format(currentProduct.getPrice())+" per unit");

        holder.stockText.setText(String.valueOf(currentProduct.getStockLevel()) + " in stock.");

        Log.i("STOCK", currentProduct.getName() + " has stock of: " + String.valueOf(currentProduct.getStockLevel()));
        if(currentProduct.getStockLevel() <= 0){
            holder.stockText.setText("Out of stock!");
            holder.stockText.setTextColor(context.getColor(R.color.red));
        } else {
            holder.stockText.setTextColor(context.getColor(R.color.black));
        }

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(holder.itemLayout, "GOTO PRODUCT " + currentProduct.getName(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout itemLayout;
        public TextView mainText;
        public TextView descText;
        public TextView stockText;
        public TextView priceText;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            this.itemLayout = (LinearLayout) itemView.findViewById(R.id.productItemLayout);
            this.mainText = (TextView) itemView.findViewById(R.id.productMainText);
            this.descText = (TextView) itemView.findViewById(R.id.productDescText);
            this.stockText = (TextView) itemView.findViewById(R.id.productStockText);
            this.priceText = (TextView) itemView.findViewById(R.id.productPriceText);
        }
    }
}
