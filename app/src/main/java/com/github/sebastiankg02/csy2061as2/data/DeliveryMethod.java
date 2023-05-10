package com.github.sebastiankg02.csy2061as2.data;

import com.github.sebastiankg02.csy2061as2.R;

public enum DeliveryMethod {
    NONE(-1, 0, 0.0f),
    STANDARD(0, R.string.shipping_std, 2.5f),
    EXPRESS(1, R.string.shipping_exp, 5.0f),
    NEXT_DAY(2, R.string.shipping_nxt, 7.5f);

    public int value;
    public int resString;
    public float cost;

    private DeliveryMethod(int v, int r, float c){
        this.value = v;
        this.resString = r;
        this.cost = c;
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
