package com.github.sebastiankg02.csy2061as2.data.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Order;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewOrderListFragment;
import com.github.sebastiankg02.csy2061as2.user.User;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    public ArrayList<Order> orders;

    public OrderAdapter(ArrayList<Order> orders, Context c){
        this.context = c;
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.item_order, parent, false);
        ViewHolder holder = new ViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order currentOrder = orders.get(position);
        currentOrder.updateProducts(context);
        currentOrder.requestOrderStatusUpdate(context);
        User.DBHelper userHelper = new User.DBHelper(context, "User", null, 1);
        User currentUser = userHelper.getSpecificUser(currentOrder.getUserID());

        GradientDrawable itemBorder = (GradientDrawable) holder.itemLayout.getBackground().mutate();
        itemBorder.setStroke(ViewOrderListFragment.dpToPxBorder,context.getColor(currentOrder.getStatus().displayColour));

        holder.orderNumber.setText("Order #" + currentOrder.getUserID() + currentUser.fullName.substring(0, 3).toUpperCase() + "-" + currentOrder.getId());
        holder.orderStatus.setText(currentOrder.getStatus().displayMessage);
        holder.orderStatus.setTextColor(context.getColor(currentOrder.getStatus().displayColour));
        holder.orderCreated.setText(User.formatter.format(currentOrder.getCreated()));
        holder.orderDeliveryMethod.setText(currentOrder.getDelivery().resString);
        holder.orderDueBy.setText(User.formatter.format(currentOrder.getCreated().plusDays(currentOrder.getDelivery().maximumDays)));
        holder.orderItems.setText(currentOrder.getOrderProductCount() + " total items in order.");
        holder.orderTotalPrice.setText(currentOrder.calculateTotalPrice(context));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemLayout;
        public TextView orderNumber;
        public TextView orderStatus;
        public TextView orderCreated;
        public TextView orderDeliveryMethod;
        public TextView orderDueBy;
        public TextView orderItems;
        public TextView orderTotalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemLayout = (LinearLayout) itemView.findViewById(R.id.orderItemLayout);
            orderNumber = (TextView) itemView.findViewById(R.id.orderNumberText);
            orderStatus = (TextView) itemView.findViewById(R.id.orderStatusText);
            orderCreated = (TextView) itemView.findViewById(R.id.orderCreatedText);
            orderDeliveryMethod = (TextView) itemView.findViewById(R.id.orderDeliveryMethodText);
            orderDueBy = (TextView) itemView.findViewById(R.id.orderDueByText);
            orderItems = (TextView) itemView.findViewById(R.id.orderItemsInOrder);
            orderTotalPrice = (TextView) itemView.findViewById(R.id.orderTotalPriceText);
        }
    }
}
