package com.github.sebastiankg02.csy2061as2.fragments.views;

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
import com.github.sebastiankg02.csy2061as2.data.adapters.ProductAdapter;
import com.github.sebastiankg02.csy2061as2.dialog.product.ProductCreateDialog;

/**
 * A fragment that displays a list of products within a category, as per the one selected in ViewCategoriesFragment
 * The fragment contains a RecyclerView to display the products and a TextView to display the current category name.
 * The fragment also contains a ProductAdapter to handle the display of the products.
 */
public class ViewProductListFragment extends Fragment {
    private View masterView;
    private RecyclerView recycler;
    private TextView currentCategoryHeader;
    public static ProductAdapter adapter;
    public static Product currentProductViewing;

    /**
     * Constructs a new ViewProductListFragment object.
     * This constructor sets the layout resource file to be used by the fragment.
     */
    public ViewProductListFragment() {
        super(R.layout.fragment_view_product_list);
    }

    /**
     * Called when the view is created. Initializes the RecyclerView and sets up the adapter.
     * If the current user is an admin, the category header will display a message to add a new product.
     *
     * @param v The view that was created
     * @param b The bundle associated with the view
     */
    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);

        recycler = (RecyclerView) masterView.findViewById(R.id.productRecycler);
        currentCategoryHeader = (TextView) masterView.findViewById(R.id.productCategoryTitleText);

        //Load products from category as loaded from database
        Product.DBHelper productHelper = new Product.DBHelper(getContext());
        adapter = new ProductAdapter(productHelper.getAllProductsInCategory(ViewCategoriesFragment.currentCategoryID), getContext(), getActivity());
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recycler.setAdapter(adapter);

        if(MainActivity.currentLoggedInUser.level.value == 0) {
            currentCategoryHeader.setText("Browsing " + ViewCategoriesFragment.currentCategoryTitle);
        } else if (MainActivity.currentLoggedInUser.level.value == 1){
            currentCategoryHeader.setText("Browsing " + ViewCategoriesFragment.currentCategoryTitle + "\nClick here to add new product.");
            currentCategoryHeader.setOnClickListener(new View.OnClickListener() {
                /**
                 * Creates and displays a dialog for creating a new product when the admin clicks on the category title.
                 *
                 * @param view The view that was clicked.
                 */
                @Override
                public void onClick(View view) {
                    ProductCreateDialog.createDialog(getContext(), (ViewGroup) v).create().show();
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
        masterView = inflater.inflate(R.layout.fragment_view_product_list, container, false);
        return masterView;
    }
}