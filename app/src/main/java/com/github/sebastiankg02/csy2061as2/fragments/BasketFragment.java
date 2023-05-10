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

import com.github.sebastiankg02.csy2061as2.MainActivity;
import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Basket;
import com.github.sebastiankg02.csy2061as2.data.DeliveryMethod;
import com.github.sebastiankg02.csy2061as2.data.Order;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.github.sebastiankg02.csy2061as2.data.adapters.BasketAdapter;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class BasketFragment extends Fragment {

    private View masterView;
    private static TextView emptyText;
    private static RecyclerView recycler;
    private BasketAdapter adapter;
    private TextView selectShippingText;
    private RadioButton shippingStandard;
    private RadioButton shippingExpress;
    private RadioButton shippingNextDay;
    private static TextView totalPrice;
    private Button placeOrderButton;
    private Button backButton;
    private static float currentShippingCost;
    private static int dpToPx;

    public BasketFragment() {
        super(R.layout.fragment_basket);

    }

    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);

        dpToPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.text_margin), getResources().getDisplayMetrics());

        emptyText = (TextView) masterView.findViewById(R.id.basketInfoText);
        recycler = (RecyclerView) masterView.findViewById(R.id.basketRecycler);
        adapter = new BasketAdapter(getContext());
        selectShippingText = (TextView) masterView.findViewById(R.id.selectShippingMethod);
        shippingStandard = (RadioButton) masterView.findViewById(R.id.basketShippingStandard);
        shippingExpress = (RadioButton) masterView.findViewById(R.id.basketShippingExpress);
        shippingNextDay = (RadioButton) masterView.findViewById(R.id.basketShippingNextDay);
        totalPrice = (TextView) masterView.findViewById(R.id.basketTotalPriceAmountText);
        placeOrderButton = (Button) masterView.findViewById(R.id.basketPlaceOrder);
        backButton = (Button) masterView.findViewById(R.id.basketBackToBrowse);

        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        shippingStandard.setChecked(true);
        currentShippingCost = DeliveryMethod.STANDARD.cost;
        calculateTotalPrice(getContext());

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

        updateBasketView(getContext());
        calculateTotalPrice(getContext());

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Basket.getContents().size() > 0) {
                    Order.DBHelper orderHelper = new Order.DBHelper(getContext(), "Order", null, 1);
                    DeliveryMethod dm = DeliveryMethod.NONE;

                    if (shippingStandard.isChecked()) {
                        dm = DeliveryMethod.STANDARD;
                    } else if (shippingExpress.isChecked()) {
                        dm = DeliveryMethod.EXPRESS;
                    } else if (shippingNextDay.isChecked()) {
                        dm = DeliveryMethod.NEXT_DAY;
                    }

                    orderHelper.addOrderFromBasket(getContext(), Basket.getContents(), MainActivity.currentLoggedInUser, dm);
                    Basket.emptyBasket();
                } else {
                    Snackbar.make(view, R.string.basket_empty, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.globalNavigation.popBackStack();
            }
        });
    }

    private static void updateBasketView(Context c){
        if(Basket.getContents().size() > 0){
            emptyText.setVisibility(View.GONE);
            LinearLayout.LayoutParams textMargin = (LinearLayout.LayoutParams) emptyText.getLayoutParams();
            //textMargin.setMargins(0, 0, 0, 0);
            emptyText.setLayoutParams(textMargin);
            recycler.setVisibility(View.VISIBLE);
        } else {
            LinearLayout.LayoutParams textMargin = (LinearLayout.LayoutParams) emptyText.getLayoutParams();
            textMargin.setMargins(dpToPx, dpToPx, dpToPx, dpToPx);
            emptyText.setLayoutParams(textMargin);
            recycler.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    public static void calculateTotalPrice(Context c){
        Product.DBHelper prodHelper = new Product.DBHelper(c, "Product", null, 1);

        float price = currentShippingCost;
        for(int i: Basket.getContents().keySet()){
            price += prodHelper.getSpecificProduct(i).getPrice() * Basket.getContents().get(i);
        }
        updateBasketView(c);
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