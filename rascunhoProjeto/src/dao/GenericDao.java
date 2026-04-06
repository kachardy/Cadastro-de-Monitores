package dao;

import javax.persistence.EntityManager;
import java.util.List;

public class GenericDao<T> {
    static EntityManager em;
    private Class<T> classe;

    public GenericDao (EntityManager em, Class<T> classe) {
        GenericDao.em = em;
        this.classe = classe;
    }

    public void salvar(T entidade) {
        try {
            em.getTransaction().begin();
            em.persist(entidade);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    public void atualizar(T entidade) {
        try {
            em.getTransaction().begin();
            em.merge(entidade);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    public void removerPorId(Long id) {
        try {
            em.getTransaction().begin();
            T entidade = em.find(classe, id);
            if (entidade != null) {
                em.remove(entidade);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    public List<T> listarTodos() {
        return em.createQuery("SELECT e FROM" + classe.getSimpleName() + " e", classe).getResultList();
    }

}
