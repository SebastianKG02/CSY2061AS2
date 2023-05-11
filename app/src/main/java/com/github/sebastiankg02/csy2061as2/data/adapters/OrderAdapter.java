package com.github.sebastiankg02.csy2061as2.data.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sebastiankg02.csy2061as2.MainActivity;
import com.github.sebastiankg02.csy2061as2.R;
import com.github.sebastiankg02.csy2061as2.data.Order;
import com.github.sebastiankg02.csy2061as2.data.OrderStatus;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewOrderListFragment;
import com.github.sebastiankg02.csy2061as2.fragments.views.ViewSpecificOrderFragment;
import com.github.sebastiankg02.csy2061as2.user.User;
import com.google.android.material.snackbar.Snackbar;

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

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, view);
                popup.getMenuInflater().inflate(R.menu.order_click, popup.getMenu());

                popup.getMenu().getItem(popup.getMenu().size()-1).setVisible(false);
                if(currentOrder.getStatus() == OrderStatus.DELIVERED ||
                        currentOrder.getStatus() == OrderStatus.START_RETURN ||
                        currentOrder.getStatus() == OrderStatus.RETURNED ||
                        currentOrder.getStatus() == OrderStatus.CANCELLED ||
                        currentOrder.getStatus() == OrderStatus.NONE){
                    popup.getMenu().removeItem(R.id.cancelOrderOption);
                }

                if(currentOrder.getStatus() != OrderStatus.DELIVERED){
                    popup.getMenu().removeItem(R.id.startReturnOption);
                }

                if(currentOrder.getStatus() == OrderStatus.CANCELLED || currentOrder.getStatus() == OrderStatus.RETURNED){
                    popup.getMenu().getItem(popup.getMenu().size()-1).setVisible(true);
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()) {
                            case R.id.viewOrderContentsOption:
                                ViewSpecificOrderFragment.workingOrder = currentOrder;
                                Navigation.findNavController(view).navigate(R.id.action_viewOrderListFragment_to_viewSpecificOrderFragment);
                                break;
                            case R.id.startReturnOption:
                                AlertDialog returnDialog = new AlertDialog.Builder(context)
                                        .setTitle(R.string.return_title)
                                        .setMessage(R.string.return_desc)
                                        .setPositiveButton(R.string.return_confirm, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                currentOrder.setStatus(OrderStatus.START_RETURN);
                                                currentOrder.updateOrder(context, false);
                                                notifyItemChanged(holder.getAdapterPosition());
                                            }
                                        })
                                        .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        }).create();
                                returnDialog.show();
                                break;
                            case R.id.cancelOrderOption:
                                AlertDialog cancelDialog = new AlertDialog.Builder(context)
                                        .setTitle(R.string.cancel_title)
                                        .setMessage(R.string.cancel_desc)
                                        .setPositiveButton(R.string.cancel_confirm, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                currentOrder.setStatus(OrderStatus.CANCELLED);
                                                currentOrder.updateOrder(context, true);
                                                notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        }).create();
                                cancelDialog.show();
                                break;
                            case R.id.removeOrderOption:
                                AlertDialog removeDialog = new AlertDialog.Builder(context)
                                        .setTitle(R.string.menu_remove_order)
                                        .setMessage(R.string.remove_order_desc)
                                        .setPositiveButton(R.string.remove_order_confirm, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                currentOrder.delete(context);
                                                orders.remove(currentOrder);
                                                notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        }).create();
                                removeDialog.show();
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
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
