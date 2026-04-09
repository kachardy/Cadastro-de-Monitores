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

    // Unifiquei o salvar e o atualizar aqui. O merge() é esperto:
    // se o objeto não existe no banco, ele cria (INSERT); se já existe, ele atualiza (UPDATE).
    public void salvar(T entidade) {
        try {
            em.getTransaction().begin();
            // Agora uso o merge como meu "salvamento padrão" para evitar erros de
            // objeto duplicado ou objeto destacado (detached) na memória.
            em.merge(entidade);
            em.getTransaction().commit();
        } catch (Exception e) {
            // Se der qualquer problema no banco (como uma matrícula duplicada),
            // eu dou o rollback para não deixar a transação "pendurada" ou corromper os dados.
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace(); // Deixo o erro no console pra eu conseguir debugar depois.
        }
    }

    // Removi o método atualizar() daqui porque o salvar() com merge já resolve tudo.
    // Menos código para manter e menos chance de erro nos Controllers.

    public void removerPorId(Long id) {
        try {
            em.getTransaction().begin();
            T entidade = em.find(classe, id);
            if (entidade != null) {
                em.remove(entidade);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    // Busca simples de todos os registros da tabela usando o nome da classe.
    public List<T> listarTodos() {
        return em.createQuery("SELECT e FROM " + classe.getSimpleName() + " e", classe).getResultList();
    }
}