package services;

import dao.AlunoDao;
import models.Aluno;
import models.Pessoa;
import erros.AlunoJaExisteException;

import java.util.List;

public class AlunoService {
    private AlunoDao alunoDao;
    private PessoaService pessoaService; // Injetamos o serviço de pessoa

    public AlunoService(AlunoDao alunoDao, PessoaService pessoaService) {
        this.alunoDao = alunoDao;
        this.pessoaService = pessoaService;
    }

    // Extraímos a lógica da verificação para um método privado para deixar o código mais legível
    private boolean existeAluno(Aluno a) {
        Pessoa existente = pessoaService.buscarPorEmail(a.getEmail());
        Aluno matriculado = alunoDao.recuperarAlunoPorMatricula(a.getMatricula());

        return (existente != null || matriculado != null);
    }
    public void adicionarAluno(Aluno a) throws AlunoJaExisteException {
        // Validação
        if (existeAluno(a)) {
            throw new AlunoJaExisteException();
        }
        alunoDao.salvar(a);
    }


    public List<Aluno> getTodosOsAlunos() {
        return alunoDao.listarTodos();
    }

    public List<Aluno> buscarAlunosPorNome(String nome) {
        return alunoDao.buscarAlunosPorNome(nome);
    }

    public Aluno recuperarAlunoPorMatricula(String numMat) {
        return alunoDao.recuperarAlunoPorMatricula(numMat);
    }
}