package com.github.sebastiankg02.csy2061as2.fragments.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Category;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.github.sebastiankg02.csy2061as2.data.adapters.ProductAdapter;

public class ViewProductListFragment extends Fragment {

    private View masterView;
    private RecyclerView recycler;
    private TextView currentCategoryHeader;
    public static ProductAdapter adapter;
    public static Product currentProductViewing;

    public ViewProductListFragment() {
        super(R.layout.fragment_view_product_list);
    }


    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);

        recycler = (RecyclerView) masterView.findViewById(R.id.productRecycler);
        currentCategoryHeader = (TextView) masterView.findViewById(R.id.productCategoryTitleText);

        Product.DBHelper productHelper = new Product.DBHelper(getContext(), "Product", null, 1);
        productHelper.initDefaultProducts();
        adapter = new ProductAdapter(productHelper.getAllProductsInCategory(ViewCategoriesFragment.currentCategoryID), getContext(), getActivity());
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recycler.setAdapter(adapter);

        Category.DBHelper catHelper = new Category.DBHelper(getContext(), "Category", null, 1);
        currentCategoryHeader.setText("Browsing " + ViewCategoriesFragment.currentCategoryTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        masterView = inflater.inflate(R.layout.fragment_view_product_list, container, false);
        return masterView;
    }
}