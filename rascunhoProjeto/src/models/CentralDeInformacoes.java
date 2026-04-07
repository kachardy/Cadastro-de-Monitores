package models;

import dao.AlunoDao;
import dao.GenericDao;
import dao.PessoaDao;
import erros.AlunoJaExisteException;
import erros.EditalJaExisteException;
import javax.persistence.EntityManager;
import java.util.List;

// Essa classe é um Facade
public class CentralDeInformacoes {

    private EntityManager em = Persistencia.getEntityManager();
    private AlunoDao alunoDao = new AlunoDao(em, Aluno.class);
    private PessoaDao pessoaDao = new PessoaDao(em, Pessoa.class);
    private GenericDao<EditalDeMonitoria> editalDao = new GenericDao<>(em, EditalDeMonitoria.class);
    private GenericDao<Coordenador> coordenadorDao = new GenericDao<>(em, Coordenador.class);

    public List<Aluno> getTodosOsAlunos() {
        return alunoDao.listarTodos();
    }

    public List<EditalDeMonitoria> getTodosOsEditais() {
        return editalDao.listarTodos();
    }

    // O coordenador pode ser buscado por uma regra específica ou ID fixo
    public Coordenador getCoordenador() {
        List<Coordenador> lista = coordenadorDao.listarTodos();
        return lista.isEmpty() ? null : lista.get(0);
    }

    public Aluno recuperarAlunoPorMatricula(String numMat) {
        return alunoDao.recuperarAlunoPorMatricula(numMat);
    }

    public Pessoa recuperarPessoaPorEmail(String email) {
        return pessoaDao.recuperarPessoaPorEmail(email);
    }

    public boolean adicionarAluno(Aluno a) throws AlunoJaExisteException {
        if (recuperarAlunoPorMatricula(a.getMatricula()) != null || recuperarPessoaPorEmail(a.getEmail()) != null) {
            throw new AlunoJaExisteException();
        }

        alunoDao.salvar(a);
        return true;
    }

    public boolean adicionarCoordenador(Coordenador c) {
        coordenadorDao.salvar(c);
        return true;
    }

    public boolean adicionarEdital(EditalDeMonitoria edital) throws EditalJaExisteException {
        if (recuperarEditalPeloId(edital.getId()) != null) {
            throw new EditalJaExisteException();
        }
        editalDao.salvar(edital);
        return true;
    }

    public EditalDeMonitoria recuperarEditalPeloId(long id) {
        try {
            return em.createQuery("SELECT e FROM EditalDeMonitoria e WHERE e.id = :id", EditalDeMonitoria.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public String percorrerEditais() {
        List<EditalDeMonitoria> editais = getTodosOsEditais();
        if (editais.isEmpty()) return "Nenhum edital";

        StringBuilder resultado = new StringBuilder();
        for (EditalDeMonitoria e : editais) {
            resultado.append("\n").append(e.toString());
        }
        return resultado.toString();
    }

    public List<Disciplina> recuperarInscricoesDeUmAlunoEmUmEdital(String matriculaAluno, long idEdital) {
        Aluno alunoEncontrado = recuperarAlunoPorMatricula(matriculaAluno);
        EditalDeMonitoria editalEncontrado = recuperarEditalPeloId(idEdital);

        if (alunoEncontrado == null || editalEncontrado == null) {
            return null;
        }
        // A lógica de busca de inscrições agora deve ser delegada ao Edital ou um InscricaoDAO
        return editalEncontrado.getGerenciador().getDisciplinasPorAluno(matriculaAluno);
    }
}