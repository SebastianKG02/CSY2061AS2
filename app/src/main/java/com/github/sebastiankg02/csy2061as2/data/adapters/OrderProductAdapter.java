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

/**
 * Adapter for the RecyclerView that displays a list of OrderProduct objects.
 * This adapter is responsible for inflating the layout for each item in the list,
 * and binding the data from the OrderProduct object to the corresponding views in the layout.
 *
 * context The context of the activity or fragment that is using this adapter.
 * products The list of OrderProduct objects to display in the RecyclerView.
 */
public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ViewHolder>{
    private Context context;
    private ArrayList<OrderProduct> products;

    /**
     * Constructs an adapter for displaying a list of products in an order.
     *
     * @param c The context in which the adapter is being used.
     * @param o The order containing the products to be displayed.
     */
    public OrderProductAdapter(Context c, Order o){
        this.context = c;
        this.products = o.getOrderProducts();
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
        View item = inflater.inflate(R.layout.item_order_product, parent, false);
        ViewHolder holder = new ViewHolder(item);
        return holder;
    }

    /**
     * Binds the data of an OrderProduct object to the views in the ViewHolder.
     *
     * @param holder The ViewHolder containing the views to bind the data to.
     * @param position The position of the OrderProduct object in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderProduct currentOP = products.get(position);
        Product.DBHelper prodHelper = new Product.DBHelper(context);
        Product currentProduct = prodHelper.getSpecificProduct(currentOP.getProduct());
        float price = currentProduct.getPrice();
        int quantity = currentOP.getQuantity();

        holder.mainText.setText(currentProduct.getName());
        holder.descText.setText(currentProduct.getDesc());

        holder.priceText.setText("£"+new DecimalFormat("#.0#").format(price * quantity)+"\n"+"inc. VAT of £"+new DecimalFormat("#.0#").format((price*quantity)-((price*quantity)/1.2f)));
        holder.stockText.setText(quantity + " units in order\n£" +new DecimalFormat("#.0#").format(price) + " each");
    }

    /**
     * Returns the number of items in the list of products.
     *
     * @return The number of items in the list of products.
     */
    @Override
    public int getItemCount() {
        return products.size();
    }

    /**
     * Contains references to the layout and views of the item, which can be accessed and modified
     * as needed.
     *
     * itemView The view of the item to be held by the ViewHolder
     * itemLayout The layout of the item to be held by the ViewHolder
     * mainText The main text view of the item to be held by the ViewHolder
     * descText The description text view of the item to be held by the ViewHolder
     * stockText The stock text view of the item to be held by the ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemLayout;
        public TextView mainText;
        public TextView descText;
        public TextView stockText;
        public TextView priceText;

        /**
         * Constructs a new ViewHolder object for an OrderProduct item instance within a RecyclerView.
         *
         * @param itemView The parent view for the item
         */
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
