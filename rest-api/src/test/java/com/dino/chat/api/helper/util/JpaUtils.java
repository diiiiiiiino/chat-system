package com.dino.chat.api.helper.util;

import jakarta.persistence.EntityManager;

public class JpaUtils {
    public static void flushAndClear(EntityManager entityManager) {
        entityManager.flush();
        entityManager.clear();
    }
}
