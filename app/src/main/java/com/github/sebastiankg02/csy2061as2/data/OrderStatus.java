package com.github.sebastiankg02.csy2061as2.data;

import com.github.sebastiankg02.csy2061as2.R;

/**
 * An enumeration of the possible statuses of an order.
 * Each status has an associated integer value, string resource ID, and color resource ID.
 * The possible statuses are:
 * - CREATED: The order has been created but not yet processed.
 * - PACKING: The order is being packed and prepared for dispatch.
 * - PRE_DISPATCH: The order is ready for dispatch but has not yet been dispatched.
 * - DISPATCHED: The order has been dispatched.
 * - DELIVERED: The order has been delivered.
 * - CANCELLED: The order has been cancelled.
 * - NONE: The order has no status. Users should NEVER see this status!
 */
public enum OrderStatus {
	//New order - just created
    CREATED(0, R.string.order_status_created, R.color.order_created),
	//Order acknowledged by warehouse, preparing for pre-dispatch
    PACKING(1, R.string.order_status_packing, R.color.order_packing),
	//Order packed, awaiting shipping
    PRE_DISPATCH(2, R.string.order_status_pre_dispatch, R.color.order_pre_dispatch),
	//Order on the way to the customer
    DISPATCHED(3, R.string.order_status_dispatch, R.color.order_dispatched),
	//Order reached customer & completed
    DELIVERED(4, R.string.order_status_delivered, R.color.order_delivered),
	//Order completed, Customer has started a return 
    START_RETURN(-1, R.string.order_status_start_return, R.color.order_start_return),
	//Return has been received by warehouse, completed
    RETURNED(-2, R.string.order_status_returned, R.color.order_returned),
    CANCELLED(-9999, R.string.order_status_cancelled, R.color.order_cancelled),
	//Error status - customers should never see this!
    NONE(9999, R.string.order_status_error, R.color.order_error);

    public int status;
    public int displayMessage;
    public int displayColour;

    /**
     * Constructs an OrderStatus enum with the given status, display message, and display colour.
     *
     * @param status The status of the order
     * @param displayMessage The message to display for the order status
     * @param colour The colour to display for the order status
     */
    private OrderStatus(int status, int displayMessage, int colour){
        this.status = status;
        this.displayMessage = displayMessage;
        this.displayColour = colour;
    }

    /**
     * Converts an integer value to an OrderStatus enum value. Will return NONE if OrderStatus is not found for a given value of s.
     *
     * @param s The integer value to convert.
     * @return The corresponding OrderStatus enum value.
     */
    public static OrderStatus fromInt(int s){
        switch(s){
            case -2:
                return RETURNED;
            case -1:
                return START_RETURN;
            case 0:
                return CREATED;
            case 1:
                return PACKING;
            case 2:
                return PRE_DISPATCH;
            case 3:
                return DISPATCHED;
            case 4:
                return DELIVERED;
            case -9999:
                return CANCELLED;
            default:
                return NONE;
        }
    }
}
