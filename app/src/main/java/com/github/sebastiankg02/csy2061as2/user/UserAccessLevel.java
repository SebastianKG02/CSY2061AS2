package com.github.sebastiankg02.csy2061as2.user;

/**
 * An enumeration of user access levels.
 * NONE: -1
 * USER: 0
 * ADMIN: 1
 */
public enum UserAccessLevel {
    NONE(-1),
    USER(0),
    ADMIN(1);

    public int value;
    /**
     * Constructor for the UserAccessLevel enum.
     *
     * @param l The integer value associated with the access level.
     */
    private UserAccessLevel(int l){
        this.value = l;
    }

    /**
     * Returns the UserAccessLevel corresponding to the given integer value.
     *
     * @param i The integer value to convert to a UserAccessLevel
     * @return The UserAccessLevel corresponding to the given integer value
     *         USER if i is 0, ADMIN if i is 1, and NONE otherwise
     */
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
