package services;

import erros.AlunoJaExisteException;
import erros.EditalJaExisteException;
import factories.ServiceFactory;
import models.*;
import java.util.List;

// Classe Facade
public class CentralFacade {

    private CoordenadorService coordenadorService;
    private PessoaService pessoaService;
    private AlunoService alunoService;
    private EditalService editalService;

    public CentralFacade() {
        this.coordenadorService = ServiceFactory.getCoordenadorService();
        this.pessoaService = ServiceFactory.getPessoaService();
        this.alunoService = ServiceFactory.getAlunoService();
        this.editalService = ServiceFactory.getEditalService();
    }

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

    public void adicionarCoordenador(Coordenador c) {
        coordenadorService.adicionarCoordenador(c);
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
