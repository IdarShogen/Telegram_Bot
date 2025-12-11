package org.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserStateService {
    private static final Map<Long, UserState> state = new ConcurrentHashMap<Long, UserState>();

    public static void setState(long chatId, UserState userState) {
        state.put(chatId, userState);
    }
    public static UserState getState(long chatId) {
        return state.getOrDefault(chatId, UserState.IDLE);
    }

    public static void clearState(long chatId) {
        state.remove(chatId);
    }
}
