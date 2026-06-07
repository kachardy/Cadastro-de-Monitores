package dao;

import javax.persistence.EntityManager;
import java.util.List;

public class GenericDao<T> {
    private EntityManager em;
    private Class<T> classe;

    public GenericDao (EntityManager em, Class<T> classe) {
        this.em = em;
        this.classe = classe;
    }

    protected EntityManager getEm() {
        return this.em;
    }

    // Agora retorna a entidade gerenciada (T)
    public T salvar(T entidade) {
        try {
            em.getTransaction().begin();
            // O merge retorna a instância gerenciada. Salvamos ela na variável.
            T entidadeGerenciada = em.merge(entidade);
            em.getTransaction().commit();
            return entidadeGerenciada; // Retorna o objeto pronto para uso seguro
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            // ADICIONADO: Lança a exceção para que o sistema saiba que o salvamento falhou
            throw new RuntimeException("Erro ao salvar/atualizar registro.", e);
        }
    }

    public List<T> listarTodos() {
        return em.createQuery("SELECT e FROM " + classe.getSimpleName() + " e", classe).getResultList();
    }
}