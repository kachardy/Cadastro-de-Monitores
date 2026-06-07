package dao;

import models.Pessoa;

import javax.persistence.EntityManager;

public class PessoaDao extends GenericDao<Pessoa>{

    public PessoaDao(EntityManager em, Class classe) {
        super(em, classe);
    }

    public Pessoa recuperarPessoaPorEmail (String email) {
        try {
            return getEm().createQuery("SELECT p FROM Pessoa p WHERE p.email = :emailRecebido", Pessoa.class)
                    .setParameter("emailRecebido", email)
                    .getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null;
        }
    }
}
