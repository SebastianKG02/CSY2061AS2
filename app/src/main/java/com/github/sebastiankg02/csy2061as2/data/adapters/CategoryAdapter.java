package com.github.sebastiankg02.csy2061as2.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Category;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewCategoriesFragment;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    public ArrayList<Category> categories;
    private Context context;

    public CategoryAdapter(ArrayList<Category> cats, Context c){
        this.categories = cats;
        this.context = c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.item_category, parent, false);
        ViewHolder holder = new ViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category currentCategory = categories.get(position);
        holder.mainText.setText(currentCategory.getMainCategory());

        Product.DBHelper prodHelper = new Product.DBHelper(context);
        int numberOfProducts = prodHelper.getAllProductsInCategory(currentCategory.getId()).size();

        if(numberOfProducts > 1){
            holder.subText.setText(String.valueOf(numberOfProducts) + " products available.");
        } else if (numberOfProducts == 1){
            holder.subText.setText(String.valueOf(numberOfProducts) + " product available.");
        } else {
            holder.subText.setText("No products available yet!");
            holder.subText.setTextColor(context.getColor(R.color.red));
        }

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numberOfProducts > 0) {
                    ViewCategoriesFragment.currentCategoryID = currentCategory.getId();
                    ViewCategoriesFragment.currentCategoryTitle = currentCategory.getMainCategory();
                    Navigation.findNavController(view).navigate(R.id.action_viewCategoriesFragment_to_viewProductListFragment);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemLayout;
        public TextView mainText;
        public TextView subText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemLayout = (LinearLayout) itemView.findViewById(R.id.categoryItemLayout);
            this.mainText = (TextView) itemView.findViewById(R.id.categoryMainText);
            this.subText = (TextView) itemView.findViewById(R.id.categorySubText);
        }
    }
}
