package com.github.sebastiankg02.csy2061as2.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Basket;
import com.github.sebastiankg02.csy2061as2.data.DeliveryMethod;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.github.sebastiankg02.csy2061as2.data.adapters.BasketAdapter;

import java.text.DecimalFormat;

public class BasketFragment extends Fragment {

    private View masterView;
    private TextView emptyText;
    private RecyclerView recycler;
    private BasketAdapter adapter;
    private RadioButton shippingStandard;
    private RadioButton shippingExpress;
    private RadioButton shippingNextDay;
    private static TextView totalPrice;
    private Button placeOrderButton;
    private Button backButton;
    private static float currentShippingCost;

    public BasketFragment() {
        super(R.layout.fragment_basket);
    }

    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);

        emptyText = (TextView) masterView.findViewById(R.id.basketInfoText);
        recycler = (RecyclerView) masterView.findViewById(R.id.basketRecycler);
        adapter = new BasketAdapter(getContext());
        shippingStandard = (RadioButton) masterView.findViewById(R.id.basketShippingStandard);
        shippingExpress = (RadioButton) masterView.findViewById(R.id.basketShippingExpress);
        shippingNextDay = (RadioButton) masterView.findViewById(R.id.basketShippingNextDay);
        totalPrice = (TextView) masterView.findViewById(R.id.basketTotalPriceAmountText);
        placeOrderButton = (Button) masterView.findViewById(R.id.basketPlaceOrder);
        backButton = (Button) masterView.findViewById(R.id.basketBackToBrowse);

        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        shippingStandard.setText(getResources().getString(DeliveryMethod.STANDARD.resString) + "\n£"+new DecimalFormat("#.0#").format(DeliveryMethod.STANDARD.cost));
        shippingStandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shippingStandard.setChecked(true);
                shippingExpress.setChecked(false);
                shippingNextDay.setChecked(false);
                currentShippingCost = DeliveryMethod.STANDARD.cost;
                calculateTotalPrice(getContext());
            }
        });

        shippingExpress.setText(getResources().getString(DeliveryMethod.EXPRESS.resString) + "\n£"+new DecimalFormat("#.0#").format(DeliveryMethod.EXPRESS.cost));
        shippingExpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shippingStandard.setChecked(false);
                shippingExpress.setChecked(true);
                shippingNextDay.setChecked(false);
                currentShippingCost = DeliveryMethod.EXPRESS.cost;
                calculateTotalPrice(getContext());
            }
        });

        shippingNextDay.setText(getResources().getString(DeliveryMethod.NEXT_DAY.resString) + "\n£"+new DecimalFormat("#.0#").format(DeliveryMethod.NEXT_DAY.cost));
        shippingNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shippingStandard.setChecked(false);
                shippingExpress.setChecked(false);
                shippingNextDay.setChecked(true);
                currentShippingCost = DeliveryMethod.NEXT_DAY.cost;
                calculateTotalPrice(getContext());
            }
        });

        if(Basket.getContents().size() > 0){
            emptyText.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams textMargin = (LinearLayout.LayoutParams) emptyText.getLayoutParams();
            //textMargin.setMargins(0, 0, 0, 0);
            emptyText.setLayoutParams(textMargin);
            recycler.setVisibility(View.VISIBLE);
            calculateTotalPrice(getContext());
        } else {
            LinearLayout.LayoutParams textMargin = (LinearLayout.LayoutParams) emptyText.getLayoutParams();
            int dpToPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.text_margin), getResources().getDisplayMetrics());
            textMargin.setMargins(dpToPx, dpToPx, dpToPx, dpToPx);
            emptyText.setLayoutParams(textMargin);
            recycler.setVisibility(View.INVISIBLE);
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    public static void calculateTotalPrice(Context c){
        Product.DBHelper prodHelper = new Product.DBHelper(c, "Product", null, 1);

        float price = currentShippingCost;
        for(int i: Basket.getContents().keySet()){
            price += prodHelper.getSpecificProduct(i).getPrice() * Basket.getContents().get(i);
        }

        totalPrice.setText("£"+new DecimalFormat("#.0#").format(price) + "\nincluding VAT of £"+new DecimalFormat("#.0#").format(price-(price/1.2f)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        masterView = inflater.inflate(R.layout.fragment_basket, container, false);
        return masterView;
    }
}