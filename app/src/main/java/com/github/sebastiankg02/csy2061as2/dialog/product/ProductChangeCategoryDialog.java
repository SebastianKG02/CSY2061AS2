package com.github.sebastiankg02.csy2061as2.dialog.product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Category;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewProductListFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * Contains a factory method for creating an alert dialog builder object responsible for changing the category of a product.
 */
public class ProductChangeCategoryDialog {
    /**
     * Creates an alert dialog builder object for changing the category of a product.
     *
     * @param c The context of the activity
     * @param vg The ViewGroup of the activity
     * @param toChange The product to change the category of
     * @return The alert dialog builder object for changing the category of a product
     */
    public static AlertDialog.Builder createDialog(Context c, ViewGroup vg, Product toChange) {
        AlertDialog.Builder output = new AlertDialog.Builder(c)
                .setTitle("Migrating Product [" + toChange.getId() + "]: '" + toChange.getName() + "'");
        View alertView = LayoutInflater.from(c).inflate(R.layout.dialog_product_change_category, vg, false);

        Category.DBHelper catHelper = new Category.DBHelper(c);
        ArrayList<String> categoryNames = new ArrayList<String>();

       //Iterate through the list of categories in the databse and add their names to a list of category name for the spinner dropdown.
        int counter = 0;
        int thisCategory = 0;
        for(Category cat: catHelper.getCategories()){
            categoryNames.add(String.valueOf(cat.getId()) + " - " + cat.getMainCategory());

            if(cat.getId() == toChange.getCategory()){
                thisCategory = counter;
            }
        }

        //Sets up a spinner with an adapter containing a list of all category names in the database.
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item,categoryNames);
        Spinner spinner = (Spinner) alertView.findViewById(R.id.productMigrateSpinner);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(thisCategory);

        output.setView(alertView).setPositiveButton(R.string.admin_product_change_name, new DialogInterface.OnClickListener() {
            /**
             * Handles the positive button click event for the dialog box. Updates the category of the product to the selected value
             * from the spinner, updates the product in the database, removes the old product from the adapter, and
             * displays a success message. If an error occurs, displays an error message.
             *
             * @param dialogInterface The dialog interface that was clicked
             * @param i The index of the item that was clicked
             */
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                try {
                    toChange.setCategory(Integer.parseInt(((String)spinner.getSelectedItem()).split("-")[0].trim()));
                    Product.DBHelper prodHelper = new Product.DBHelper(c);
                    prodHelper.updateProduct(toChange);
                    ViewProductListFragment.adapter.products.remove(toChange);
                    ViewProductListFragment.adapter.notifyDataSetChanged();
                    Snackbar.make(vg.getRootView(), R.string.product_update_success, Snackbar.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Snackbar.make(vg.getRootView(), R.string.product_update_error, Snackbar.LENGTH_SHORT).show();
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
