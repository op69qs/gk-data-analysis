package org.triber.analysis.util;

import java.util.UUID;

public class UuidUtil {

    public static String get32UUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }


    public static void main(String[] args) {
        int count = 885;
        for (int i = 0; i < count; i++) {
            System.out.println(get32UUID());
        }
    }
}
