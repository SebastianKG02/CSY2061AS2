package com.github.sebastiankg02.csy2061as2.data.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Basket;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.github.sebastiankg02.csy2061as2.fragments.BasketFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {

    private Context context;

    public BasketAdapter(Context c){
        Basket.init();
        this.context = c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.item_basket_product, parent, false);
        ViewHolder holder = new ViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product.DBHelper prodHelper = new Product.DBHelper(context);

        int productID = (int)Basket.getContents().keySet().toArray()[position];
        int productQuantity = Basket.getContents().get(productID);
        Product basketProduct = prodHelper.getSpecificProduct(productID);

        holder.productName.setText(basketProduct.getName());
        holder.productPricePer.setText("£"+new DecimalFormat("#.0#").format(basketProduct.getPrice())+" each");

        int maxQuantity = basketProduct.getStockLevel() + productQuantity;
        holder.quantity.setHint("Quantity ("+ maxQuantity + " units available)");
        holder.quantity.setText(String.valueOf(productQuantity));
        updateTotalPrice(holder, basketProduct.getPrice(), productQuantity);

        holder.decQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantityValue = Integer.parseInt(holder.quantity.getText().toString());
                if(quantityValue-1 > 0){
                    holder.quantity.setText(String.valueOf(quantityValue-1));
                    updateTotalPrice(holder, basketProduct.getPrice(), quantityValue-1);
                    Basket.removeFromBasket(basketProduct, 1);
                    BasketFragment.calculateTotalPrice(context, true);
                } else {
                    removeItem(basketProduct, holder);
                }
            }
        });

        holder.incQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantityValue = Integer.parseInt(holder.quantity.getText().toString());
                if(quantityValue+1 <= maxQuantity){
                    holder.quantity.setText(String.valueOf(quantityValue+1));
                    updateTotalPrice(holder, basketProduct.getPrice(), quantityValue+1);
                    Basket.addToBasket(basketProduct, 1);
                    BasketFragment.calculateTotalPrice(context, true);
                }
            }
        });

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(basketProduct, holder);
            }
        });
    }

    private void removeItem(Product basketProduct, ViewHolder holder){
        AlertDialog removeDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.basket_remove)
                .setMessage(R.string.basket_remove_desc)
                .setPositiveButton(R.string.basket_remove, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Basket.removeFromBasket(basketProduct);
                        notifyItemRemoved(holder.getAdapterPosition());
                        BasketFragment.calculateTotalPrice(context, true);
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create();
        removeDialog.show();
    }

    private void updateTotalPrice(ViewHolder holder, float price, int newQuantity){
        holder.totalPrice.setText("£"+new DecimalFormat("#.0#").format(price * newQuantity)+"\n"+"inc. VAT of £"+new DecimalFormat("#.0#").format((price*newQuantity)-((price*newQuantity)/1.2f)));
    }

    @Override
    public int getItemCount() {
        return Basket.getContents().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemLayout;
        public TextView productName;
        public TextView productPricePer;
        public ImageButton decQuantityButton;
        public TextInputEditText quantity;
        public ImageButton incQuantityButton;
        public TextView totalPrice;
        public Button removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemLayout = (LinearLayout) itemView.findViewById(R.id.basketItemLayout);
            this.productName = (TextView) itemView.findViewById(R.id.basketProductName);
            this.productPricePer = (TextView) itemView.findViewById(R.id.basketPricePerProduct);
            this.decQuantityButton = (ImageButton) itemView.findViewById(R.id.basketDecQuantityButton);
            this.quantity = (TextInputEditText) itemView.findViewById(R.id.basketQuantityText);
            this.incQuantityButton = (ImageButton) itemView.findViewById(R.id.basketIncQuantityButton);
            this.totalPrice = (TextView) itemView.findViewById(R.id.basketProductTotalPriceText);
            this.removeButton = (Button) itemView.findViewById(R.id.basketRemoveItemButton);
        }
    }
}
