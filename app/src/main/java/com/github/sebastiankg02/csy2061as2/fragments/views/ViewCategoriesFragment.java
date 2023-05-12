package com.github.sebastiankg02.csy2061as2.fragments.views;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061as2.MainActivity;
import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Category;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.github.sebastiankg02.csy2061as2.data.adapters.CategoryAdapter;
import com.github.sebastiankg02.csy2061as2.data.adapters.ProductAdapter;
import com.github.sebastiankg02.csy2061as2.dialog.category.CategoryCreateDialog;

/**
 * A fragment that displays a list of categories.
 * The current category ID and title are stored as static variables, so that other app elements can function properly.
 * The fragment contains a RecyclerView to display the categories and a TextView to display the current category name.
 */
public class ViewCategoriesFragment extends Fragment {
    public static int currentCategoryID = -1;
    public static String currentCategoryTitle;

    private View masterView;
    private RecyclerView recycler;
    private TextView currentCategoryText;
    public static CategoryAdapter adapter;

    /**
     * Constructs a new ViewCategoriesFragment object.
     * This constructor sets the layout resource of the fragment to R.layout.fragment_view_categories.
     */
    public ViewCategoriesFragment() {
        super(R.layout.fragment_view_categories);
    }

    /**
     * Called when the view is created. Initializes the RecyclerView and sets up the adapter.
     * If the current logged in user is an admin, sets up a click listener on the category title text
     * to create a new category.
     *
     * @param v The view that was created
     * @param b The bundle associated with the view
     */
    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v,b);

        recycler = (RecyclerView) masterView.findViewById(R.id.categoryRecycler);
        currentCategoryText = (TextView) masterView.findViewById(R.id.categoryTitleText);

        Category.DBHelper catHelper = new Category.DBHelper(getContext());
        Product.DBHelper prodHelper = new Product.DBHelper(getContext());
        adapter = new CategoryAdapter(catHelper.getCategories(), getContext());
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recycler.setAdapter(adapter);

        if(MainActivity.currentLoggedInUser.level.value == 1){
            currentCategoryText.setText("Viewing all Categories\nTap here to create new category.");
            currentCategoryText.setOnClickListener(new View.OnClickListener() {
                /**
                 * Displays a dialog box to create a new category when the admin clicks on a button.
                 *
                 * @param view The view that was clicked
                 */
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder createDialog = CategoryCreateDialog.createDialog(getContext(), (ViewGroup) v);
                    createDialog.create().show();
                }
            });
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        masterView = inflater.inflate(R.layout.fragment_view_categories, container, false);
        return masterView;
    }
}