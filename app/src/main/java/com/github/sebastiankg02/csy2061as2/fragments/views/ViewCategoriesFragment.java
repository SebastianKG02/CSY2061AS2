package com.github.sebastiankg02.csy2061as2.fragments.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Category;
import com.github.sebastiankg02.csy2061as2.data.adapters.CategoryAdapter;

public class ViewCategoriesFragment extends Fragment {

    public static int currentCategoryID = -1;
    public static String currentCategoryTitle;

    private View masterView;
    private RecyclerView recycler;
    private TextView currentCategoryText;
    private static CategoryAdapter adapter;

    public ViewCategoriesFragment() {
        super(R.layout.fragment_view_categories);
    }


    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v,b);

        recycler = (RecyclerView) masterView.findViewById(R.id.categoryRecycler);
        currentCategoryText = (TextView) masterView.findViewById(R.id.categoryTitleText);

        Category.DBHelper catHelper = new Category.DBHelper(getContext(), "Category", null, 1);
        catHelper.initDefaultCategories();
        adapter = new CategoryAdapter(catHelper.getCategories(), getContext());
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recycler.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        masterView = inflater.inflate(R.layout.fragment_view_categories, container, false);
        return masterView;
    }
}