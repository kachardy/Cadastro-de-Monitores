package dao;

import models.Aluno;
import javax.persistence.EntityManager;
import java.util.List;

public class AlunoDao extends GenericDao<Aluno> {

    // No construtor, eu passo a classe Aluno para o GenericDao conseguir montar as queries automáticas.
    public AlunoDao(EntityManager em, Class<Aluno> classe) {
        super(em, classe);
    }

    // Busca exata pela matrícula. Útil para login ou validações de cadastro.
    public Aluno recuperarAlunoPorMatricula(String matricula) {
        try {
            return getEm().createQuery("SELECT a FROM Aluno a WHERE a.matricula = :matriculaRecebida", Aluno.class)
                    .setParameter("matriculaRecebida", matricula)
                    .getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            // Se não encontrar nada, devolvo null para tratar a lógica lá na Central.
            return null;
        }
    }

    // NOVO METODO: Aqui eu faço a busca "profissional" direto no banco.
    // Uso o LOWER para não ter problema com maiúsculas/minúsculas e o LIKE com '%'
    // para que funcione como um "contém" (busca qualquer parte do nome).
    public List<Aluno> buscarAlunosPorNome(String nome) {
        return getEm().createQuery("SELECT a FROM Aluno a WHERE LOWER(a.nome) LIKE :nomeBusca", Aluno.class)
                .setParameter("nomeBusca", "%" + nome.toLowerCase() + "%")
                .getResultList();
    }
}