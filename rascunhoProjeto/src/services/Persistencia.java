package services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

// Padrão Singleton
public class Persistencia {

    // Atributo estático privado que guarda a única instância da fábrica
    private static EntityManagerFactory emf;

    private Persistencia() {}

    // O synchronized garante que uma ou mais threads não criem EntityManager diferentes
    public static synchronized EntityManager getEntityManager() {
        if (emf == null) {
            try {
                // Criação tardia apenas na primeira vez que for necessário
                emf = Persistence.createEntityManagerFactory("monitoriaPU");
            } catch (Exception e) {
                System.err.println("Erro ao inicializar a fábrica de persistência: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return emf.createEntityManager();
    }

    // Método para fechar a fábrica graciosamente no encerramento do programa
    public static void fecharFabrica() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}