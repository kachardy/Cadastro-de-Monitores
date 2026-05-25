package dao;

import javax.persistence.EntityManager;

import models.Aluno;
import models.Pessoa;
import services.Persistencia;

public class DaoFactory {

    // Método de fábrica estático para criar instâncias de DAOs
    public static <T> GenericDao<T> getDAO(Class<T> classeEntidade) {
        // Obtém o EntityManager único gerado pelo Singleton de Persistência
        EntityManager em = Persistencia.getEntityManager();

        // Retorna o DAO específico com base na classe que foi solicitada
        return new GenericDao<>(em, classeEntidade);
    }

    public static PessoaDao getPessoaDAO() {
        return new PessoaDao(Persistencia.getEntityManager(), Pessoa.class);
    }

    public static AlunoDao getAlunoDAO() {
        return new AlunoDao(Persistencia.getEntityManager(), Aluno.class);
    }

    public static EditalDao getEditalDAO() {
        return new EditalDao(Persistencia.getEntityManager());
    }
}