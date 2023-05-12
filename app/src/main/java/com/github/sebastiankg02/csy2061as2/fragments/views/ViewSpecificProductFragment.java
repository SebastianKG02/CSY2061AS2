package com.github.sebastiankg02.csy2061as2.fragments.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.github.sebastiankg02.csy2061as2.MainActivity;
import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Basket;
import com.github.sebastiankg02.csy2061as2.dialog.product.ProductChangeCategoryDialog;
import com.github.sebastiankg02.csy2061as2.dialog.product.ProductChangeDescDialog;
import com.github.sebastiankg02.csy2061as2.dialog.product.ProductChangeNameDialog;
import com.github.sebastiankg02.csy2061as2.dialog.product.ProductChangePriceDialog;
import com.github.sebastiankg02.csy2061as2.dialog.product.ProductChangeStockDialog;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;

/**
 * A fragment that displays the details of a specific product.
 * Contains various TextViews and ImageButtons for adjusting the quantity of the product.
 */
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

    /**
     * Constructs a new ViewSpecificProductFragment object.
     * This constructor sets the layout resource used by the fragment to R.layout.fragment_view_product.
     */
    public ViewSpecificProductFragment() {
        super(R.layout.fragment_view_product);
    }

    /**
     * Called when the view is created. Initializes the UI elements and sets up functionality of all buttons.
     * If the user is an admin, they will also be shown a popup menu upon tapping on the admin button, 
     * which will allow the admin to manage it.
     *
     * @param v The view that was created
     * @param b The bundle containing any saved state information
     */
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

        //Update product text fields
        categoryText.setText(ViewCategoriesFragment.currentCategoryTitle);
        productNameText.setText(ViewProductListFragment.currentProductViewing.getName());
        productDescText.setText(ViewProductListFragment.currentProductViewing.getDesc());
        priceText.setText("£"+new DecimalFormat("#.0#").format(ViewProductListFragment.currentProductViewing.getPrice())+" per unit");

        /*
         * Determines if the current product has stock and updates the stock text view accordingly.
         * If the product is out of stock, the stock text view will display "Out of stock" in red.
         * If the product has stock, the stock text view will display the number of items in stock.
         */
        boolean hasStock = true;
        if(ViewProductListFragment.currentProductViewing.getStockLevel() <= 0){
            hasStock = false;
            stockText.setTextColor(getContext().getColor(R.color.red));
            stockText.setText(R.string.out_of_stock);
        } else {
            stockText.setText(String.valueOf(ViewProductListFragment.currentProductViewing.getStockLevel()) + " in stock.");
        }

        final boolean hasStockBasket = hasStock;
        addToBasketButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Add product to user basket if stock is present.
             *
             * @param view The view that was clicked
             */
            @Override
            public void onClick(View view) {
                if(hasStockBasket){
                    Basket.init();
                    Basket.addToBasket(ViewProductListFragment.currentProductViewing, Integer.valueOf(quantityText.getText().toString()));
                }
            }
        });

        //Set an OnClickListener on the back button to clear the current product viewing and pop the back stack.
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
                /**
                 * Creates a popup menu for a product, with options to change the name, description, category, stock, and price.
                 * Removes the option to view the product page and create a new product. (As these are not applicable in the product viewer)
                 *
                 * @param view The view that was clicked to open the popup menu
                 */
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(getContext(), view);
                    popup.getMenuInflater().inflate(R.menu.product_click, popup.getMenu());
                    popup.getMenu().removeItem(R.id.viewProductPageOption);
                    popup.getMenu().removeItem(R.id.adminProductCreate);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.adminProductChangeName:
                                    ProductChangeNameDialog.createDialog(getContext(), (ViewGroup) v, ViewProductListFragment.currentProductViewing).create().show();
                                    return true;
                                case R.id.adminProductChangeDescription:
                                    ProductChangeDescDialog.createDialog(getContext(), (ViewGroup) v, ViewProductListFragment.currentProductViewing).create().show();
                                    return true;
                                case R.id.adminProductChangeCategory:
                                    ProductChangeCategoryDialog.createDialog(getContext(), (ViewGroup) v, ViewProductListFragment.currentProductViewing).create().show();
                                    return true;
                                case R.id.adminProductChangeStock:
                                    ProductChangeStockDialog.createDialog(getContext(), (ViewGroup) v, ViewProductListFragment.currentProductViewing).create().show();
                                    return true;
                                case R.id.adminProductChangePrice:
                                    ProductChangePriceDialog.createDialog(getContext(), (ViewGroup) v, ViewProductListFragment.currentProductViewing).create().show();
                                    return true;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });
        }

        /*
         * Set an OnClickListener on the increment button to increase the quantity of the product by 1.
         * If the quantity is less than the current stock level, the quantity is incremented and the total price is updated.
         * This will NOT add it to the basket!
         */
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

        /*
         * Set an OnClickListener on the decrement button to decrease the quantity of an item by 1.
         * If the quantity is greater than 1, the quantity is updated and the total price is recalculated.
         * This will NOT add it to the basket!
         */
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

    /**
     * Updates the total price displayed on the UI based on the quantity of the product selected.
     *
     * @param quantity The quantity of the product selected.
     */
    public void updateTotalPrice(int quantity){
        totalPriceText.setText("£"+new DecimalFormat("#.0#").format((float) quantity * ViewProductListFragment.currentProductViewing.getPrice()));
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     *                  The fragment should not add the view itself, but this can be used to generate the
     *                  LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        masterView = inflater.inflate(R.layout.fragment_view_product, container, false);
        return masterView;
    }
}