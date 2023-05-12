package com.github.sebastiankg02.csy2061as2.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061as2.MainActivity;
import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Basket;
import com.github.sebastiankg02.csy2061as2.data.DeliveryMethod;
import com.github.sebastiankg02.csy2061as2.data.Order;
import com.github.sebastiankg02.csy2061as2.data.Product;
import com.github.sebastiankg02.csy2061as2.data.adapters.BasketAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;

/**
 * A fragment that displays the contents of a user's shopping basket.
 * The fragment contains a RecyclerView that displays the items in the basket,
 * as well as radio buttons for selecting the shipping method.
 */
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

    /**
     * Constructs a new BasketFragment object.
     * This constructor sets the layout resource of the fragment to R.layout.fragment_basket.
     */
    public BasketFragment() {
        super(R.layout.fragment_basket);
    }

    /**
     * Called when the view is created. Initializes the view components and sets up the click listeners.
     *
     * @param v The view that was created
     * @param b The bundle containing any saved state information
     */
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
        calculateTotalPrice(getContext(), true);

        /*
         * Set the text of the standard shipping method RadioButton and add an OnClickListener to it.
         * When clicked, it sets the current shipping method to standard and recalculates the total price,
         * as well as ensuring that the other delivery methods are not checked.
         */
        shippingStandard.setText(getResources().getString(DeliveryMethod.STANDARD.resString) + "\n£"+new DecimalFormat("#.0#").format(DeliveryMethod.STANDARD.cost));
        shippingStandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shippingStandard.setChecked(true);
                shippingExpress.setChecked(false);
                shippingNextDay.setChecked(false);
                currentShippingCost = DeliveryMethod.STANDARD.cost;
                calculateTotalPrice(getContext(), true);
            }
        });

        /*
         * Set the text of the express shipping method RadioButton and add an OnClickListener to it.
         * When clicked, it sets the current shipping method to express and recalculates the total price,
         * as well as ensuring that the other delivery methods are not checked.
         */
        shippingExpress.setText(getResources().getString(DeliveryMethod.EXPRESS.resString) + "\n£"+new DecimalFormat("#.0#").format(DeliveryMethod.EXPRESS.cost));
        shippingExpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shippingStandard.setChecked(false);
                shippingExpress.setChecked(true);
                shippingNextDay.setChecked(false);
                currentShippingCost = DeliveryMethod.EXPRESS.cost;
                calculateTotalPrice(getContext(), true);
            }
        });

        /*
         * Set the text of the next day shipping method RadioButton and add an OnClickListener to it.
         * When clicked, it sets the current shipping method to next day and recalculates the total price,
         * as well as ensuring that the other delivery methods are not checked.
         */
        shippingNextDay.setText(getResources().getString(DeliveryMethod.NEXT_DAY.resString) + "\n£"+new DecimalFormat("#.0#").format(DeliveryMethod.NEXT_DAY.cost));
        shippingNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shippingStandard.setChecked(false);
                shippingExpress.setChecked(false);
                shippingNextDay.setChecked(true);
                currentShippingCost = DeliveryMethod.NEXT_DAY.cost;
                calculateTotalPrice(getContext(), true);
            }
        });

        updateBasketView(getContext());
        calculateTotalPrice(getContext(), true);

        /*
         * Set an OnClickListener on the place order button. When clicked, check if the basket is empty or not.
         * If the basket is not empty, create a new order and add it to the database. Then empty the basket and
         * navigate to the View Categories Fragment. If the basket is empty, display a Snackbar info message.
         */
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Basket.getContents().size() > 0) {
                    Order.DBHelper orderHelper = new Order.DBHelper(getContext());
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
                    MainActivity.globalNavigation.navigate(R.id.action_basketFragment_to_viewCategoriesFragment);
                    Snackbar.make(view, R.string.basket_order_success, Snackbar.LENGTH_SHORT).show();
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

    /**
     * Eithr display basket contents or notify the user that their basket is empty.
     *
     * @param c The context of the activity or fragment calling this method.
     */
    private static void updateBasketView(Context c){
        if(Basket.getContents().size() > 0){
            emptyText.setVisibility(View.GONE);
            LinearLayout.LayoutParams textMargin = (LinearLayout.LayoutParams) emptyText.getLayoutParams();
            textMargin.setMargins(0, 0, 0, 0);
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

    /**
     * Calculates the total price of the items in the basket, including shipping and VAT.
     *
     * @param c The context of the application.
     * @param setTotalPrice Whether or not to set the total price in the UI.
     * @return The total price of the items in the basket, including shipping and VAT.
     */
    public static String calculateTotalPrice(Context c, boolean setTotalPrice){
        Product.DBHelper prodHelper = new Product.DBHelper(c);

        float price = currentShippingCost;
        for(int i: Basket.getContents().keySet()){
            price += prodHelper.getSpecificProduct(i).getPrice() * Basket.getContents().get(i);
        }

        updateBasketView(c);

        String output = "£" + new DecimalFormat("#.0#").format(price) + "\nincluding VAT of £" + new DecimalFormat("#.0#").format(price - (price / 1.2f));

        if(setTotalPrice) {
            totalPrice.setText(output);
        }

        return output;
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
        masterView = inflater.inflate(R.layout.fragment_basket, container, false);
        return masterView;
    }
}