
package models;

import controllers.AuthController;

import java.io.File;
import java.util.ArrayList;

public class Programa {

    public static void main(String[] args) {
        Persistencia persistencia = new Persistencia();
        final CentralDeInformacoes central = recuperarOuCriarCentral(persistencia);

        // Iniciando tudo
        AuthController auth = new AuthController(central, persistencia);
        auth.iniciar();
    }

    // Mantenha apenas o método de recuperar/criar XML que você já tem:
    private static CentralDeInformacoes recuperarOuCriarCentral(Persistencia p) {
    	CentralDeInformacoes c = null;
		File arquivo = new File("central.xml");

		if (arquivo.exists() && arquivo.length() > 0) {
			try {
				c = p.recuperarCentral("central.xml");
			} catch (Exception e) {
			}
		} else {
			//System.out.println("Arquivo central.xml não existe ou está vazio. Criando nova base...");
		}
		
		if (c == null) {
			c = new CentralDeInformacoes();
		}
		
		// Garante que as listas não sejam nulas (Segurança extra)
		if (c.getTodosOsEditais() == null) c.setTodosOsEditais(new ArrayList<EditalDeMonitoria>());
		if (c.getTodosOsAlunos() == null) c.setTodosOsAlunos(new ArrayList<Aluno>());
		
		return c;
    }
}
