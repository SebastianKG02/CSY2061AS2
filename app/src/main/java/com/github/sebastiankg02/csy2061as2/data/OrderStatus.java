package com.github.sebastiankg02.csy2061as2.data;

/**
 * Simple enum for holding order status information, as well as a tring resource ID
 */
public enum OrderStatus {
	//New order - just created
    CREATED(0, 0),
	//Order acknowledged by warehouse, preparing for pre-dispatch
    PACKING(1, 0),
	//Order packed, awaiting shipping
    PRE_DISPATCH(2, 0),
	//Order on the way to the customer
    DISPATCHED(3, 0),
	//Order reached customer & completed
    DELIVERED(4, 0),
	//Order completed, Customer has started a return 
    START_RETURN(-1, 0),
	//Return has been recieved by warehouse, completed
    RETURNED(-2, 0),
	//Error status - customers should never see this!
    NONE(-9999, 0);

    public int status;
    public int displayMessage;

    private OrderStatus(int status, int displayMessage){
        this.status = status;
        this.displayMessage = displayMessage;
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
            default:
                return NONE;
        }
    }
}
