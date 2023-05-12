package com.github.sebastiankg02.csy2061as2.fragments.views;

import android.os.Bundle;
import android.util.TypedValue;
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

import java.util.ArrayList;

/**
 * A fragment that displays a list of all of a users' orders.
 * Contains a master view, an empty text view, a recycler view, a back button, and an adapter.
 * The dpToPx and dpToPxBorder variables are used for layout border calculations.
 */
public class ViewOrderListFragment extends Fragment {
    private View masterView;
    private TextView emptyText;
    private RecyclerView recycler;
    private Button backButton;
    private int dpToPx;
    public static int dpToPxBorder;
    private OrderAdapter adapter;

    /**
     * Constructs a new ViewOrderListFragment object.
     * This constructor sets the layout resource file to be used by the fragment.
     */
    public ViewOrderListFragment() {
        super(R.layout.fragment_view_order_list);
    }

    /**
     * Called when the view is created. Initializes the view components and sets up the click listener for the back button.
     *
     * @param v The view that was created
     * @param b The bundle containing any saved state information
     */
    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);

        dpToPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.text_margin), getResources().getDisplayMetrics());
        dpToPxBorder = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.text_padding_recycler_half), getResources().getDisplayMetrics());

        emptyText = (TextView) masterView.findViewById(R.id.viewOrderListText);
        recycler = (RecyclerView) masterView.findViewById(R.id.viewOrderListRecycler);
        backButton = (Button) masterView.findViewById(R.id.viewOrderListBackToBrowse);

        //Load orders for this user 
        Order.DBHelper orderHelper = new Order.DBHelper(getContext());
        ArrayList<Order> userOrders = orderHelper.getAllOrdersForUser(MainActivity.currentLoggedInUser.id);

        adapter = new OrderAdapter(userOrders, getContext());
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        /*
         * Check if there are any user orders and set the visibility of the empty text and recycler view accordingly.
         * If there are no user orders, the empty text is displayed with a margin and the recycler view is hidden.
         */
        if(userOrders.size() > 0){
            emptyText.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        } else {
            LinearLayout.LayoutParams textMargin = (LinearLayout.LayoutParams) emptyText.getLayoutParams();
            textMargin.setMargins(dpToPx, dpToPx, dpToPx, dpToPx);
            emptyText.setLayoutParams(textMargin);
            recycler.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }

        //Set an OnClickListener on the back button to pop the back stack of the global navigation.
        backButton.setOnClickListener(new View.OnClickListener() {
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
        masterView = inflater.inflate(R.layout.fragment_view_order_list, container, false);
        return masterView;
    }
}