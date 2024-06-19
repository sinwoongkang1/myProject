package com.example.myproject;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private JPAUtil(){
    }
    private static final EntityManagerFactory emfInstance =
            Persistence.createEntityManagerFactory("UserPU");

    public static EntityManagerFactory getEntityManagerFactory() {
        return emfInstance;
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(()-> {
            if (emfInstance != null) {
                System.out.println("Shutting down");
                emfInstance.close();
            }
        }));
    }
}
