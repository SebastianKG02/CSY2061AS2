package com.github.sebastiankg02.csy2061as2.dialog.product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Category;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewCategoriesFragment;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewProductListFragment;
import com.google.android.material.snackbar.Snackbar;

/**
 * Contains a factory method for creating an alert dialog builder object responsible for changing product name.
 */
public class ProductChangeNameDialog {
    /**
     * Creates an alert dialog builder object for modifying a product's name.
     *
     * @param c The context of the activity
     * @param vg The ViewGroup of the activity
     * @param toChange The product to modify
     * @return The alert dialog builder object
     */
    public static AlertDialog.Builder createDialog(Context c, ViewGroup vg, Product toChange) {
        AlertDialog.Builder output = new AlertDialog.Builder(c)
                .setTitle("Modifying Product Name [" + toChange.getId() + "]: '" + toChange.getName() + "'");
        View alertView = LayoutInflater.from(c).inflate(R.layout.dialog_product_change_name, vg, false);

        EditText text = alertView.findViewById(R.id.productChangeNameText);
        text.setText(toChange.getName());
        output.setView(alertView).setPositiveButton(R.string.admin_product_change_name, new DialogInterface.OnClickListener() {
            /**
             * Handles the positive button click event for the dialog box. If the text field is not empty, the product name is updated
             * and the adapter is notified of the change. A success message is displayed using a Snackbar. If the update
             * fails, an error message is displayed. If the text field is empty, an info message is displayed.
             *
             * @param dialogInterface The dialog interface that was clicked
             * @param i The index of the clicked item
             */
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(!text.getText().toString().isEmpty()) {
                    try {
                        toChange.setName(text.getText().toString());
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
