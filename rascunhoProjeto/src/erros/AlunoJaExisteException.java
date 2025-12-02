package erros;

public class AlunoJaExisteException extends Exception {
    public AlunoJaExisteException() {
        super("Aluno já existe!");
    }
}
