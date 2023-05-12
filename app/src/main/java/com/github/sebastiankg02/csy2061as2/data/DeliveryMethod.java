package com.github.sebastiankg02.csy2061as2.data;

import com.github.sebastiankg02.csy2061as2.R;

/**
 * An enumeration of delivery methods, each with a unique value, 
 * a resource string, a cost, a minimum  & maximum number of expected delivery days.
 * The NONE delivery method is used as a default value.
 */
public enum DeliveryMethod {
    NONE(-1, 0, 0.0f, 1, 9999),
    STANDARD(0, R.string.shipping_std, 2.5f, 3, 5),
    EXPRESS(1, R.string.shipping_exp, 5.0f, 2, 3),
    NEXT_DAY(2, R.string.shipping_nxt, 7.5f, 1, 1);

    public int value;
    public int resString;
    public float cost;
    public int minimumDays;
    public int maximumDays;

    /**
     * Constructor for a DeliveryMethod enumeration.
     *
     * @param v The value of the delivery method.
     * @param r The resource string of the delivery method.
     * @param c The cost of the delivery method.
     * @param minDays The minimum number of days for delivery.
     * @param maxDays The maximum number of days for delivery.
     */
    private DeliveryMethod(int v, int r, float c, int minDays, int maxDays){
        this.value = v;
        this.resString = r;
        this.cost = c;
        this.minimumDays = minDays;
        this.maximumDays = maxDays;
    }

    /**
     * Returns the DeliveryMethod enum value corresponding to the given integer value.
     *
     * @param v The integer value to convert to a DeliveryMethod enum value.
     * @return The DeliveryMethod enum value corresponding to the given integer value.
     *         If the integer value is not recognized, returns NONE.
     */
    public static DeliveryMethod fromInt(int v){
        switch(v){
            case 0:
                return STANDARD;
            case 1:
                return EXPRESS;
            case 2:
                return NEXT_DAY;
            default:
                return NONE;
        }
    }
}
