package com.github.sebastiankg02.csy2061as2.data.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061as2.MainActivity;
import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.github.sebastiankg02.csy2061as2.dialog.product.ProductChangeCategoryDialog;
import com.github.sebastiankg02.csy2061as2.dialog.product.ProductChangeDescDialog;
import com.github.sebastiankg02.csy2061as2.dialog.product.ProductChangeNameDialog;
import com.github.sebastiankg02.csy2061as2.dialog.product.ProductChangePriceDialog;
import com.github.sebastiankg02.csy2061as2.dialog.product.ProductChangeStockDialog;
import com.github.sebastiankg02.csy2061as2.dialog.product.ProductCreateDialog;
import com.github.sebastiankg02.csy2061as2.dialog.product.ProductDeleteDialog;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewProductListFragment;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * An adapter for a RecyclerView that displays a list of products.
 *
 * products An ArrayList of Product objects to display in the RecyclerView
 * context The context of the application
 * activity The activity that contains the RecyclerView
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    public ArrayList<Product> products;
    private Context context;
    private Activity activity;

    /**
     * Adapter for displaying a list of products in a RecyclerView.
     *
     * @param prods The list of products to display
     * @param c The context of the application
     * @param a The activity that is using the adapter
     */
    public ProductAdapter(ArrayList<Product> prods, Context c, Activity a){
        this.products = prods;
        this.context = c;
        this.activity = a;
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
        View item = inflater.inflate(R.layout.item_product, parent, false);
        ViewHolder holder = new ViewHolder(item);
        return holder;
    }

    /**
     * Binds the data of a product to the view holder.
     *
     * @param holder The view holder to bind the data to.
     * @param position The position of the product in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product currentProduct = products.get(position);

        holder.mainText.setText(currentProduct.getName());
        holder.descText.setText(currentProduct.getDesc());
        holder.priceText.setText("£"+new DecimalFormat("#.0#").format(currentProduct.getPrice())+" per unit");

        holder.stockText.setText(String.valueOf(currentProduct.getStockLevel()) + " in stock.");

        
        //Sets the stock text and color based on the stock level of the current product.
        if(currentProduct.getStockLevel() <= 0){
            holder.stockText.setText(R.string.out_of_stock);
            holder.stockText.setTextColor(context.getColor(R.color.red));
        } else {
            holder.stockText.setTextColor(context.getColor(R.color.black));
        }

        /*
         * Sets an OnClickListener on the item layout of a product in the product list.
         * If the current logged in user is an admin, a PopupMenu will be displayed with options to edit the product.
         * If the current logged in user is not an admin, the user will be taken to the product page.
         */
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.currentLoggedInUser.level.value == 0){
                    ViewProductListFragment.currentProductViewing = currentProduct;
                    Navigation.findNavController(view).navigate(R.id.action_viewProductListFragment_to_viewSpecificProductFragment);
                } else {
                    PopupMenu popup = new PopupMenu(context, view);
                    popup.getMenuInflater().inflate(R.menu.product_click, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        //Handle the click events for the options menu in the product list fragment.
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.viewProductPageOption:
                                    ViewProductListFragment.currentProductViewing = currentProduct;
                                    Navigation.findNavController(view).navigate(R.id.action_viewProductListFragment_to_viewSpecificProductFragment);
                                    return true;
                                case R.id.adminProductChangeName:
                                    ProductChangeNameDialog.createDialog(context, (ViewGroup) view, currentProduct).create().show();
                                    return true;
                                case R.id.adminProductChangeDescription:
                                    ProductChangeDescDialog.createDialog(context, (ViewGroup) view, currentProduct).create().show();
                                    return true;
                                case R.id.adminProductChangeCategory:
                                    ProductChangeCategoryDialog.createDialog(context, (ViewGroup) view, currentProduct).create().show();
                                    return true;
                                case R.id.adminProductChangeStock:
                                    ProductChangeStockDialog.createDialog(context, (ViewGroup) view, currentProduct).create().show();
                                    return true;
                                case R.id.adminProductChangePrice:
                                    ProductChangePriceDialog.createDialog(context, (ViewGroup) view, currentProduct).create().show();
                                    return true;
                                case R.id.adminProductDelete:
                                    AlertDialog.Builder deleteDialog = ProductDeleteDialog.createDialog(context, (ViewGroup) view, currentProduct);
                                    if(deleteDialog != null){
                                        deleteDialog.create().show();
                                    }
                                    return true;
                                case R.id.adminProductCreate:
                                    ProductCreateDialog.createDialog(context, (ViewGroup) view).create().show();
                                    return true;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            }
        });
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

    /*
     * Contains references to the layout and views of the item, such as the main text, description text,
     * stock text, and price text.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout itemLayout;
        public TextView mainText;
        public TextView descText;
        public TextView stockText;
        public TextView priceText;

        /**
         * Constructs a ViewHolder object for a product item view.
         *
         * @param itemView The view representing a single product item.
         */
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
