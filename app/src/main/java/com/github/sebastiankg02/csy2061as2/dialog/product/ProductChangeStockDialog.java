package com.github.sebastiankg02.csy2061as2.dialog.product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewProductListFragment;
import com.google.android.material.snackbar.Snackbar;

/**
 * Contains a factory method for creating an alert dialog builder object responsible for changing product stock level.
 */
public class ProductChangeStockDialog {
    /**
     * Creates an alert dialog builder object for modifying the stock of a product.
     *
     * @param c The context of the activity
     * @param vg The ViewGroup of the activity
     * @param toChange The product to modify
     * @return The alert dialog builder object
     */
    public static AlertDialog.Builder createDialog(Context c, ViewGroup vg, Product toChange) {
        AlertDialog.Builder output = new AlertDialog.Builder(c)
                .setTitle("Modifying Product Stock [" + toChange.getId() + "]: '" + toChange.getName() + "'");
        View alertView = LayoutInflater.from(c).inflate(R.layout.dialog_product_change_stock, vg, false);

        EditText text = alertView.findViewById(R.id.productChangeStockText);
        ImageButton inc = (ImageButton) alertView.findViewById(R.id.productIncStockButton);
        ImageButton dec = (ImageButton) alertView.findViewById(R.id.productDecStockButton);

        text.setText(String.valueOf(toChange.getStockLevel()));

        //Set an OnClickListener on the increment button that increases the value of the stock level TextView by 1.
        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setText(String.valueOf(Integer.valueOf(text.getText().toString()) + 1));
            }
        });

        //Set an OnClickListener on the decrement button that decreases the value of the stock level TextView by 1.
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.valueOf(text.getText().toString())-1 >= 0){
                    text.setText(String.valueOf(Integer.valueOf(text.getText().toString()) - 1));
                }
            }
        });

        output.setView(alertView).setPositiveButton(R.string.admin_product_change_stock, new DialogInterface.OnClickListener() {
            /**
             * Handles the positive nutton click event for the dialog box. If the stock level text field is not empty, the stock level of the product
             * is updated with the new value entered by the user. The product is then updated in the database and the product
             * list adapter contents are refreshed. A success message is displayed to the user. If the stock level text field is empty, an error message is
             * displayed to the user.
             *
             * @param dialogInterface The dialog interface that was clicked
             * @param i The index of the clicked item
             */
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(!text.getText().toString().isEmpty()) {
                    try {
                        toChange.setStockLevel(Integer.valueOf(text.getText().toString()));
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
