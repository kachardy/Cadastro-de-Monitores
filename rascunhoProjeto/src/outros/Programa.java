package outros;

import pessoas.Aluno;
import pessoas.Coordenador;
import telas.TelaCadastroAluno;
import telas.TelaCadastroCoordenador;
import telas.TelaLogin;

import javax.swing.JOptionPane;

import erros.AlunoJaExisteException;

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
			telaCood.adicionarAcaoCancelar(e -> {
				telaCood.dispose();
				System.exit(0);
			}); 
			
			telaCood.adicionarAcaoSalvar(e -> {
				
				// Pega os dados do coordenador
				String nome = telaCood.getNome();
				String matricula = telaCood.getMatricula();
				String email = telaCood.getEmail();
				String senha = telaCood.getSenha();
				
				// Validação dos campos
				if (verificaCamposDoCadastro(nome, matricula, email, senha) == false) {
					return;
				}
				
				Coordenador novoCood = new Coordenador(nome, matricula, email, senha); 
				
				// Salva na Central e no XML
				central.setCoordenador(novoCood);
				persistencia.salvarCentral(central, "central.xml");
				
				JOptionPane.showMessageDialog(telaCood, "Coordenador cadastrado com sucesso!");
				
				telaCood.dispose();
				fazerLogin(central, persistencia); 
				
			
			});
			
		} else {
			System.out.println("Coordenador já existe. Abrindo Login...");
			fazerLogin(central, persistencia);
			
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
	
	
	// Método para cadastrar aluno
	private static void fazerCadastroAluno(CentralDeInformacoes central, Persistencia persistencia) {
		TelaCadastroAluno cadastroAluno = new TelaCadastroAluno();
		
		cadastroAluno.adicionarAcaoCancelar(e -> {
			cadastroAluno.dispose();
		});
		
		cadastroAluno.adicionarAcaoSalvar(e -> {
			String nome = cadastroAluno.getNome();
			String matricula = cadastroAluno.getMatricula();
			String email = cadastroAluno.getEmail();
			String senha = cadastroAluno.getSenha();
			
			// Validação dos campos
			if (verificaCamposDoCadastro(nome, matricula, email, senha) == false) {
				return;
			}
			
			Aluno aluno = new Aluno(nome, matricula, email, senha); 
			
			// Salva aluno na Central e no XML
			try {
				central.adicionarAluno(aluno);
			} catch (AlunoJaExisteException e1) {
				JOptionPane.showMessageDialog(cadastroAluno, e1.getMessage());
				return;
			}
			persistencia.salvarCentral(central, "central.xml");
			
			JOptionPane.showMessageDialog(cadastroAluno, "Aluno cadastrado com sucesso!");
			cadastroAluno.dispose();
			fazerLogin(central, persistencia);
		});
		
		cadastroAluno.adicionarAcaoLinkLogin(new java.awt.event.MouseAdapter() {
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        System.out.println("Indo para o cadastro...");
		        cadastroAluno.dispose(); 
		        fazerLogin(central, persistencia);
		    }
		});
	}
		
	
	// Método para login (Adicionei persistencia no parâmetro para repassar ao cadastro)
	private static void fazerLogin(CentralDeInformacoes central, Persistencia persistencia) {
		TelaLogin login = new TelaLogin();
		
		login.adicionarAcaoSalvar(e -> {
			
			String emailDigitado = login.getEmail();
			String senhaDigitada = login.getSenha();
			
			// Verifica se é o Coordenador
			Coordenador coord = central.getCoordenador();
			if (coord != null && coord.getEmail().equals(emailDigitado) && coord.getSenha().equals(senhaDigitada)) {
				
				JOptionPane.showMessageDialog(login, "Bem-vindo, Coordenador(a) " + coord.getNome());
				login.dispose();
				
				// new TelaPrincipalCod (Futuramente)
				return;
				
			}
			
			// Verifica se é um Aluno
			for (Aluno a : central.getTodosOsAlunos()) {
				if (a.getEmail().equals(emailDigitado) && a.getSenha().equals(senhaDigitada)) {
					JOptionPane.showMessageDialog(login, "Bem-vindo, Aluno " + a.getNome());
					login.dispose();
					return;
					
				}
			}
			
			
			// Mensagem se não acha ninguém
			JOptionPane.showMessageDialog(login, "Email ou senha incorretos!");
		});
		
		login.adicionarAcaoCancelar(e -> {
			login.dispose();
		});
		
		login.adicionarAcaoLinkCadastro(new java.awt.event.MouseAdapter() {
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        System.out.println("Indo para o cadastro...");
		        login.dispose(); 
		        fazerCadastroAluno(central, persistencia);
		    }
		});
	}
	
	private static boolean verificaCamposDoCadastro(String nome, String matricula, String senha, String email) {
		if(nome.isEmpty() || matricula.isEmpty() || email.isEmpty() || senha.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
			return false;
		}
		return true;
	}
	
}