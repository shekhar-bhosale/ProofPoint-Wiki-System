package com.proofpoint.wikisystem.util;

import com.proofpoint.wikisystem.model.AccessType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_FAILED_WITH_MESSAGE = "Operation FAILED Message:";

    public static Map<Action, List<AccessType>> authorizedActionsMap;

    static {
        authorizedActionsMap = new HashMap<>();

        List<AccessType> read = new ArrayList<>();
        read.add(AccessType.READ_WRITE);
        read.add(AccessType.READ_ONLY);
        authorizedActionsMap.put(Action.READ, read);

        List<AccessType> update = new ArrayList<>();
        update.add(AccessType.READ_WRITE);
        authorizedActionsMap.put(Action.UPDATE, update);

        List<AccessType> delete = new ArrayList<>();
        delete.add(AccessType.READ_WRITE);
        authorizedActionsMap.put(Action.DELETE, delete);
    }
}
