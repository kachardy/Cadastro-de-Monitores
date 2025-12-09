package outros;

import pessoas.Aluno;
import pessoas.Coordenador;
import pessoas.Pessoa;
import telas.TelaCadastroAluno;
import telas.TelaCadastroCoordenador;
import telas.TelaCadastroEdital;
import telas.TelaDetalheEditalAluno;
import telas.TelaDetalheEditalCoordenador;
import telas.TelaListaAlunos;
import telas.TelaListagem;
import telas.TelaListagemAluno;
import telas.TelaLogin;
import telas.TelaPerfilAluno;
import telas.TelaPerfilAlunoSecundaria;
import telas.TelaPrincipalAluno;
import telas.TelaPrincipalCoordenador;
import telas.TelaResultadoEdital;

import javax.swing.JOptionPane;

import java.util.List;

import erros.AlunoJaExisteException;
import erros.UsuarioJaExisteException;

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
				if (!verificaCamposDoCadastro(nome, matricula, email, senha)) {
					JOptionPane.showMessageDialog(telaCood, "Os campos estão inválidos");
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
	
	private static boolean verificaCamposDoCadastro(String nome, String matricula, String senha, String email) {
		if(nome.isEmpty() || matricula.isEmpty() || email.isEmpty() || senha.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
			return false;
		}
		
		if(!Validador.validarMatricula(matricula) || !Validador.validarNome(nome) || !Validador.validarMatricula(matricula) || !Validador.validarSenha(senha)) {
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
			if (!verificaCamposDoCadastro(nome, matricula, email, senha)) {
				JOptionPane.showMessageDialog(cadastroAluno, "Os campos estão inválidos");
				return;
			}
			
			Aluno aluno = new Aluno(nome, matricula, email, senha); 
			
			// Salva aluno na Central e no XML
			try {
				central.adicionarAluno(aluno);
			} catch (AlunoJaExisteException e1) {
				JOptionPane.showMessageDialog(cadastroAluno, e1.getMessage());
				return;
			} catch (UsuarioJaExisteException e2) {
				JOptionPane.showMessageDialog(cadastroAluno, e2.getMessage());
				return;
			}
			persistencia.salvarCentral(central, "central.xml");
			
			JOptionPane.showMessageDialog(cadastroAluno, "Aluno cadastrado com sucesso!");
			cadastroAluno.dispose();
			fazerLogin(central, persistencia);
		});
		
		cadastroAluno.adicionarAcaoLinkLogin(new java.awt.event.MouseAdapter() {
		    public void mouseClicked(java.awt.event.MouseEvent e) {
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
		
		telaAluno.adicionarAcaoVerPerfil(e -> {
		    telaAluno.dispose();
		    abrirPerfilComoAluno(a, central, persistencia);
		});
		
		telaAluno.adicionarAcaoSair(e -> {
			telaAluno.dispose();
			fazerLogin(central, persistencia);
		});
	}
	
	private static void abrirPerfilComoAluno(Aluno aluno, CentralDeInformacoes central, Persistencia persistencia) {
	    TelaPerfilAluno telaPerfil = new TelaPerfilAluno(aluno,false);
	    
	    telaPerfil.adicionarAcaoSalvar(e -> {
	        boolean sucesso = salvarAlteracoesDoAluno(telaPerfil, aluno, central, persistencia);
	        if (sucesso) {
	            telaPerfil.dispose();
	            chamarTelaAluno(aluno, central, persistencia); // Volta para Menu do Aluno
	        }
	    });
	    
	    telaPerfil.adicionarAcaoVoltar(e -> {
	        telaPerfil.dispose();
	        chamarTelaAluno(aluno, central, persistencia); // Volta para Menu do Aluno
	    });
	}

	private static void chamarTelaListagemAluno(Aluno aluno, CentralDeInformacoes central, Persistencia persistencia) {
	    TelaListagemAluno telaLista = new TelaListagemAluno(); 
	    
	    telaLista.preencherTabela(central.getTodosOsEditais());
	    
	    // Detalha
	    telaLista.adicionarAcaoDetalhar(e -> {
	    	EditalDeMonitoria edital = buscarEditalSelecionado(telaLista, central);
	        if (edital == null) {
	            JOptionPane.showMessageDialog(telaLista, "Selecione um edital.");
	            return;
	        }
	        JOptionPane.showMessageDialog(telaLista, edital.toString(), "Detalhes", JOptionPane.INFORMATION_MESSAGE);
	    });

	    // Se inscreve se não tiver fechado
	    telaLista.adicionarAcaoInscrever(e -> {
	    	EditalDeMonitoria edital = buscarEditalSelecionado(telaLista, central);
	        if (edital == null) {
	            JOptionPane.showMessageDialog(telaLista, "Selecione um edital.");
	            return;
	        }

	        if (edital.jaAcabou()) {
	            JOptionPane.showMessageDialog(telaLista, "As inscrições já encerraram!");
	            return;
	        }

	        telaLista.dispose();
	        chamarTelaDetalheEditalAluno(edital, aluno, central, persistencia);
	    });

	    // Desistir
	    telaLista.adicionarAcaoDesistir(e -> {
	    	EditalDeMonitoria edital = buscarEditalSelecionado(telaLista, central);
	        if (edital == null) {
	            JOptionPane.showMessageDialog(telaLista, "Selecione um edital.");
	            return;
	        }

	        if (edital.isResultadoFinal()) {
	            JOptionPane.showMessageDialog(telaLista, "O resultado final já foi divulgado\nImpossível desistir.");
	            return;
	        }

	        boolean ok = edital.desistirDoEdital(aluno);
	        persistencia.salvarCentral(central, "central.xml");

	        if (!ok) {
	            JOptionPane.showMessageDialog(telaLista, "Você não está inscrito em nenhuma disciplina deste edital.");
	            return;
	        }

	        JOptionPane.showMessageDialog(telaLista, "Desistência realizada com sucesso!");
	        telaLista.dispose();
	        chamarTelaListagemAluno(aluno, central, persistencia);
	    });

	    telaLista.adicionarAcaoResultado(e -> {
	    	EditalDeMonitoria edital = buscarEditalSelecionado(telaLista, central);
	        if (edital == null) {
	            JOptionPane.showMessageDialog(telaLista, "Selecione um edital");
	            return;
	        }

	        if (!edital.jaAcabou() && !edital.isResultadoCalculado() && !edital.isResultadoFinal()) {
	            JOptionPane.showMessageDialog(telaLista, "Edital sem resultado disponível");
	            return;
	        }

	        chamarTelaResultadoFinal(aluno,edital);
	    });

	    telaLista.adicionarAcaoVoltar(e -> {
	        telaLista.dispose();
	        chamarTelaAluno(aluno, central, persistencia);
	    });
	}
	
	private static EditalDeMonitoria buscarEditalSelecionado(TelaListagemAluno telaLista, CentralDeInformacoes central) {
	    Long id = telaLista.getIdEditalSelecionado();

	    if (id == null) {
	        return null;
	    }

	    for (EditalDeMonitoria ed : central.getTodosOsEditais()) {
	        if (ed.getId() == id) {
	            return ed;
	        }
	    }

	    return null;
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
		
		telaCoord.adicionarAcaoListarAlunos(e -> {
			telaCoord.dispose();
			chamarTelaQueListaAlunos(coordenador, central, persistencia);
		});
		
		telaCoord.adicionarAcaoSair(e -> {
			telaCoord.dispose();
			fazerLogin(central, persistencia);
		});
	}
	
	private static void chamarTelaQueListaAlunos(Coordenador coordenador, CentralDeInformacoes central, Persistencia persistencia) {
		TelaListaAlunos tela = new TelaListaAlunos();
		
		// Carrega todos inicialmente
		tela.preencherTabela(central.getTodosOsAlunos());
		tela.adicionarAcaoBuscar(e -> {
			String filtro = tela.getTextoFiltro().toLowerCase();
			
			if (filtro.isEmpty()) {
				tela.preencherTabela(central.getTodosOsAlunos());
			} else {
				// Filtra a lista
				List<Aluno> filtrados = new ArrayList<>();
				for (Aluno a : central.getTodosOsAlunos()) {
					if (a.getNome().toLowerCase().startsWith(filtro)) {
						filtrados.add(a);
					}
				}
				tela.preencherTabela(filtrados);
			}
		});
		
		tela.adicionarAcaoPerfil(e -> {
			String matriculaSelecionada = tela.getMatriculaAlunoSelecionado();
			
            
            if (matriculaSelecionada != null) {
                // Busca o aluno na central pela matrícula
                Aluno alunoEncontrado = null;
                for (Aluno a : central.getTodosOsAlunos()) {
                    if (a.getMatricula().equals(matriculaSelecionada)) {
                        alunoEncontrado = a;
                        break;
                    }
                }
                
                if (alunoEncontrado != null) {
                    tela.dispose();
                    chamarTelaPerfilAlunoSecundario(alunoEncontrado, central, persistencia, coordenador);
                }
                
            } else {
                JOptionPane.showMessageDialog(tela, "Selecione um aluno na tabela primeiro.");
            }
		});
		
		tela.adicionarAcaoVoltar(e -> {
			tela.dispose();
			chamarTelaCoordenador(coordenador, central, persistencia);
		});
	}
	
	private static void chamarTelaPerfilAlunoSecundario(Aluno aluno, CentralDeInformacoes central, Persistencia persistencia, Coordenador coordenador) {
		TelaPerfilAlunoSecundaria tela = new TelaPerfilAlunoSecundaria(aluno, true);
		
		tela.adicionarAcaoSalvar(e -> {
			boolean sucesso = salvarAlteracoesDoAluno(tela, aluno, central, persistencia);
            if (sucesso) {
                tela.dispose();
                chamarTelaQueListaAlunos(coordenador, central, persistencia);
            }
		});
		
		tela.adicionarAcaoVoltar(e -> {
			tela.dispose();
			chamarTelaQueListaAlunos(coordenador, central, persistencia);
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
	
	private static void chamarTelaPerfilAluno(Aluno aluno, EditalDeMonitoria editalAnterior, Coordenador coord, CentralDeInformacoes central, Persistencia persistencia) {
        TelaPerfilAluno telaPerfil = new TelaPerfilAluno(aluno,true);
        
        telaPerfil.adicionarAcaoSalvar(e -> {
            boolean sucesso = salvarAlteracoesDoAluno(telaPerfil, aluno, central, persistencia);
            if (sucesso) {
                telaPerfil.dispose();
                chamarTelaDetalheEdital(editalAnterior, coord, central, persistencia); // Volta pro edital
            }
        });
        
        telaPerfil.adicionarAcaoVoltar(e -> {
            telaPerfil.dispose();
            chamarTelaDetalheEdital(editalAnterior, coord, central, persistencia); // Volta pro edital
        });
	}
	
	private static void chamarTelaDetalheEdital(EditalDeMonitoria edital, Coordenador coordenador, CentralDeInformacoes central, Persistencia persistencia) {
		TelaDetalheEditalCoordenador telaDetalhe = new TelaDetalheEditalCoordenador(edital);
		
		telaDetalhe.adicionarAcaoClonar(e -> {
			telaDetalhe.dispose();
			
			// Clona o objeto
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
            if (edital.isResultadoCalculado()) {
                // Se já calculou, o botão vira "Fechar Edital"
                if (edital.isResultadoFinal()) {
                    JOptionPane.showMessageDialog(telaDetalhe, "Este edital já está finalizado.");
                    chamarTelaResultadoFinal(coordenador, edital);
                } else {
                    int op = JOptionPane.showConfirmDialog(telaDetalhe, "Deseja encerrar desistências e gerar Resultado Final?");
                    if (op == JOptionPane.YES_OPTION) {
                        edital.setResultadoFinal(true); // Trava desistências
                        persistencia.salvarCentral(central, "central.xml");
                        JOptionPane.showMessageDialog(telaDetalhe, "Resultado Finalizado!");
                        chamarTelaResultadoFinal(coordenador,edital);
                    }
                }
            } else {
                // Calcula pela primeira vez
                int op = JOptionPane.showConfirmDialog(telaDetalhe, "Calcular ranking preliminar?");
                if (op == JOptionPane.YES_OPTION) {
                    edital.calcularRanking(); // Ordena as listas
                    persistencia.salvarCentral(central, "central.xml");
                    
                    JOptionPane.showMessageDialog(telaDetalhe, "Ranking gerado! Alunos podem ver.");
                    telaDetalhe.dispose();
                    // Recarrega a tela para mostrar a tabela já ordenada
                    chamarTelaDetalheEdital(edital, coordenador, central, persistencia);
                }
            }
        });
		
		telaDetalhe.adicionarAcaoEditar(e -> {
			telaDetalhe.dispose();
			// Passa o edital original para edição
			chamarTelaCadastroEdital(coordenador, central, persistencia, edital);
		});
		
		telaDetalhe.adicionarAcaoVerPerfil(e -> {
            String matriculaSelecionada = telaDetalhe.getMatriculaAlunoSelecionado();
            
            if (matriculaSelecionada != null) {
                // Busca o aluno na central pela matrícula
                Aluno alunoEncontrado = null;
                for (Aluno a : central.getTodosOsAlunos()) {
                    if (a.getMatricula().equals(matriculaSelecionada)) {
                        alunoEncontrado = a;
                        break;
                    }
                }
                
                if (alunoEncontrado != null) {
                    telaDetalhe.dispose();
                    // Chama a tela de perfil passando o aluno encontrado
                    chamarTelaPerfilAluno(alunoEncontrado, edital, coordenador, central, persistencia);
                }
            } else {
                JOptionPane.showMessageDialog(telaDetalhe, "Selecione um aluno na tabela primeiro.");
            }
        });
		
		telaDetalhe.adicionarAcaoEnviarEmail(e -> {
			
			if (telaDetalhe.getMatriculaAlunoSelecionado() == null) {
				JOptionPane.showMessageDialog(telaDetalhe, "Selecione um aluno antes de enviar um email!");
				return;
			}
			
			Aluno alunoSelecionado = central.recuperarAlunoPorMatricula(telaDetalhe.getMatriculaAlunoSelecionado());
			boolean sucesso = Mensageiro.enviarEmail(alunoSelecionado.getEmail());
			
			if(sucesso) {
				JOptionPane.showMessageDialog(telaDetalhe, "Email enviado com sucesso!");
			} else {
				JOptionPane.showMessageDialog(telaDetalhe, "Erro ao enviar email :(");
			}
		});

		telaDetalhe.adicionarAcaoVoltar(e -> {
			telaDetalhe.dispose();
			chamarTelaListagemEditais(coordenador, central, persistencia);
		});
	}
	
	
	private static void chamarTelaResultadoFinal(Pessoa p, EditalDeMonitoria edital) {
	    TelaResultadoEdital telaResultadoFinal = new TelaResultadoEdital(edital);

	    // Só o bigboss gera o pdf
	    if (!(p instanceof Coordenador)) {
	        telaResultadoFinal.desabilitarGerarPdf();
	    }

	    // Botão fechar
	    telaResultadoFinal.adicionarAcaoFechar(e -> {
	        telaResultadoFinal.dispose();
	    });

	    // Botão gerar PDF
	    telaResultadoFinal.adicionarAcaoGerarPdf(e -> {
	        GeradorDeRelatorio.gerarPdfResultado(edital);
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
				 JOptionPane.showMessageDialog(telaEdital, "Data inválida! Use o formato dd/mm/aaaa");
			 }
		});
		
		// Cancelar
		telaEdital.adicionarAcaoCancelar(e -> {
			telaEdital.dispose();
			chamarTelaCoordenador(coordenador, central, persistencia);
		});
	}
	
	private static boolean salvarAlteracoesDoAluno(TelaPerfilAluno tela, Aluno aluno, CentralDeInformacoes central, Persistencia persistencia) {
	    String novoNome = tela.getNome();
        String novoEmail = tela.getEmail();
        String novaSenha = tela.getSenha();
        
        if (novoNome.isEmpty() || novoEmail.isEmpty() || novaSenha.isEmpty()) {
            JOptionPane.showMessageDialog(tela, "Preencha todos os campos!");
            return false;
        }
        
        // Atualiza original
        aluno.setNome(novoNome);
        aluno.setEmail(novoEmail);
        aluno.setSenha(novaSenha);
        
        // Atualiza no edital
        for (EditalDeMonitoria ed : central.getTodosOsEditais()) {
            for (Disciplina d : ed.getTodasAsDisciplinas()) {
                ArrayList<Aluno> inscritos = d.getAlunosInscritos();
                for (int i = 0; i < inscritos.size(); i++) {
                    Aluno aInscrito = inscritos.get(i);
                    if (aInscrito.getMatricula().equals(aluno.getMatricula())) {
                        aInscrito.setNome(novoNome);
                        aInscrito.setEmail(novoEmail);
                        aInscrito.setSenha(novaSenha);
                    }
                }
            }
        }
        
        persistencia.salvarCentral(central, "central.xml");
        JOptionPane.showMessageDialog(tela, "Perfil atualizado com sucesso!");
        return true;
	}
}