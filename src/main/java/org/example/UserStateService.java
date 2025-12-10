package org.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserStateService {
    private static final Map<Long, UserState> state = new ConcurrentHashMap<Long, UserState>();

}
