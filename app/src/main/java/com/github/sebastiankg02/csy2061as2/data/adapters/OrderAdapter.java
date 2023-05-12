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

/**
 * An adapter for displaying a list of orders in a RecyclerView.
 *
 * orders An ArrayList of Order objects to display
 * context The context of the activity or fragment using this adapter
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    public ArrayList<Order> orders;

    /**
     * Constructs an OrderAdapter object with the given list of orders and context.
     *
     * @param orders The list of orders to be displayed in the adapter
     * @param c The context in which the adapter is being used
     */
    public OrderAdapter(ArrayList<Order> orders, Context c){
        this.context = c;
        this.orders = orders;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item = inflater.inflate(R.layout.item_order, parent, false);
        ViewHolder holder = new ViewHolder(item);
        return holder;
    }

    /**
     * Binds the data of an order to a ViewHolder, updating the products, requesting an order status update,
     * and setting the user information. Also sets the border color of the item layout based on the order status.
     *
     * @param holder The ViewHolder to bind the data to
     * @param position The position of the order in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order currentOrder = orders.get(position);
        currentOrder.updateProducts(context);
        currentOrder.requestOrderStatusUpdate(context);
        User.DBHelper userHelper = new User.DBHelper(context);
        User currentUser = userHelper.getSpecificUser(currentOrder.getUserID());

        GradientDrawable itemBorder = (GradientDrawable) holder.itemLayout.getBackground().mutate();
        itemBorder.setStroke(ViewOrderListFragment.dpToPxBorder,context.getColor(currentOrder.getStatus().displayColour));

        //Set the text for each field in the order view holder based on the current order.
        holder.orderNumber.setText("Order #" + currentOrder.getUserID() + currentUser.fullName.substring(0, 3).toUpperCase() + "-" + currentOrder.getId());
        holder.orderStatus.setText(currentOrder.getStatus().displayMessage);
        holder.orderStatus.setTextColor(context.getColor(currentOrder.getStatus().displayColour));
        holder.orderCreated.setText(User.formatter.format(currentOrder.getCreated()));
        holder.orderDeliveryMethod.setText(currentOrder.getDelivery().resString);
        holder.orderDueBy.setText(User.formatter.format(currentOrder.getCreated().plusDays(currentOrder.getDelivery().maximumDays)));
        holder.orderItems.setText(currentOrder.getOrderProductCount() + " total items in order.");
        holder.orderTotalPrice.setText(currentOrder.calculateTotalPrice(context));

        /**
         * Set an OnClickListener on the item layout of the holder. When clicked, a PopupMenu is displayed
         * with options that vary depending on the status of the current order. The options include viewing
         * the order contents, starting a return, cancelling the order, and removing the order.
         */
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
                            /**
                             * Navigates to the ViewSpecificOrderFragment to view the contents of the current order.
                             * Sets the current order to the working order in the ViewSpecificOrderFragment.
                             */
                            case R.id.viewOrderContentsOption:
                                ViewSpecificOrderFragment.workingOrder = currentOrder;
                                Navigation.findNavController(view).navigate(R.id.action_viewOrderListFragment_to_viewSpecificOrderFragment);
                                break;
                            /**
                             * Displays an alert dialog to confirm the user's intention to start a return for the current order.
                             */
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
                            /**
                             * Displays an alert dialog to confirm the cancellation of an order. If the user confirms the cancellation,
                             * the order status is updated to cancelled and the adapter is notified of the change.
                             */
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
                            /**
                             * Displays an alert dialog to confirm the removal of an order. If the user confirms the removal,
                             * the order is deleted from the context and the list of orders is updated.
                             */
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

    /**
     * Returns the number of items in the list of orders.
     *
     * @return The number of orders in the list.
     */
    @Override
    public int getItemCount() {
        return orders.size();
    }

    /*
     * Contains references to the various views that make up an order item, such as the order number,
     * status, created date, delivery method, due date, items in the order, and total price.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemLayout;
        public TextView orderNumber;
        public TextView orderStatus;
        public TextView orderCreated;
        public TextView orderDeliveryMethod;
        public TextView orderDueBy;
        public TextView orderItems;
        public TextView orderTotalPrice;

        /**
         * Constructs a ViewHolder object for an Order item object in the RecyclerView.
         *
         * @param itemView The view representing the order item
         */
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
