package services;

import dao.*;
import erros.AlunoJaExisteException;
import erros.EditalJaExisteException;
import models.*;
import java.util.List;

// Essa classe funciona como o nosso Facade (Fachada) para o sistema.
public class CentralDeInformacoes {

    private CoordenadorService coordenadorService = new CoordenadorService(DaoFactory.getDAO(Coordenador.class));
    private PessoaService pessoaService = new PessoaService(DaoFactory.getPessoaDAO());
    private AlunoService alunoService = new AlunoService (DaoFactory.getAlunoDAO(), pessoaService);
    private EditalService editalService= new EditalService(DaoFactory.getEditalDAO());

    // --- MÉTODOS DE BUSCA (READ) ---

    public List<Aluno> getTodosOsAlunos() {
        return alunoService.getTodosOsAlunos();
    }

    public List<EditalDeMonitoria> getTodosOsEditais() {
        return editalService.getTodosOsEditais();
    }

    public Coordenador getCoordenador() {
        return coordenadorService.getCoordenador();
    }

    public Aluno recuperarAlunoPorMatricula(String numMat) {
        return alunoService.recuperarAlunoPorMatricula(numMat);
    }

    public Pessoa recuperarPessoaPorEmail(String email) {
        return pessoaService.buscarPorEmail(email);
    }

    public EditalDeMonitoria recuperarEditalPeloId(long id) {
        return editalService.recuperarEditalPeloId(id);
    }

    // --- MÉTODOS DE PERSISTÊNCIA---

    public void adicionarAluno(Aluno a) throws AlunoJaExisteException {
        alunoService.adicionarAluno(a);
    }

    public boolean adicionarCoordenador(Coordenador c) {
        return coordenadorService.adicionarCoordenador(c);
    }

    public void adicionarEdital(EditalDeMonitoria edital) throws EditalJaExisteException {
        editalService.adicionarEdital(edital);
    }

    public void salvarEdital(EditalDeMonitoria edital) {
        editalService.salvarEdital(edital);
    }

    public List<Aluno> buscarAlunosPorNome(String nome) {
        return alunoService.buscarAlunosPorNome(nome);
    }
}
