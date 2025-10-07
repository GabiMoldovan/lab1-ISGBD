package org.example.database;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class JPAUtil {
    private static EntityManagerFactory emf;

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            Dotenv dotenv = Dotenv.load();

            Map<String, String> props = new HashMap<>();
            props.put("jakarta.persistence.jdbc.url", dotenv.get("DATABASE_URL"));
            props.put("jakarta.persistence.jdbc.user", dotenv.get("DB_USER"));
            props.put("jakarta.persistence.jdbc.password", dotenv.get("DB_PASSWORD"));

            emf = Persistence.createEntityManagerFactory("my-persistence-unit", props);
        }
        return emf;
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void close() {
        if (emf != null) emf.close();
    }
}
