package com.github.sebastiankg02.csy2061as2.fragments.views;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061as2.MainActivity;
import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Order;
import com.github.sebastiankg02.csy2061as2.data.adapters.OrderAdapter;
import com.github.sebastiankg02.csy2061as2.data.adapters.OrderProductAdapter;
import com.github.sebastiankg02.csy2061as2.user.User;

/**
 * A fragment that displays the details of a specific order.
 * Contains various TextViews to display the order number, status, creation date, delivery method,
 * due date, number of items in the order, and the total price of the order.
 */
public class ViewSpecificOrderFragment extends Fragment {
    private View masterView;
    private LinearLayout itemLayout;
    private TextView orderNumber;
    private TextView orderStatus;
    private TextView orderCreated;
    private TextView orderDeliveryMethod;
    private TextView orderDueBy;
    private TextView orderItems;
    private TextView orderTotalPrice;
    private RecyclerView recycler;
    private OrderProductAdapter adapter;
    private Button backButton;
    public static Order workingOrder;

    /**
     * Constructs a new ViewSpecificOrderFragment object.
     * This constructor sets the layout resource of the fragment to R.layout.fragment_view_specific_order.
     */
    public ViewSpecificOrderFragment() {
        super(R.layout.fragment_view_specific_order);
    }

    /**
     * Called when the view is created. Initializes the UI elements and updates the working order.
     * Also sets up the OrderProduct adapter, which will load the order contents and display them in a user-friendly manner.
     *
     * @param v The view that was created
     * @param b The bundle containing any saved state information
     */
    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);

        workingOrder.updateProducts(getContext());
        workingOrder.requestOrderStatusUpdate(getContext());

        itemLayout = (LinearLayout) masterView.findViewById(R.id.orderItemLayout);
        orderNumber = (TextView) masterView.findViewById(R.id.specOrderIDText);
        orderStatus = (TextView) masterView.findViewById(R.id.orderStatusText);
        orderCreated = (TextView) masterView.findViewById(R.id.orderCreatedText);
        orderDeliveryMethod = (TextView) masterView.findViewById(R.id.orderDeliveryMethodText);
        orderDueBy = (TextView) masterView.findViewById(R.id.orderDueByText);
        orderItems = (TextView) masterView.findViewById(R.id.orderItemsInOrder);
        orderTotalPrice = (TextView) masterView.findViewById(R.id.orderTotalPriceText);
        backButton = (Button) masterView.findViewById(R.id.viewOrderListBackToBrowse);

        //Get OrderProducts from order
        recycler = (RecyclerView) masterView.findViewById(R.id.opRecycler);
        adapter = new OrderProductAdapter(getContext(), workingOrder);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        //Set border of order header in line with delivery status
        GradientDrawable itemBorder = (GradientDrawable) itemLayout.getBackground().mutate();
        itemBorder.setStroke(ViewOrderListFragment.dpToPxBorder,getContext().getColor(workingOrder.getStatus().displayColour));

        //Update order header contents based on order
        User.DBHelper userHelper = new User.DBHelper(getContext());
        User currentUser = userHelper.getSpecificUser(workingOrder.getUserID());
        orderNumber.setText("Order #" + workingOrder.getUserID() + currentUser.fullName.substring(0, 3).toUpperCase() + "-" + workingOrder.getId());
        orderStatus.setText(workingOrder.getStatus().displayMessage);
        orderStatus.setTextColor(getContext().getColor(workingOrder.getStatus().displayColour));
        orderCreated.setText(User.formatter.format(workingOrder.getCreated()));
        orderDeliveryMethod.setText(workingOrder.getDelivery().resString);
        orderDueBy.setText(User.formatter.format(workingOrder.getCreated().plusDays(workingOrder.getDelivery().maximumDays)));
        orderItems.setText(workingOrder.getOrderProductCount() + " total items in order.");
        orderTotalPrice.setText(workingOrder.calculateTotalPrice(getContext()));

        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles the click event for the back button in the MainActivity.
             * Pops the back stack and goes back to the previous fragment.
             *
             * @param view The view that was clicked
             */
            @Override
            public void onClick(View view) {
                MainActivity.globalNavigation.popBackStack();
            }
        });
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
        masterView = inflater.inflate(R.layout.fragment_view_specific_order, container, false);
        return masterView;
    }
}