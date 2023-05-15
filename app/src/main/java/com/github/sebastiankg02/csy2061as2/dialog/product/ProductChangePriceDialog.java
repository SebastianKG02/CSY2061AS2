package com.github.sebastiankg02.csy2061as2.dialog.product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewProductListFragment;
import com.google.android.material.snackbar.Snackbar;

/**
 * Contains a factory method for creating an alert dialog builder object responsible for changing product price.
 * (Price, List price & Retail price)
 */
public class ProductChangePriceDialog {
    /**
     * Creates an alert dialog builder object for modifying the price of a product.
     *
     * @param c The context of the activity
     * @param vg The ViewGroup of the activity
     * @param toChange The product to modify
     * @return The alert dialog builder object for modifying the price of a product
     */
    public static AlertDialog.Builder createDialog(Context c, ViewGroup vg, Product toChange) {
        AlertDialog.Builder output = new AlertDialog.Builder(c)
                .setTitle("Modifying Product Price [" + toChange.getId() + "]: '" + toChange.getName() + "'");
        View alertView = LayoutInflater.from(c).inflate(R.layout.dialog_product_change_price, vg, false);

        EditText price = alertView.findViewById(R.id.productChangePriceText);
        EditText listPrice = alertView.findViewById(R.id.productChangeListPriceText);
        EditText retailPrice = alertView.findViewById(R.id.productChangeRetailPriceText);

        price.setText(String.valueOf(toChange.getPrice()));
        listPrice.setText(String.valueOf(toChange.getListPrice()));
        retailPrice.setText(String.valueOf(toChange.getRetailPrice()));

        output.setView(alertView).setPositiveButton(R.string.admin_product_change_price, new DialogInterface.OnClickListener() {
            /**
             * Handles the positive button click event for the dialog box. If the price, list price, and retail price fields are not empty,
             * the product's price, list price, and retail price are updated with the values entered in the fields. The updated
             * product is then saved to the database and the product list is refreshed. A success message is displayed in a
             * Snackbar. If any of the fields are empty or an exception is thrown while parsing the values, an error message is
             * displayed in a Snackbar.
             *
             * @param dialogInterface The dialog interface that was clicked
             * @param i The index of the clicked item
             */
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(!price.getText().toString().isEmpty() && !listPrice.getText().toString().isEmpty() && !retailPrice.getText().toString().isEmpty()) {
                    try {
                        toChange.setPrice(Float.parseFloat(price.getText().toString()));
                        toChange.setListPrice(Float.parseFloat(listPrice.getText().toString()));
                        toChange.setRetailPrice(Float.parseFloat(retailPrice.getText().toString()));
                        Product.DBHelper prodHelper = new Product.DBHelper(c);
                        prodHelper.updateProduct(toChange);
                        ViewProductListFragment.adapter.notifyDataSetChanged();
                        Snackbar.make(vg.getRootView(), R.string.product_update_success, Snackbar.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Snackbar.make(vg.getRootView(), R.string.product_update_error, Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(vg.getRootView(), R.string.product_update_info_empty, Snackbar.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
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

        return output;
    }
}
