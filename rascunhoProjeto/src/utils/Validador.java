package utils;

public class Validador {
	
	public static boolean validarNome(String nome) {
	    if (nome == null || nome.isBlank()) {
	        return false;
	    }

	    nome = nome.trim();

	    // Verifica se contém apenas letras e espaços
	    if (!nome.matches("[A-Za-zÀ-ú ]+")) {
	        return false;
	    }

	    return true;
	}
	
	public static boolean validarEmail(String email) {
	    if (email == null || email.isBlank()) {
	        return false;
	    }
	    
	    email = email.trim();
	    // Regex para o email
	    String padraoEmail = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

	    if (email.matches(padraoEmail)) {
	        return true;
	    } else {
	        return false;
	    }
	}
	
	public static boolean validarMatricula(String matricula) {
	    if (matricula == null || matricula.isBlank()) {
	        return false;
	    }

	    matricula = matricula.trim();

	    // Verifica se contém apenas números utilizando regex
	    if (!matricula.matches("\\d+")) {
	        return false;
	    }

	    return true;
	}

	
	public static boolean validarSenha(String senha) {
		if(senha.length() >= 6) {
			return true;
		}
		return false;
	}
	
	public static boolean validarNumeroEdital(String matricula) {
	    if (matricula == null || matricula.isBlank()) {
	        return false;
	    }

	    matricula = matricula.trim();

	    // Verifica se contém apenas números utilizando regex
	    if (!matricula.matches("\\d+")) {
	        return false;
	    }

	    return true;
	}

}
