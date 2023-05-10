package com.github.sebastiankg02.csy2061as2.fragments.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.sebastiankg02.csy2061as2.MainActivity;
import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Basket;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class ViewSpecificProductFragment extends Fragment {

    private View masterView;
    private TextView categoryText;
    private TextView productNameText;
    private TextView productDescText;
    private TextView priceText;
    private TextView stockText;
    private TextView quantityText;
    private ImageButton decButton;
    private ImageButton incButton;
    private TextView totalPriceText;
    private Button addToBasketButton;
    private Button backButton;
    private Button adminButton;

    public ViewSpecificProductFragment() {
        super(R.layout.fragment_view_product);
    }

    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v,b);

        categoryText = (TextView) masterView.findViewById(R.id.viewSpecProductCategoryText);
        productNameText = (TextView) masterView.findViewById(R.id.viewSpecProductTitleText);
        productDescText = (TextView) masterView.findViewById(R.id.viewSpecProductDescText);
        priceText = (TextView) masterView.findViewById(R.id.viewSpecProductPriceText);
        stockText = (TextView) masterView.findViewById(R.id.viewSpecProductStockText);
        quantityText = (TextView) masterView.findViewById(R.id.addToBasketQuantityText);
        decButton = (ImageButton) masterView.findViewById(R.id.addToBasketDecProdCountButton);
        incButton = (ImageButton) masterView.findViewById(R.id.addToBasketIncProdCountButton);
        addToBasketButton = (Button) masterView.findViewById(R.id.viewSpecProductOrderButton);
        backButton = (Button) masterView.findViewById(R.id.viewSpecProductBackButton);
        adminButton = (Button) masterView.findViewById(R.id.viewSpecProductAdminButton);
        totalPriceText = (TextView) masterView.findViewById(R.id.viewSpecProductTotalPriceAmountText);

        categoryText.setText(ViewCategoriesFragment.currentCategoryTitle);
        productNameText.setText(ViewProductListFragment.currentProductViewing.getName());
        productDescText.setText(ViewProductListFragment.currentProductViewing.getDesc());
        priceText.setText("£"+new DecimalFormat("#.0#").format(ViewProductListFragment.currentProductViewing.getPrice())+" per unit");

        boolean hasStock = true;
        if(ViewProductListFragment.currentProductViewing.getStockLevel() <= 0){
            hasStock = false;
            stockText.setTextColor(getContext().getColor(R.color.red));
            stockText.setText(R.string.out_of_stock);
        } else {
            stockText.setText(String.valueOf(ViewProductListFragment.currentProductViewing.getStockLevel()) + " in stock.");
        }

        addToBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(masterView, "ADD TO BASKET TBC.", Snackbar.LENGTH_SHORT).show();
                Basket.init();
                Basket.addToBasket(ViewProductListFragment.currentProductViewing, Integer.valueOf(quantityText.getText().toString()));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewProductListFragment.currentProductViewing = null;
                MainActivity.globalNavigation.popBackStack();
            }
        });

        if(MainActivity.currentLoggedInUser.level.value != 1){
            adminButton.setEnabled(false);
            adminButton.setVisibility(View.INVISIBLE);
        } else {
            adminButton.setEnabled(true);
            adminButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(masterView, "ADMIN BUTTON TBC", Snackbar.LENGTH_SHORT).show();
                }
            });
        }

        incButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.valueOf(quantityText.getText().toString());

                if(quantity < ViewProductListFragment.currentProductViewing.getStockLevel()) {
                    quantityText.setText(String.valueOf(quantity + 1));
                    updateTotalPrice(quantity + 1);
                }
            }
        });

        decButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.valueOf(quantityText.getText().toString());

                if(quantity-1 > 0){
                    quantityText.setText(String.valueOf(quantity-1));
                    updateTotalPrice(quantity - 1);
                }
            }
        });
        updateTotalPrice(Integer.valueOf(quantityText.getText().toString()));
    }

    public void updateTotalPrice(int quantity){
        totalPriceText.setText("£"+new DecimalFormat("#.0#").format((float) quantity * ViewProductListFragment.currentProductViewing.getPrice()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        masterView = inflater.inflate(R.layout.fragment_view_product, container, false);
        return masterView;
    }
}