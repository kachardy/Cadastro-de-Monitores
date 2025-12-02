package erros;

public class EditalJaExisteException extends Exception{
	public EditalJaExisteException() {
        super("Edital já existe!");
    }
}
