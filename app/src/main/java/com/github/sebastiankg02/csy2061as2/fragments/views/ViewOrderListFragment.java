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

public class ViewOrderListFragment extends Fragment {
    private View masterView;
    private TextView emptyText;
    private RecyclerView recycler;
    private Button backButton;
    private int dpToPx;
    public static int dpToPxBorder;
    private OrderAdapter adapter;

    public ViewOrderListFragment() {
        super(R.layout.fragment_view_order_list);
    }

    @Override
    public void onViewCreated(View v, Bundle b) {
        super.onViewCreated(v, b);

        dpToPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.text_margin), getResources().getDisplayMetrics());
        dpToPxBorder = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.text_padding_recycler_half), getResources().getDisplayMetrics());

        emptyText = (TextView) masterView.findViewById(R.id.viewOrderListText);
        recycler = (RecyclerView) masterView.findViewById(R.id.viewOrderListRecycler);
        backButton = (Button) masterView.findViewById(R.id.viewOrderListBackToBrowse);

        Order.DBHelper orderHelper = new Order.DBHelper(getContext());
        ArrayList<Order> userOrders = orderHelper.getAllOrdersForUser(MainActivity.currentLoggedInUser.id);

        adapter = new OrderAdapter(userOrders, getContext());
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.globalNavigation.popBackStack();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        masterView = inflater.inflate(R.layout.fragment_view_order_list, container, false);
        return masterView;
    }
}