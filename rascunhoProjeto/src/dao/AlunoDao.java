package dao;

import models.Aluno;

import javax.persistence.EntityManager;

public class AlunoDao extends GenericDao {

    public AlunoDao(EntityManager em, Class classe) {
        super(em, classe);
    }

    public Aluno recuperarAlunoPorMatricula(String matricula) {
        try {
            return getEm().createQuery("SELECT a FROM Aluno a WHERE a.matricula = :matriculaRecebida", Aluno.class)
                    .setParameter("matriculaRecebida", matricula)
                    .getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null;
        }
    }

}
