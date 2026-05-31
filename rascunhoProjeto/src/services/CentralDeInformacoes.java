package services;

import dao.*;
import erros.AlunoJaExisteException;
import erros.EditalJaExisteException;
import models.*;
import java.util.List;

// Essa classe funciona como o nosso Facade (Fachada) para o sistema.
public class CentralDeInformacoes {

    // Usando a Factory
    private AlunoDao alunoDao = DaoFactory.getAlunoDAO();
    private PessoaDao pessoaDao = DaoFactory.getPessoaDAO();
    private EditalMongoDao editalMongoDao = DaoFactory.getEditalMongoDAO();
    private GenericDao<Coordenador> coordenadorDao = DaoFactory.getDAO(Coordenador.class);
    private DisciplinaService disciplinaService = new DisciplinaService(editalMongoDao);

    // --- MÉTODOS DE BUSCA (READ) ---

    public List<Aluno> getTodosOsAlunos() {
        return alunoDao.listarTodos();
    }

    public List<EditalDeMonitoria> getTodosOsEditais() {
        return editalMongoDao.listarTodos();
    }

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

    public EditalDeMonitoria recuperarEditalPeloId(long id) {
        return editalMongoDao.recuperarEditalPeloId(id);
    }

    // --- MÉTODOS DE PERSISTÊNCIA---

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

    // Mantenho este metodo para o cadastro inicial, pois ele valida se o Edital já existe
    // e evita duplicidade acidental na hora da criação.

    public boolean adicionarEdital(EditalDeMonitoria edital) throws EditalJaExisteException {
        if (recuperarEditalPeloId(edital.getId()) != null) {
            throw new EditalJaExisteException();
        }
        editalMongoDao.salvar(edital);
        return true;
    }

    // NOVO METODO: Este é o nosso "Smart Save".
    // Como o mEtodo salvar() do GenericDao agora usa o merge(), este metodo serve
    // tanto para salvar um edital novo quanto para atualizar um que já existe.
    // É o metodo que vou usar nos Controllers para confirmar inscrições e resultados.
    public void salvarEdital(EditalDeMonitoria edital) {
        editalMongoDao.salvar(edital);
    }

    // --- MÉTODOS DE LÓGICA E RELATÓRIOS ---

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

        // Uso o gerenciador interno do edital para filtrar o que eu preciso.
        return editalEncontrado.getGerenciador().getDisciplinasPorAluno(
                editalEncontrado.getInscricoesRealizadas(), matriculaAluno);
    }

    public List<Aluno> buscarAlunosPorNome(String nome) {
        // A Central apenas pede para o DAO fazer a busca filtrada no banco
        return alunoDao.buscarAlunosPorNome(nome);
    }

    public Disciplina recuperarDisciplinaPorNome(String nome) {
        try {
            return disciplinaService.buscarPorNome(nome);
        } catch (Exception e) {
            System.out.println("Erro ao buscar disciplina no Redis/Mongo");
            e.printStackTrace();
            return null;
        }
    }

}
