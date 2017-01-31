package tech.receipts.components;

import java.util.HashMap;
import java.util.Map;

import tech.receipts.R;

public enum DayNightMode {

    UNKNOWN(0, R.string.enum_daynight_unknown),
    AUTO(1, R.string.enum_daynight_auto),
    DAY(2, R.string.enum_daynight_day),
    NIGHT(3, R.string.enum_daynight_night),
    FOLLOW_SYSTEM(4, R.string.enum_daynight_system);

    private static final Map<Integer, DayNightMode> VALUES = new HashMap<>();

    static {
        for (DayNightMode mode : values()) {
            VALUES.put(mode.getValue(), mode);
        }
    }

    private int value;
    private int resourceId;

    DayNightMode(int i, int res) {
        value = i;
        resourceId = res;
    }

    public int getValue() {
        return value;
    }

    public int getResourceId() {
        return resourceId;
    }

    public static DayNightMode get(Integer value) {
        if (VALUES.containsKey(value)) {
            return VALUES.get(value);
        }

        return UNKNOWN;
    }
}
