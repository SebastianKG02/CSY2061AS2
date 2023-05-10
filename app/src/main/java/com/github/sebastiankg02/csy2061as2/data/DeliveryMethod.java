package com.github.sebastiankg02.csy2061as2.data;

import com.github.sebastiankg02.csy2061as2.R;

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

    private DeliveryMethod(int v, int r, float c, int minDays, int maxDays){
        this.value = v;
        this.resString = r;
        this.cost = c;
        this.minimumDays = minDays;
        this.maximumDays = maxDays;
    }

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
