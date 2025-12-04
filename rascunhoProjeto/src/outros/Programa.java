package outros;

import pessoas.Aluno;
import pessoas.Coordenador;
import telas.TelaCadastroAluno;
import telas.TelaCadastroCoordenador;
import telas.TelaCadastroEdital;
import telas.TelaDetalheEditalAluno;
import telas.TelaDetalheEditalCoordenador;
import telas.TelaListagem;
import telas.TelaListagemAluno;
import telas.TelaLogin;
import telas.TelaPrincipalAluno;
import telas.TelaPrincipalCoordenador;

import javax.swing.JOptionPane;

import erros.AlunoJaExisteException;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
				if (!verificaCamposDoCadastro(nome, matricula, email, senha)) return;
				
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
	
	private static boolean verificaCamposDoCadastro(String nome, String matricula, String senha, String email) {
		if(nome.isEmpty() || matricula.isEmpty() || email.isEmpty() || senha.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
			return false;
		}
		return true;
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
			if (!verificaCamposDoCadastro(nome, matricula, email, senha)) return;
			
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
					chamarTelaAluno(a, central, persistencia);
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
		        System.out.println("Indo para o cadastro...");
		        login.dispose(); 
		        fazerCadastroAluno(central, persistencia);
		    }
		});
	}
	
	
	// Área do aluno
	private static void chamarTelaAluno(Aluno a, CentralDeInformacoes central, Persistencia persistencia) {
		TelaPrincipalAluno telaAluno = new TelaPrincipalAluno(a);
		
		telaAluno.adicionarAcaoListarEditais(e -> {
			telaAluno.dispose();
			chamarTelaListagemAluno(a, central, persistencia);
			
		});
		
		telaAluno.adicionarAcaoSair(e -> {
			telaAluno.dispose();
			fazerLogin(central, persistencia);
		});
	}

	private static void chamarTelaListagemAluno(Aluno a, CentralDeInformacoes central, Persistencia persistencia) {
		TelaListagemAluno telaListagemAluno = new TelaListagemAluno();
		
		telaListagemAluno.preencherTabela(central.getTodosOsEditais());
		
		telaListagemAluno.adicionarAcaoDetalhar(e -> {
			Long idSelecionado = telaListagemAluno.getIdEditalSelecionado(); // Pega o ID (Long)
			
			if (idSelecionado != null) {
				EditalDeMonitoria edital = null;
				// Busca por ID
				for(EditalDeMonitoria ed : central.getTodosOsEditais()) {
					if(ed.getId() == idSelecionado) {
						edital = ed; break;
					}
				}
				
				if(edital != null) {
					JOptionPane.showMessageDialog(telaListagemAluno, edital.toString(), "Detalhes do Edital", JOptionPane.INFORMATION_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(telaListagemAluno, "Selecione um edital na tabela.");
			}
		});
		
	
		telaListagemAluno.adicionarAcaoInscrever(e -> {
			Long idSelecionado = telaListagemAluno.getIdEditalSelecionado(); // Pega o ID (Long)
			
			if (idSelecionado != null) {
				EditalDeMonitoria edital = null;
				for(EditalDeMonitoria ed : central.getTodosOsEditais()) {
					if(ed.getId() == idSelecionado) {
						edital = ed; break;
					}
				}
				
				if (edital != null) {
					if (edital.jaAcabou()) {
						JOptionPane.showMessageDialog(telaListagemAluno, "Este edital já está encerrado!");
						return;
					}
					telaListagemAluno.dispose();
					chamarTelaDetalheEditalAluno(edital, a, central, persistencia);
				}
			} else {
				JOptionPane.showMessageDialog(telaListagemAluno, "Selecione um edital para se inscrever.");
			}
		});
		
		telaListagemAluno.adicionarAcaoVoltar(e -> {
			telaListagemAluno.dispose();
			chamarTelaAluno(a, central, persistencia);
		});
		
	}
	
	private static void chamarTelaDetalheEditalAluno(EditalDeMonitoria edital, Aluno aluno, CentralDeInformacoes central, Persistencia persistencia) {
		TelaDetalheEditalAluno telaInscricao = new TelaDetalheEditalAluno(edital);
		
		telaInscricao.adicionarAcaoInscrever(e -> {
			Disciplina disciplina = telaInscricao.getDisciplinaSelecionada();
			String creStr = telaInscricao.getCRE();
			String mediaStr = telaInscricao.getMedia();
			
			if (disciplina == null) {
				JOptionPane.showMessageDialog(telaInscricao, "Selecione uma disciplina na tabela.");
				return;
			}
			
			if (creStr.isEmpty() || mediaStr.isEmpty()) {
				JOptionPane.showMessageDialog(telaInscricao, "Informe seu CRE e Média.");
				return;
			}
			
			try {
				Double.parseDouble(creStr);
				Double.parseDouble(mediaStr);
				
				boolean sucesso = edital.inscrever(aluno, disciplina, Double.parseDouble(creStr) , Double.parseDouble(mediaStr));
				
				if (sucesso) {
					persistencia.salvarCentral(central, "central.xml");
					JOptionPane.showMessageDialog(telaInscricao, "Inscrição realizada com sucesso na disciplina: " + disciplina.getNome());
					telaInscricao.dispose();
					chamarTelaListagemAluno(aluno, central, persistencia);
				} else {
					JOptionPane.showMessageDialog(telaInscricao, "Não foi possível realizar a inscrição.\nVerifique: Datas, Vagas ou Limite de Inscrições.");
				}
				
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(telaInscricao, "CRE e Média devem ser números (ex: 8.5).");
			}
		});
		
		telaInscricao.adicionarAcaoVoltar(e -> {
			telaInscricao.dispose();
			chamarTelaListagemAluno(aluno, central, persistencia);
		});
	}
	
	private static void chamarTelaCoordenador(Coordenador coordenador, CentralDeInformacoes central, Persistencia persistencia ) {
		TelaPrincipalCoordenador telaCoord = new TelaPrincipalCoordenador(coordenador);
		
		telaCoord.adicionarAcaoCadastrarEdital(e -> {
			telaCoord.dispose();
			// Null pq é um novo edital
			chamarTelaCadastroEdital(coordenador, central, persistencia, null);
		});
		
		telaCoord.adicionarAcaoListarEditais(e -> {
			telaCoord.dispose();
			chamarTelaListagemEditais(coordenador, central, persistencia);
		});
		
		telaCoord.adicionarAcaoSair(e -> {
			telaCoord.dispose();
			fazerLogin(central, persistencia);
		});
	}
	
	private static void chamarTelaListagemEditais(Coordenador coordenador, CentralDeInformacoes central, Persistencia persistencia) {
		TelaListagem telaLista = new TelaListagem();
		telaLista.preencherTabela(central.getTodosOsEditais());
		
		telaLista.adicionarAcaoDetalhar(e -> {
			Long idSelecionado = telaLista.getIdEditalSelecionado(); // Pega o ID (Long)
			
			if (idSelecionado != null) {
				EditalDeMonitoria edital = null;
				// Busca por ID
				for (EditalDeMonitoria ed : central.getTodosOsEditais()) {
					if (ed.getId() == idSelecionado) {
						edital = ed;
						break;
					}
				}
				if (edital != null) {
					telaLista.dispose();
					chamarTelaDetalheEdital(edital, coordenador, central, persistencia);
				}
			} else {
				JOptionPane.showMessageDialog(telaLista, "Selecione um edital na tabela.");
			}
		});
		
		telaLista.adicionarAcaoVoltar(e -> {
			telaLista.dispose();
			chamarTelaCoordenador(coordenador, central, persistencia);
		});
	}
	
	private static void chamarTelaDetalheEdital(EditalDeMonitoria edital, Coordenador coordenador, CentralDeInformacoes central, Persistencia persistencia) {
		TelaDetalheEditalCoordenador telaDetalhe = new TelaDetalheEditalCoordenador(edital);
		
		telaDetalhe.adicionarAcaoClonar(e -> {
			telaDetalhe.dispose();
			
			// Clona o objeto usando o método que criamos na classe Edital
			EditalDeMonitoria clone = edital.clonar();
			clone.setNumeroEdital(edital.getNumeroEdital());
			
			// Abre a tela de cadastro com o clone
			chamarTelaCadastroEdital(coordenador, central, persistencia, clone); 
		});
		
		telaDetalhe.adicionarAcaoEncerrar(e -> {
			int op = JOptionPane.showConfirmDialog(telaDetalhe, "Deseja encerrar as inscrições agora?");
			if (op == JOptionPane.YES_OPTION) {
				edital.setDataFim(LocalDate.now().minusDays(1)); // Define fim para ontem
				persistencia.salvarCentral(central, "central.xml");
				JOptionPane.showMessageDialog(telaDetalhe, "Edital encerrado.");
				telaDetalhe.dispose();
				chamarTelaListagemEditais(coordenador, central, persistencia);
			}
		});
		
		telaDetalhe.adicionarAcaoCalcular(e -> {
			int op = JOptionPane.showConfirmDialog(telaDetalhe, "Confirmar cálculo e gerar ranking?");
			if (op == JOptionPane.YES_OPTION) {
				edital.setResultadoCalculado(true);
				persistencia.salvarCentral(central, "central.xml");
				JOptionPane.showMessageDialog(telaDetalhe, "Resultado gerado!");
				telaDetalhe.dispose();
				chamarTelaListagemEditais(coordenador, central, persistencia);
			}
		});
		
		telaDetalhe.adicionarAcaoEditar(e -> {
			telaDetalhe.dispose();
			// Passa o edital original para edição
			chamarTelaCadastroEdital(coordenador, central, persistencia, edital);
		});

		telaDetalhe.adicionarAcaoVoltar(e -> {
			telaDetalhe.dispose();
			chamarTelaListagemEditais(coordenador, central, persistencia);
		});
	}
	
	
	private static void chamarTelaCadastroEdital(Coordenador coordenador, CentralDeInformacoes central, Persistencia persistencia, EditalDeMonitoria editalBase) {
		TelaCadastroEdital telaEdital = new TelaCadastroEdital(editalBase);
		
		// Lista temporária para guardar as disciplinas antes de salvar
		ArrayList<Disciplina> disciplinasTemporarias = new ArrayList<>();
		
		if (editalBase != null) {
			for (Disciplina d : editalBase.getTodasAsDisciplinas()) {
				// Clona a disciplina para não mexer na referência original durante a edição
				disciplinasTemporarias.add(new Disciplina(d.getNome(), d.getVagasRemuneradas(), d.getVagasVoluntarias()));
			}
		}
		
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
			 String numeroEdital = telaEdital.getNumeroEdital(); 
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
			 
			 // Tem disciplina?
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
				 
				 // Se 'editalBase' existe e esta na central é edição, se não é, então é clone
				 if (editalBase != null && central.getTodosOsEditais().contains(editalBase)) {
					 // Edição: Atualiza o objeto existente
					 editalBase.setNumeroEdital(numeroEdital);
					 editalBase.setDataInicio(inicio);
					 editalBase.setDataFim(fim);
					 editalBase.setMaxInscricoesPorAluno(maxInsc);
					 editalBase.setPesoCRE(pesoCRE);
					 editalBase.setPesoMedia(pesoMedia);
					 editalBase.setTodasAsDisciplinas(disciplinasTemporarias);
					 JOptionPane.showMessageDialog(telaEdital, "Edital atualizado com sucesso!");
					 
				 } else {
					 EditalDeMonitoria novoEdital = new EditalDeMonitoria(numeroEdital, inicio, fim, maxInsc, pesoCRE, pesoMedia);
					 novoEdital.setTodasAsDisciplinas(disciplinasTemporarias);
					 central.getTodosOsEditais().add(novoEdital);
					 JOptionPane.showMessageDialog(telaEdital, "Edital criado com sucesso!");
				 }
				 
				 persistencia.salvarCentral(central, "central.xml");
				 telaEdital.dispose();
				 chamarTelaCoordenador(coordenador, central, persistencia); // Volta pro menu
				 
			 } catch (Exception ex) {
				 ex.printStackTrace(); // Bom pra debug
				 JOptionPane.showMessageDialog(telaEdital, "Data inválida! Use o formato dd/mm/aaaa");
			 }
		});
		
		// Cancelar
		telaEdital.adicionarAcaoCancelar(e -> {
			telaEdital.dispose();
			chamarTelaCoordenador(coordenador, central, persistencia);
		});
	}
}