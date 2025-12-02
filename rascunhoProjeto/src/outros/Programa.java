package outros;

import pessoas.Aluno;
import pessoas.Coordenador;
import telas.TelaCadastroCoordenador;
import telas.TelaLogin;

import javax.swing.JOptionPane; // Para mensagens de sucesso

import java.io.File;
import java.util.ArrayList;

public class Programa {

	public static void main(String[] args) {
		Persistencia persistencia = new Persistencia();
		
		final CentralDeInformacoes central = recuperarOuCriarCentral(persistencia);

		// Coordenador
		if (central.getCoordenador() == null) {
			
			System.out.println("Nenhum coordenador encontrado. Abrindo tela de cadastro...");
			TelaCadastroCoordenador telaCood = new TelaCadastroCoordenador();
			
			// Ouvinte do botão
			telaCood.adicionarAcaoSalvar(e -> {
				
				// Pega os dados do coordenador
				String nome = telaCood.getNome();
				String matricula = telaCood.getMatricula();
				String email = telaCood.getEmail();
				String senha = telaCood.getSenha();
				
				// Validação dos campos
				if(nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
					JOptionPane.showMessageDialog(telaCood, "Preencha todos os campos!");
					return;
				}
				
				
				Coordenador novoCood = new Coordenador(nome, matricula, email, senha); 
				
				
				// Salva na Central e no XML
				central.setCoordenador(novoCood);
				persistencia.salvarCentral(central, "central.xml");
				
				JOptionPane.showMessageDialog(telaCood, "Coordenador cadastrado com sucesso!");
				
				telaCood.dispose(); 
				
				new TelaLogin();
			
			});
			
		} else {
			// Abre o login, pois já tem coordenador
			System.out.println("Coordenador já existe. Abrindo Login...");
			new TelaLogin();
		}
	}
	
	// Método pra a central
	private static CentralDeInformacoes recuperarOuCriarCentral(Persistencia p) {
		CentralDeInformacoes c = null;
		File arquivo = new File("central.xml");

		if (arquivo.exists() && arquivo.length() > 0) {
			try {
				c = p.recuperarCentral("central.xml");
			} catch (Exception e) {
				System.out.println("Erro ao ler central.xml (Corrompido). Criando nova base.");
			}
		} else {
			System.out.println("Arquivo central.xml não existe ou está vazio. Criando nova base...");
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