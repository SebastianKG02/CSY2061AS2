package com.github.sebastiankg02.csy2061as2.data;

public enum OrderStatus {
    CREATED(0, 0),
    PACKING(1, 0),
    PRE_DISPATCH(2, 0),
    DISPATCHED(3, 0),
    DELIVERED(4, 0),
    START_RETURN(-1, 0),
    RETURNED(-2, 0),
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
