package com.user;

import java.util.HashMap;
import java.util.Map;

public enum UserType {
    NEWCUSTOMER(0), COPPER(1), SILVER(2), GOLD(3), PLATINUM(4);

    private int value;
    private static Map<Integer, UserType> map = new HashMap<>();

    private UserType(int value) {
        this.value = value;
    }

    static {
        for (UserType userType : UserType.values()) {
            map.put(userType.value, userType);
        }
    }

    public static UserType valueOf(int userType) {
        return (UserType) map.get(userType);
    }

    public int getValue() {
        return value;
    }
}
