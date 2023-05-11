package com.github.sebastiankg02.csy2061as2.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Order;
import com.github.sebastiankg02.csy2061as2.data.OrderProduct;
import com.github.sebastiankg02.csy2061as2.data.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ViewHolder>{
    private Context context;
    private ArrayList<OrderProduct> products;

    public OrderProductAdapter(Context c, Order o){
        this.context = c;
        this.products = o.getOrderProducts();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.item_order_product, parent, false);
        ViewHolder holder = new ViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderProduct currentOP = products.get(position);
        Product.DBHelper prodHelper = new Product.DBHelper(context, "Product", null, 1);
        Product currentProduct = prodHelper.getSpecificProduct(currentOP.getProduct());
        float price = currentProduct.getPrice();
        int quantity = currentOP.getQuantity();

        holder.mainText.setText(currentProduct.getName());
        holder.descText.setText(currentProduct.getDesc());

        holder.priceText.setText("£"+new DecimalFormat("#.0#").format(price * quantity)+"\n"+"inc. VAT of £"+new DecimalFormat("#.0#").format((price*quantity)-((price*quantity)/1.2f)));
        holder.stockText.setText(quantity + " units in order\n£" +new DecimalFormat("#.0#").format(price) + " each");
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemLayout;
        public TextView mainText;
        public TextView descText;
        public TextView stockText;
        public TextView priceText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            this.itemLayout = (LinearLayout) itemView.findViewById(R.id.opItemLayout);
            this.mainText = (TextView) itemView.findViewById(R.id.opMainText);
            this.descText = (TextView) itemView.findViewById(R.id.opDescText);
            this.stockText = (TextView) itemView.findViewById(R.id.opStockText);
            this.priceText = (TextView) itemView.findViewById(R.id.opPriceText);
        }
    }
}
