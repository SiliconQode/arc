package dev.siliconcode.arc.datamodel;

import com.google.common.collect.Maps;

import java.util.Map;

public enum SnapshotType {

    FULL(1),
    PARTIAL(2);

    private final int value;
    private static Map<Integer, SnapshotType> map = Maps.newHashMap();

    static {
        map.put(1, FULL);
        map.put(2, PARTIAL);
    }

    SnapshotType(int value) { this.value = value; }

    public int value() { return this.value; }

    public static SnapshotType fromValue(int value) { return map.get(value); }
}
