package com.github.sebastiankg02.csy2061as2.user;

public enum UserAccessLevel {
    NONE(-1),
    USER(0),
    ADMIN(1);

    public int value;
    private UserAccessLevel(int l){
        this.value = l;
    }

    public static UserAccessLevel getLevelFromInt(int i){
        if(i == 0){
            return USER;
        }else if(i == 1){
            return ADMIN;
        }else {
            return NONE;
        }
    }
}
