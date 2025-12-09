package erros;

public class EmailJaExisteException extends Exception{
	public EmailJaExisteException() {
        super("E-mail já cadastrado!");
    }
}
