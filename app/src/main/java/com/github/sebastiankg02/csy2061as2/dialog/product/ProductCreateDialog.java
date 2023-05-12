package com.github.sebastiankg02.csy2061as2.dialog.product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Category;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewCategoriesFragment;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewProductListFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * Contains a factory method for creating an alert dialog builder object responsible for creating a new product.
 */
public class ProductCreateDialog {
    /**
     * Creates an alert dialog builder object for creating a new product.
     *
     * @param c The context of the activity
     * @param vg The ViewGroup of the activity
     * @return The alert dialog builder object for creating a new product
     */
    public static AlertDialog.Builder createDialog(Context c, ViewGroup vg) {
        AlertDialog.Builder output = new AlertDialog.Builder(c)
                .setTitle(R.string.product_create);
        View alertView = LayoutInflater.from(c).inflate(R.layout.dialog_product_create, vg, false);

        EditText price = alertView.findViewById(R.id.productChangePriceText);
        EditText listPrice = alertView.findViewById(R.id.productChangeListPriceText);
        EditText retailPrice = alertView.findViewById(R.id.productChangeRetailPriceText);
        EditText descText = alertView.findViewById(R.id.productChangeDescText);
        EditText stockText = alertView.findViewById(R.id.productChangeStockText);
        EditText nameText = alertView.findViewById(R.id.productChangeNameText);
        ImageButton inc = (ImageButton) alertView.findViewById(R.id.productIncStockButton);
        ImageButton dec = (ImageButton) alertView.findViewById(R.id.productDecStockButton);
        Category.DBHelper catHelper = new Category.DBHelper(c);
        ArrayList<String> categoryNames = new ArrayList<String>();

        /*
         * Retrieve all categories from the database via CategoryHelper and concats their IDs and main category strings
         * to a list of strings, used in the spinner dropdown.
         */
        for(Category cat: catHelper.getCategories()){
            categoryNames.add(String.valueOf(cat.getId()) + " - " + cat.getMainCategory());
        }
        
        /*
         * Set up a spinner adapter with the loaded categories and sets it to the spinner view.
         * Also set default values for the name, description, prices, and stock of the new product.
         */
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item,categoryNames);
        Spinner spinner = (Spinner) alertView.findViewById(R.id.productMigrateSpinner);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        nameText.setText("New product name");
        descText.setText("New product description");
        price.setText(String.valueOf(0.0f));
        listPrice.setText(String.valueOf(0.0f));
        retailPrice.setText(String.valueOf(0.0f));
        stockText.setText(String.valueOf(1));

        
        //Set an OnClickListener on the increment button to increase the stock level TextView by 1 when clicked.
        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stockText.setText(String.valueOf(Integer.valueOf(stockText.getText().toString()) + 1));
            }
        });

        //Set an OnClickListener on the decrement button to decrease the stock level TextView by 1 when clicked.
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.valueOf(stockText.getText().toString())-1 >= 0){
                    stockText.setText(String.valueOf(Integer.valueOf(stockText.getText().toString()) - 1));
                }
            }
        });

        output.setView(alertView).setPositiveButton(R.string.admin_product_change_name, new DialogInterface.OnClickListener() {
            /**
             * Handles the positive click event for the "Create Product" button in the "Add Product" dialog.
             * Validates the input fields and creates a new product with the provided information.
             * If the product is successfully created, it is added to the database and the product list.
             * If the current category is the same as the product's category, the product is also added to the displayed list.
             * If any errors occur during the creation process, an error message is displayed.
             *
             * @param dialogInterface The dialog interface that was clicked
             * @param i The index of the clicked item
             */
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(!price.getText().toString().isEmpty() &&
                        !listPrice.getText().toString().isEmpty() &&
                        !retailPrice.getText().toString().isEmpty() &&
                        !nameText.getText().toString().isEmpty() &&
                        !descText.getText().toString().isEmpty() &&
                        !stockText.getText().toString().isEmpty()) {
                    try {
                        Product.DBHelper prodHelper = new Product.DBHelper(c);
                        Product createdProduct = new Product();

                        createdProduct.setName(nameText.getText().toString());
                        createdProduct.setDesc(descText.getText().toString());
                        createdProduct.setStockLevel(Integer.parseInt(stockText.getText().toString()));
                        createdProduct.setPrice(Float.parseFloat(price.getText().toString()));
                        createdProduct.setListPrice(Float.parseFloat(listPrice.getText().toString()));
                        createdProduct.setRetailPrice(Float.parseFloat(retailPrice.getText().toString()));
                        createdProduct.setCategory(Integer.parseInt(((String)spinner.getSelectedItem()).split("-")[0].trim()));

                        prodHelper.addProduct(createdProduct);

                        if (ViewCategoriesFragment.currentCategoryID == createdProduct.getCategory()){
                            ViewProductListFragment.adapter.products.add(createdProduct);
                            ViewProductListFragment.adapter.notifyDataSetChanged();
                        }
                        Snackbar.make(vg.getRootView(), R.string.product_create_success, Snackbar.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Snackbar.make(vg.getRootView(), R.string.product_create_error, Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(vg.getRootView(), R.string.product_create_fields_empty, Snackbar.LENGTH_SHORT).show();
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
