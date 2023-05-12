package com.github.sebastiankg02.csy2061as2.dialog.category;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Category;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewCategoriesFragment;
import com.google.android.material.snackbar.Snackbar;

/**
 * Contains a factory method for creating a category deletion dialog.
 */
public class CategoryDeleteDialog {

    /**
     * Creates a dialog builder for deleting a category.
     *
     * @param context The context in which the dialog is created.
     * @param c The category to be deleted.
     * @param view The view that the dialog is associated with.
     * @return The AlertDialog.Builder object for the delete dialog.
     */
    public static AlertDialog.Builder createDialog(Context context, Category c, View view){
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.category_delete_title)
                .setMessage(R.string.category_delete_desc)
                .setPositiveButton(R.string.category_delete_confirm, new DialogInterface.OnClickListener() {
                    /**
                     * Handles the positive button click event for deleting a category.
                     * This will delete the category in question, as well as update the relevant adapter to reflect the change.
                     *
                     * @param dialogInterface The dialog interface that was clicked
                     * @param i The index of the clicked item
                     */
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        try {
                            Category.DBHelper catHelper = new Category.DBHelper(context);
                            catHelper.deleteCategory(c);

                            ViewCategoriesFragment.adapter.categories.remove(c);
                            ViewCategoriesFragment.adapter.notifyDataSetChanged();

                            Snackbar.make(view, R.string.category_delete_success, Snackbar.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Snackbar.make(view, R.string.category_delete_error, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    /**
                     * Called when the user clicks the negative button in the dialog.
                     * This will cancel the dialog.
                     * 
                     * @param dialogInterface The dialog interface that was clicked.
                     * @param i The index of the button that was clicked.
                     */
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        return deleteDialog;
    }
}
