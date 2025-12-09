package erros;

public class UsuarioJaExisteException extends Exception{
	public UsuarioJaExisteException() {
        super("Usuário já existe!");
    }
}
