package dao;

import models.Pessoa;

import javax.persistence.EntityManager;

public class PessoaDao extends GenericDao{
    public PessoaDao(EntityManager em, Class classe) {
        super(em, classe);
    }

    public Pessoa recuperarPessoaPorEmail (String email) {
        try {
            return em.createQuery("SELECT p FROM Pessoa p WHERE p.email = :emailRecebido", Pessoa.class)
                    .setParameter("emailRecebida", email)
                    .getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null;
        }
    }
}
