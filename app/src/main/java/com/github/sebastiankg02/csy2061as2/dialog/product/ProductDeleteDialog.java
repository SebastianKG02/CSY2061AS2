package com.github.sebastiankg02.csy2061as2.dialog.product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.PopupMenu;

import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.OrderProduct;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewProductListFragment;
import com.google.android.material.snackbar.Snackbar;

/**
 * Contains a factory method for creating an alert dialog builder object responsible for deleting a product.
 */
public class ProductDeleteDialog {
    /**
     * Creates a dialog to confirm the deletion of a product. If the product is currently assigned to an existing order,
     * a snackbar will be shown instead and the dialog will not be created.
     *
     * @param context The context in which the dialog is created
     * @param view The view to which the snackbar will be attached
     * @param p The product to be deleted
     * @return An alert dialog builder object for the deletion confirmation dialog, or null if the product is assigned to an order
     */
    public static AlertDialog.Builder createDialog(Context context, View view, Product p){
        OrderProduct.DBHelper opHelper = new OrderProduct.DBHelper(context);

        if(opHelper.getAllOrderProductsByProduct(p.getId()).size() > 0){
            Snackbar.make(view, R.string.product_delete_assigned, Snackbar.LENGTH_SHORT).show();
            return null;
        } else {
            AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context)
                    .setTitle(R.string.product_delete)
                    .setMessage(R.string.product_delete_desc)
                    .setPositiveButton(R.string.category_delete_confirm, new DialogInterface.OnClickListener() {
                        /**
                         * Handles the positive click event for the delete button in the product list view.
                         * Deletes the selected product from the database and updates the list view.
                         *
                         * @param dialogInterface The dialog interface that was clicked
                         * @param i The index of the clicked item
                         */
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            try {
                                Product.DBHelper prodHelper = new Product.DBHelper(context);
                                prodHelper.deleteProduct(p);
                                ViewProductListFragment.adapter.products.remove(p);
                                ViewProductListFragment.adapter.notifyDataSetChanged();
                                Snackbar.make(view, R.string.product_delete_success, Snackbar.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Snackbar.make(view, R.string.product_delete_error, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                        /**
                         * Called when the user clicks the negative button in the dialog.
                         * This will cancel the dialog.
                         *
                         * @param dialogInterface The dialog interface that was clicked
                         * @param i The index of the button that was clicked
                         */
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            return deleteDialog;
        }
    }
}
