package services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Persistencia {

    // A fábrica de conexões é estática para garantir o Singleton (instância única)
    private static EntityManagerFactory emf;

    static {
        try {
            emf = Persistence.createEntityManagerFactory("monitoriaPU");
        } catch (Exception e) {
            System.err.println("Erro ao inicializar a fábrica de persistência: " + e.getMessage());
        }
    }

    public static EntityManager getEntityManager() {
        if (emf == null) {
            throw new IllegalStateException("EntityManagerFactory não foi inicializada.");
        }
        return emf.createEntityManager();
    }

    public static void encerrar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}