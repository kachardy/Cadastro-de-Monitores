package outros;

import pessoas.Aluno;
import pessoas.Coordenador;
import telas.TelaCadastroAluno;
import telas.TelaCadastroCoordenador;
import telas.TelaCadastroEdital; // Import da tela
import telas.TelaLogin;
import telas.TelaPrincipalCoordenador;

import javax.swing.JOptionPane;

import erros.AlunoJaExisteException;

import java.io.File;
import java.time.LocalDate; // Import para datas
import java.time.format.DateTimeFormatter; // Import para formatar datas
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
			fazerLogin(central, persistencia);
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
		        System.out.println("Voltando para o login...");
		        cadastroAluno.dispose(); 
		        fazerLogin(central, persistencia);
		    }
		});
	}
		
	
	// Método para login
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
				
				chamarTelaCoordenador(coord, central, persistencia);
				return;
				
			}
			
			// Verifica se é um Aluno
			for (Aluno a : central.getTodosOsAlunos()) {
				if (a.getEmail().equals(emailDigitado) && a.getSenha().equals(senhaDigitada)) {
					JOptionPane.showMessageDialog(login, "Bem-vindo, Aluno " + a.getNome());
					login.dispose();
					// chamarTelaAluno(a, central, persistencia);
					return;
				}
			}
			
			// Mensagem se não acha ninguém
			JOptionPane.showMessageDialog(login, "Email ou senha incorretos!");
		});
		
		login.adicionarAcaoCancelar(e -> {
			login.dispose();
			System.exit(0);
		});
		
		login.adicionarAcaoLinkCadastro(new java.awt.event.MouseAdapter() {
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        login.dispose(); 
		        fazerCadastroAluno(central, persistencia);
		    }
		});
	}
	
	private static void chamarTelaCoordenador(Coordenador coordenador, CentralDeInformacoes central, Persistencia persistencia ) {
		TelaPrincipalCoordenador telaCoord = new TelaPrincipalCoordenador(coordenador);
		
		telaCoord.adicionarAcaoCadastrarEdital(e -> {
			telaCoord.dispose();
			chamarTelaCadastroEdital(coordenador, central, persistencia);
		});
		
		telaCoord.adicionarAcaoListarEditais(e -> {
			// Falta coisa aqui!!
		});
		
		telaCoord.adicionarAcaoSair(e -> {
			telaCoord.dispose();
			fazerLogin(central, persistencia);
		});
	}
	
	private static void chamarTelaCadastroEdital(Coordenador coordenador, CentralDeInformacoes central, Persistencia persistencia) {
		TelaCadastroEdital telaEdital = new TelaCadastroEdital();
		
		//Disciplinas temporárias
		ArrayList<Disciplina> disciplinasTemporarias = new ArrayList<>();
		
		// Adicionar Disciplina
		telaEdital.adicionarAcaoAddDisciplina(e -> {
			String nome = telaEdital.getNomeDisciplina();
			int vagasRem = telaEdital.getVagasRem();
			int vagasVol = telaEdital.getVagasVol();
			
			if (nome.isEmpty()) {
				JOptionPane.showMessageDialog(telaEdital, "Digite o nome da disciplina.");
				return;
			}
			
			// Cria a disciplina e guarda na lista temporária
			Disciplina d = new Disciplina(nome, vagasRem, vagasVol);
			disciplinasTemporarias.add(d);
			
			// Atualiza a tela visualmente e limpa campos
			telaEdital.adicionarTextoDisciplina(" - " + nome + " (Rem: " + vagasRem + ", Vol: " + vagasVol + ")");
			telaEdital.limparCamposDisciplina();
		});
		
		// Salvar Edital
		telaEdital.adicionarAcaoSalvar(e -> {
			 String numeroEdital = telaEdital.getNumeroEdital(); // Captura o número
			 String dataInicioStr = telaEdital.getDataInicio();
			 String dataFimStr = telaEdital.getDataFim();
			 int maxInsc = telaEdital.getMaxInscricoes();
			 double pesoCRE = telaEdital.getPesoCRE();
			 double pesoMedia = telaEdital.getPesoMedia();
			 
			 // Número do Edital
			 if (numeroEdital.isEmpty()) {
				 JOptionPane.showMessageDialog(telaEdital, "Digite o número do edital!");
				 return;
			 }
			 
			 // Datas vazias 
			 if (dataInicioStr.contains(" ") || dataFimStr.contains(" ")) {
				 JOptionPane.showMessageDialog(telaEdital, "Preencha as datas corretamente!");
				 return;
			 }
			 
			 // Soma dos pesos deve ser 1.0
			 if (Math.abs((pesoCRE + pesoMedia) - 1.0) > 0.001) {
				 JOptionPane.showMessageDialog(telaEdital, "A soma dos pesos (CRE + Média) deve ser igual a 1.0!");
				 return;
			 }
			 
			 // Validando se tem disciplina
			 if (disciplinasTemporarias.isEmpty()) {
				 JOptionPane.showMessageDialog(telaEdital, "Adicione pelo menos uma disciplina.");
				 return;
			 }
			 
			 try {
				 // Converte as datas (String "dd/MM/yyyy")
				 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				 LocalDate inicio = LocalDate.parse(dataInicioStr, formatter);
				 LocalDate fim = LocalDate.parse(dataFimStr, formatter);
				 
				 if (fim.isBefore(inicio)) {
					 JOptionPane.showMessageDialog(telaEdital, "A data final não pode ser antes da inicial!");
					 return;
				 }
				 
				 // Adiciona as disciplinas no edital
				 EditalDeMonitoria novoEdital = new EditalDeMonitoria(numeroEdital, inicio, fim, maxInsc, pesoCRE, pesoMedia);
				 novoEdital.setTodasAsDisciplinas(disciplinasTemporarias);
				 
				 // Salva na central
				 central.getTodosOsEditais().add(novoEdital);
				 persistencia.salvarCentral(central, "central.xml");
				 
				 JOptionPane.showMessageDialog(telaEdital, "Edital cadastrado com sucesso!");
				 telaEdital.dispose();
				 chamarTelaCoordenador(coordenador, central, persistencia); // Volta pro menu
				 
			 } catch (Exception ex) {
				 JOptionPane.showMessageDialog(telaEdital, "Data inválida! Use o formato dd/mm/aaaa");
			 }
		});
		
		//Cancelar
		telaEdital.adicionarAcaoCancelar(e -> {
			telaEdital.dispose();
			chamarTelaCoordenador(coordenador, central, persistencia);
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