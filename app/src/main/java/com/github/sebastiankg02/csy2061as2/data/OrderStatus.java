package com.github.sebastiankg02.csy2061as2.data;

import com.github.sebastiankg02.csy2061as2.R;

/**
 * Simple enum for holding order status information, as well as a tring resource ID
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

    private OrderStatus(int status, int displayMessage, int colour){
        this.status = status;
        this.displayMessage = displayMessage;
        this.displayColour = colour;
    }

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
