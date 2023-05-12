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
 * Contains a factory method for creating category name changing dialogs.
 */
public class CategoryChangeNameDialog {

    /**
     * Creates a dialog builder for modifying a category name.
     *
     * @param c The context of the activity.
     * @param vg The view group of the activity.
     * @param toChange The category to be modified.
     * @return The AlertDialog.Builder object.
     */
    public static AlertDialog.Builder createDialog(Context c, ViewGroup vg, Category toChange) {
        AlertDialog.Builder output = new AlertDialog.Builder(c)
                .setTitle("Modifying Category Name [" + toChange.getId() + "]: '" + toChange.getMainCategory() + "'");
        View alertView = LayoutInflater.from(c).inflate(R.layout.dialog_category_change_name, vg, false);

        EditText newCategoryName = alertView.findViewById(R.id.categoryChangeNameText);
        newCategoryName.setText(toChange.getMainCategory());

        output.setView(alertView).setPositiveButton(R.string.category_edit_name, new DialogInterface.OnClickListener() {
            /**
             * Handles the click event for the positive button of the dialog box. Updates the main category name of the selected category
             * with the new category name entered by the user. If the update is successful, the adapter is notified
             * of the change and a success message is displayed. If the update fails, an error message is displayed.
             *
             * @param dialogInterface The dialog interface that was clicked
             * @param i The index of the clicked item
             */
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                try {
                    toChange.setMainCategory(newCategoryName.getText().toString());
                    Category.DBHelper catHelper = new Category.DBHelper(c);
                    catHelper.updateCategory(toChange);
                    ViewCategoriesFragment.adapter.notifyDataSetChanged();
                    Snackbar.make(vg.getRootView(), R.string.category_change_name_success, Snackbar.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Snackbar.make(vg.getRootView(), R.string.category_change_name_error, Snackbar.LENGTH_SHORT).show();
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
