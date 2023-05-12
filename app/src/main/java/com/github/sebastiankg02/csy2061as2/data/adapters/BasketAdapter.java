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

/**
 * Adapter for the RecyclerView that displays the items in the user's basket.
 * The adapter is responsible for creating the views that represent each item in the basket.
 *
 * context The context of the activity or fragment that is using this adapter.
 */
public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {

    private Context context;

    public BasketAdapter(Context c){
        Basket.init();
        this.context = c;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.item_basket_product, parent, false);
        ViewHolder holder = new ViewHolder(item);
        return holder;
    }

    /**
     * Binds the data to the views in the ViewHolder.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the adapter.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product.DBHelper prodHelper = new Product.DBHelper(context);

        /*
         * Updates the view holder with the product details and quantity information.
         */
        int productID = (int)Basket.getContents().keySet().toArray()[position];
        int productQuantity = Basket.getContents().get(productID);
        Product basketProduct = prodHelper.getSpecificProduct(productID);

        holder.productName.setText(basketProduct.getName());
        holder.productPricePer.setText("£"+new DecimalFormat("#.0#").format(basketProduct.getPrice())+" each");

        int maxQuantity = basketProduct.getStockLevel() + productQuantity;
        holder.quantity.setHint("Quantity ("+ maxQuantity + " units available)");
        holder.quantity.setText(String.valueOf(productQuantity));
        updateTotalPrice(holder, basketProduct.getPrice(), productQuantity);

        /**
         * Sets an OnClickListener on the decrement quantity button of a product in the basket.
         * When the button is clicked, the quantity of the product is decremented by 1.
         * If the quantity becomes 0, the product is removed from the basket.
         * The total price of the basket is updated accordingly.
         */
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

        /**
         * Sets an OnClickListener on the increment quantity button of a product in the basket.
         * When the button is clicked, the quantity of the product is incremented by 1, and the total price
         * of the product is updated accordingly. The product is also added to the basket, and the total price
         * of the basket is recalculated.
         */
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

        /**
         * Sets an OnClickListener on the remove button of a basket product item.
         * When the button is clicked, the corresponding item is removed from the basket.
         */
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(basketProduct, holder);
            }
        });
    }

    /**
     * Removes an item from the basket and updates the UI accordingly.
     *
     * @param basketProduct The product to remove from the basket
     * @param holder The ViewHolder associated with the product to remove
     */
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

    /**
     * Updates the total price displayed in the ViewHolder based on the given price and quantity.
     *
     * @param holder The ViewHolder containing the total price TextView to be updated.
     * @param price The price of the item.
     * @param newQuantity The new quantity of the item.
     */
    private void updateTotalPrice(ViewHolder holder, float price, int newQuantity){
        holder.totalPrice.setText("£"+new DecimalFormat("#.0#").format(price * newQuantity)+"\n"+"inc. VAT of £"+new DecimalFormat("#.0#").format((price*newQuantity)-((price*newQuantity)/1.2f)));
    }

    /**
     * Returns the number of items in the basket.
     *
     * @return The number of items in the basket.
     */
    @Override
    public int getItemCount() {
        return Basket.getContents().size();
    }

    /**
    * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
    * This ViewHolder is used to hold the views of a single item in the basket.
    *
    * itemView The view of the item in the basket
    * itemLayout The layout of the item in the basket
    * productName The name of the product in the basket
    * productPricePer The price per unit of the product in the basket
    * decQuantityButton The button to decrease the quantity of the product in the basket
    * quantity The quantity of the product in the basket
    * incQuantityButton The button to increase the quantity of the product in the basket
    * totalPrice The total price
    */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemLayout;
        public TextView productName;
        public TextView productPricePer;
        public ImageButton decQuantityButton;
        public TextInputEditText quantity;
        public ImageButton incQuantityButton;
        public TextView totalPrice;
        public Button removeButton;

        /**
        * Constructs a ViewHolder object for the Basket - any any products within the basket
        *
        * @param itemView The view representing the basket item
        */
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
