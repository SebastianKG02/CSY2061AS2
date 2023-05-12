package com.github.sebastiankg02.csy2061as2.dialog.category;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Category;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewCategoriesFragment;
import com.google.android.material.snackbar.Snackbar;

/**
 * Contains a factory method for creating the category creation dialog builder.
 */
public class CategoryCreateDialog {
    /**
     * Creates a dialog builder for creating a new category.
     *
     * @param c The context in which the dialog is created
     * @param vg The parent view group of the dialog
     * @return An AlertDialog.Builder object for creating the dialog
     */
    public static AlertDialog.Builder createDialog(Context c, ViewGroup vg) {
        AlertDialog.Builder output = new AlertDialog.Builder(c)
                .setTitle("Creating new Category");
        View alertView = LayoutInflater.from(c).inflate(R.layout.dialog_category_create, vg, false);

        EditText newCategoryName = alertView.findViewById(R.id.categoryCreateNameText);
        output.setView(alertView).setPositiveButton(R.string.category_edit_new, new DialogInterface.OnClickListener() {
            /**
             * Handles the positive button click event for creating a new category.
             * This will create a new category with the given name (as specified by the EditText view),
             * and will also update the database and relevant adapter with the change.
             * 
             * @param dialogInterface The dialog interface that was clicked
             * @param i The index of the clicked item
             */
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(!newCategoryName.getText().toString().isEmpty()) {
                    try {
                        Category newCategory = new Category();
                        newCategory.setMainCategory(newCategoryName.getText().toString());
                        Category.DBHelper catHelper = new Category.DBHelper(c);
                        catHelper.addCategory(newCategory);
                        ViewCategoriesFragment.adapter.notifyDataSetChanged();
                        ViewCategoriesFragment.adapter.categories.add(newCategory);
                        Snackbar.make(vg.getRootView(), R.string.category_create_success, Snackbar.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Snackbar.make(vg.getRootView(), R.string.category_create_error, Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(vg.getRootView(), R.string.category_create_name_empty, Snackbar.LENGTH_SHORT).show();
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
