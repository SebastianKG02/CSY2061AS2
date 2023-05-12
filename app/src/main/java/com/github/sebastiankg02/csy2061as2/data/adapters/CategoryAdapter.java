package com.github.sebastiankg02.csy2061as2.data.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061as2.MainActivity;
import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Category;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.github.sebastiankg02.csy2061as2.dialog.category.CategoryChangeNameDialog;
import com.github.sebastiankg02.csy2061as2.dialog.category.CategoryCreateDialog;
import com.github.sebastiankg02.csy2061as2.dialog.category.CategoryDeleteDialog;
import com.github.sebastiankg02.csy2061as2.dialog.category.CategoryMigrateProductsDialog;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewCategoriesFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * An adapter for the RecyclerView that displays a list of categories.
 *
 * categories An ArrayList of Category objects to display in the RecyclerView
 * context The context of the activity or fragment that is using this adapter
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    public ArrayList<Category> categories;
    private Context context;

    /**
     * Constructs a new CategoryAdapter with the given list of categories and context.
     *
     * @param cats The list of categories to display in the adapter.
     * @param c The context in which the adapter is being used.
     */
    public CategoryAdapter(ArrayList<Category> cats, Context c){
        this.categories = cats;
        this.context = c;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.item_category, parent, false);
        ViewHolder holder = new ViewHolder(item);
        return holder;
    }

    /**
     * Binds the data to the view holder for a given position.
     *
     * @param holder The ViewHolder to bind the data to
     * @param position The position of the item within the adapter's data set
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category currentCategory = categories.get(position);
        holder.mainText.setText(currentCategory.getMainCategory());

        Product.DBHelper prodHelper = new Product.DBHelper(context);
        int numberOfProducts = prodHelper.getAllProductsInCategory(currentCategory.getId()).size();

        //Sets the subtext of a product item based on the number of products available.
        if(numberOfProducts > 1){
            holder.subText.setText(String.valueOf(numberOfProducts) + " products available.");
        } else if (numberOfProducts == 1){
            holder.subText.setText(String.valueOf(numberOfProducts) + " product available.");
        } else {
            holder.subText.setText("No products available yet!");
            holder.subText.setTextColor(context.getColor(R.color.red));
        }

        /**
         * Sets an OnClickListener on the item layout of a category in the RecyclerView.
         * If the current logged in user is an admin, a PopupMenu will be displayed with options to manage the category.
         * If the current logged in user is not an admin, clicking on the category will navigate to the list of products in that category.
         */
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.currentLoggedInUser.level.value == 0) {
                    if (numberOfProducts > 0) {
                        ViewCategoriesFragment.currentCategoryID = currentCategory.getId();
                        ViewCategoriesFragment.currentCategoryTitle = currentCategory.getMainCategory();
                        Navigation.findNavController(view).navigate(R.id.action_viewCategoriesFragment_to_viewProductListFragment);
                    }
                } else if (MainActivity.currentLoggedInUser.level.value == 1){
                    PopupMenu popup = new PopupMenu(context, view);
                    popup.getMenuInflater().inflate(R.menu.admin_category_manage, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        /**
                         * Handle the click events for the admin category menu items.
                         */
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch(menuItem.getItemId()) {
                                case R.id.adminCategoryView:
                                    ViewCategoriesFragment.currentCategoryID = currentCategory.getId();
                                    ViewCategoriesFragment.currentCategoryTitle = currentCategory.getMainCategory();
                                    Navigation.findNavController(view).navigate(R.id.action_viewCategoriesFragment_to_viewProductListFragment);
                                    break;
                                case R.id.adminCategoryChangeName:
                                     CategoryChangeNameDialog.createDialog(view.getContext(), (ViewGroup) view, currentCategory).create().show();
                                    break;
                                case R.id.adminCategoryMigrateProducts:
                                    CategoryMigrateProductsDialog.createDialog(view.getContext(), (ViewGroup) view, currentCategory).create().show();
                                    break;
                                case R.id.adminCategoryDelete:
                                    if (numberOfProducts > 0) {
                                        Snackbar.make(view, R.string.category_delete_products, Snackbar.LENGTH_SHORT).show();
                                    } else {
                                        CategoryDeleteDialog.createDialog(context, currentCategory, view).create().show();
                                    }
                                    break;
                                case R.id.adminCategoryNew:
                                    CategoryCreateDialog.createDialog(context, (ViewGroup) view).create().show();
                            }
                            return true;
                        }
                    });
                    popup.show();
                    notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Returns the number of items in the list of categories.
     *
     * @return The number of items in the list of categories.
     */
    @Override
    public int getItemCount() {
        return categories.size();
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     * In this case, it holds references to the layout and text views of a category item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemLayout;
        public TextView mainText;
        public TextView subText;

        /**
         * Constructs a ViewHolder object for a RecyclerView item view.
         *
         * @param itemView The view object that represents a single item in the RecyclerView.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemLayout = (LinearLayout) itemView.findViewById(R.id.categoryItemLayout);
            this.mainText = (TextView) itemView.findViewById(R.id.categoryMainText);
            this.subText = (TextView) itemView.findViewById(R.id.categorySubText);
        }
    }
}
