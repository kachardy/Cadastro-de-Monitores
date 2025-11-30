package outros;

import pessoas.EnumSexo;

public class Validador {
	
	public static EnumSexo validarSexo(String string) {
		if (string.equalsIgnoreCase("M") || string.equalsIgnoreCase("Masculino")){
			return EnumSexo.MASCULINO;
		} else if (string.equalsIgnoreCase("F") || string.equalsIgnoreCase("Feminino")){
			return EnumSexo.FEMININO;
		} else {
			return null;
		}
	}
	
	public static String validarNome(String nome) {
	    if (nome == null || nome.isBlank()) {
	        return null;
	    }

	    nome = nome.trim();

	    // Verifica se contém apenas letras e espaços
	    if (!nome.matches("[A-Za-zÀ-ú ]+")) {
	        return null;
	    }

	    return nome;
	}
	
	public static String validarEmail(String email) {
	    if (email == null || email.isBlank()) {
	        return null;
	    }
	    
	    email = email.trim();
	    // Regex para o email
	    String padraoEmail = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

	    if (email.matches(padraoEmail)) {
	        return email.trim();
	    } else {
	        return null;
	    }
	}
	
	public static String validarMatricula(String matricula) {
	    if (matricula == null || matricula.isBlank()) {
	        return null;
	    }

	    matricula = matricula.trim();

	    // Verifica se contém apenas números utilizando regex
	    if (!matricula.matches("\\d+")) {
	        return null;
	    }

	    return matricula;
	}

	
	public static String validarSenha(String senha) {
		if(senha.length() >= 6) {
			return senha;
		}
		return null;
	}
	
	public static String validarNumeroEdital(String matricula) {
	    if (matricula == null || matricula.isBlank()) {
	        return null;
	    }

	    matricula = matricula.trim();

	    // Verifica se contém apenas números utilizando regex
	    if (!matricula.matches("\\d+")) {
	        return null;
	    }

	    return matricula;
	}

}
