package com.github.sebastiankg02.csy2061as2.dialog.category;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Category;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewCategoriesFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * Contains a factory method for the creation of the alert dialog builder object responsible for product category migration.
 */
public class CategoryMigrateProductsDialog {

    /**
     * Creates an dialog builder object for migrating all products from one category to another.
     *
     * @param c The context of the activity
     * @param vg The ViewGroup of the activity
     * @param toChange The category to migrate products from
     * @return The AlertDialog.Builder object
     */
    public static AlertDialog.Builder createDialog(Context c, ViewGroup vg, Category toChange) {
        Product.DBHelper prodHelper = new Product.DBHelper(c);
        AlertDialog.Builder output = new AlertDialog.Builder(c)
                .setTitle("Migrating [" + prodHelper.getAllProductsInCategory(toChange.getId()).size() + "] product(s) in Category [" + toChange.getId() + "]: '" + toChange.getMainCategory() + "'");
        View alertView = LayoutInflater.from(c).inflate(R.layout.dialog_category_migrate_products, vg, false);
        Category.DBHelper catHelper = new Category.DBHelper(c);
        ArrayList<String> categoryNames = new ArrayList<String>();

        //Iterate through the list of categories in the databse and add their names to a list of category name for the spinner dropdown.
        int counter = 0;
        int thisCategory = 0;
        for(Category cat: catHelper.getCategories()){
            categoryNames.add(String.valueOf(cat.getId()) + " - " + cat.getMainCategory());

            if(cat == toChange){
                thisCategory = counter;
            }
        }

        //Sets up a spinner with an adapter containing category names and selects the current category.
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item,categoryNames);
        Spinner spinner = (Spinner) alertView.findViewById(R.id.adminCategoryMigrateSpinner);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(thisCategory);

        output.setView(alertView).setPositiveButton(R.string.migrate, new DialogInterface.OnClickListener() {
            /**
             * Handles the positive button click event for the dialog box. Changes the category of all products in the current category
             * to the selected category from the spinner. Notifies the adapter of the change and displays a success message.
             *
             * @param dialogInterface The dialog interface that was clicked
             * @param i The index of the item that was clicked
             */
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                try {
                    int newCategory = Integer.parseInt(((String)spinner.getSelectedItem()).split("-")[0].trim());
                    for(Product p: prodHelper.getAllProductsInCategory(toChange.getId())){
                        p.setCategory(newCategory);
                        prodHelper.updateProduct(p);
                    }
                    ViewCategoriesFragment.adapter.notifyDataSetChanged();
                    Snackbar.make(vg.getRootView(), R.string.category_migrate_success, Snackbar.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Snackbar.make(vg.getRootView(), R.string.category_migrate_error, Snackbar.LENGTH_SHORT).show();
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
