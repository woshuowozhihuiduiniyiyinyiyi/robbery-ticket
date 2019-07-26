package com.hj.tj.gohome.utils;

import com.hj.tj.gohome.consts.OwnerConstants;

import java.util.Map;
import java.util.Objects;

public class OwnerContextHelper {
    private static ThreadLocal<Map<String, Object>> ownerThreadLocal = new ThreadLocal<>();

    public static void putOwner(Map<String, Object> map) {
        ownerThreadLocal.set(map);
    }

    public static Integer getOwnerId() {
        Map<String, Object> ownerMap = ownerThreadLocal.get();

        return Integer.parseInt(ownerMap.get(OwnerConstants.OWNER_ID).toString());
    }

    public static String getOwnerNickname() {
        Map<String, Object> ownerMap = ownerThreadLocal.get();

        return ownerMap.get(OwnerConstants.NICKNIME).toString();
    }

    public static String getFrom() {
        Map<String, Object> ownerMap = ownerThreadLocal.get();

        return ownerMap.get(OwnerConstants.FROM).toString();
    }

    public static boolean hasLogin() {
        Map<String, Object> ownerMap = ownerThreadLocal.get();

        if (Objects.isNull(ownerMap) || Objects.isNull(ownerMap.get(OwnerConstants.OWNER_ID))) {
            return false;
        }

        return true;
    }

}
