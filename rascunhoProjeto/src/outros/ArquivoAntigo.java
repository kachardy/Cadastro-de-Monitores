package outros;
// Arquivo antigo da lista de aquecimento

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import pessoas.Aluno;

public class ArquivoAntigo {
	/*
			while (true) {
	        	System.out.println("------------------------------------------");
	        	System.out.println("Opções:\n1-Cadastrar Aluno\n2-Listar Alunos\n"
	        			+ "3-Buscar aluno específico\n4-Novo Edital\n5-Informar Quantidade de Editais Cadastrados\n"
	        			+ "6-Detalhar Edital Específico\n7-Inscrever Aluno em vaga de Algum Edital\n"
	        			+ "8-Gerar Relatório de um Aluno\nS-Sair");
	        	
	        	System.out.println("------------------------------------------");
	        	
	        	String opcao = leitor.nextLine();
	        	
	        	// --- opção 1: cadastrar aluno ---
	        	if (opcao.equals("1")) {
	        		
	        		// Testa a matrícula e valida ela
	                System.out.print("Digite a matrícula: ");
	                String matricula = leitor.nextLine();
	                
	                String testeMatricula = Validador.validarMatricula(matricula);
	                
	                if(testeMatricula == null) {
	                	System.out.println("A matrícula só pode conter números!");
	                	continue;
	                }
	                
	                // Testa o nome e valida ele
	                System.out.print("Digite o nome: ");
	                String nome = leitor.nextLine();
	                
	                String testeNome = Validador.validarNome(nome);
	                
	                if (testeNome == null) {
	                	System.out.println("Nome inválido");
	                	continue;
	                }
	                
	                // Testa o email e valida ele
	                System.out.print("Digite o email: ");
	                String email = leitor.nextLine();
	                
	                String testeEmail = Validador.validarEmail(email);
	                
	                if(testeEmail == null) {
	                	System.out.println("Email Inválido!");
	                	continue;
	                }
	                
	                // Testa a senha e valida ela
	                System.out.print("Digite a senha: ");
	                String senha = leitor.nextLine();
	                
	                String testeSenha = Validador.validarSenha(senha);
	                
	                if(testeSenha == null) {
	                	System.out.println("A senha deve ser igual ou maior que 6 caracteres");
	                	continue;
	                }
	                
	                Aluno novoAluno = new Aluno(nome, matricula, email, senha);

	                if (central.adicionarAluno(novoAluno)) {
	                    // salva a central novamente
	                    persistencia.salvarCentral(central, "central.xml");
	                }

	            // --- opção 2: listar alunos ---
	            } else if (opcao.equals("2")) {
	                if (central.getTodosOsAlunos().isEmpty()) {
	                    System.out.println("Nenhum aluno cadastrado ainda.");
	                } else {
	                    System.out.println("\n--- Alunos cadastrados ---");
	                    for (Aluno a : central.getTodosOsAlunos()) {
	                        System.out.println(a.getMatricula() + " - " + a.getNome());
	                    }
	                }

	            // --- opção 3: buscar aluno ---
	            } else if (opcao.equals("3")) {
	                System.out.print("Digite a matrícula: ");
	                String mat = leitor.nextLine();

	                Aluno encontrado = central.recuperarAlunoPorMatricula(mat);

	                if (encontrado != null) {
	                    System.out.println("Aluno encontrado!");
	                    System.out.println("Matrícula: " + encontrado.getMatricula());
	                    System.out.println("Nome: " + encontrado.getNome());
	                } else {
	                    System.out.println("Aluno não encontrado!");
	                }
	                
	            
	             // --- opção 4: criar um edital ---
	            } else if (opcao.equalsIgnoreCase("4")) {
		            // Formatador de Data
		            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		            
		            System.out.println("Criando edital.....");
		            System.out.println("Digite o número do edital: ");
		            String numeroEdital = leitor.nextLine();
		            
		            String testeNumeroEdital = Validador.validarNumeroEdital(numeroEdital);
		            
		            if (testeNumeroEdital == null) {
		            	System.out.println("O número do edital deve conter somente números!");
		            	continue;
		            }
		            
		            try {
			            System.out.print("Digite a data de início (dd/MM/yyyy): ");
			            LocalDate dataInicio = LocalDate.parse(leitor.nextLine(), formatter);
			            
			            System.out.print("Digite a data de fim (dd/MM/yyyy): ");
			            LocalDate dataFim = LocalDate.parse(leitor.nextLine(), formatter);
			            
			            if (!dataInicio.isBefore(dataFim)) {
			            	System.out.println("A data de ínicio deve ser antes da data final!");
			            	continue;
			            }
			            EditalDeMonitoria edital = new EditalDeMonitoria(numeroEdital, dataInicio, dataFim);
		            
			            while(true) {
			            	System.out.println("Adicionando disciplinas....");
			            	System.out.println("Nome da disciplina:");
			            	String nome = leitor.nextLine();
			            	System.out.println("Quantidade de vagas:");
			            	
			            	int qtdVagas = Integer.parseInt(leitor.nextLine());
			            	
			            	Disciplina disciplina = new Disciplina(nome, qtdVagas);
			            	edital.adicionarDisciplina(disciplina);
			            	System.out.println("Deseja cadastrar mais uma nesse edital? (Digite 'N' caso não)");
			            	if (leitor.nextLine().equalsIgnoreCase("N")) {
			            		break;
			            	}
			            }
			             
			            central.adicionarEdital(edital);
			            persistencia.salvarCentral(central, "central.xml");
			            
		            } catch (Exception e) {
		            	System.out.println("Erro na criação do edital!");
		            }
		            
		        // --- opção 5: listar editais ---
	            } else if (opcao.equalsIgnoreCase("5")) {
	            	System.out.println(central.percorrerEditais());
	            
	            // --- opção 6: busucar edital específico ---
	            } else if (opcao.equalsIgnoreCase("6")) {
	            	System.out.println("Digite o id do edital: ");
	            	try {
	            		long id = Long.parseLong(leitor.nextLine());
	            		System.out.println(central.recuperarEditalPeloId(id));
	            	} catch (Exception e){
	            		System.out.println("Erro na busca do edital");
	            	}
	            	
	            // --- opção 7: inscrever aluno em alguma vaga ---
	            } else if (opcao.equalsIgnoreCase("7")) {
	            	
	            	System.out.println("Digite a matrícula do aluno que deseja inscrever:");
	            	String matricula = leitor.nextLine();
	            	
	            	System.out.println("Digite o número id edital que deseja inscrever o aluno:");
	            	
	            	// Evitar erro e declarar antes do try
	            	long id = -1;
	            	try {
	            		id = Long.parseLong(leitor.nextLine());
	            		System.out.println(central.recuperarEditalPeloId(id));
	            	} catch (Exception e){
	            		System.out.println("Erro na busca do edital");
	            	}
	            	
	            	System.out.println("Digite a disciplina que deseja inscrever o aluno:");
	            	String nomeDisciplina = leitor.nextLine();
	            	
	            	EditalDeMonitoria edital = central.recuperarEditalPeloId(id);
	            	Aluno aluno = central.recuperarAlunoPorMatricula(matricula);
	            	Disciplina disciplina = null;
	            	try {
	            		disciplina = edital.recuperarDisciplinaPeloNome(nomeDisciplina);
	            	} catch (Exception e) {
	            		System.out.println("Erro ao inscrever aluno!");
	            		continue;
	            	}
	            	
	            	edital.inscrever(aluno, disciplina);
	            	persistencia.salvarCentral(central, "central.xml");
	            	
	             
	            // --- opção 8: gerar comprovante em pdf  ---
	            } else if (opcao.equalsIgnoreCase("8")) {
	            	System.out.println("Digite a matrícula do aluno:");
	            	String matricula = leitor.nextLine();
	            
	            	System.out.println("Digite o número id do edital");
	            	long id = -1;
	            	try {
	            		id = Long.parseLong(leitor.nextLine());
	            		System.out.println(central.recuperarEditalPeloId(id));
	            	} catch (Exception e){
	            		System.out.println("Erro ao gerar comprovante!");
	            		continue;
	            	}
	            	
	            	
	            	GeradorDeRelatorio.obterComprovanteDeInscricaoAluno(matricula,id,central);
	            
	            // --- sair ---
	            } else if (opcao.equalsIgnoreCase("S")) { 
	                System.out.println("Encerrando o programa...");
	                break;

	            } else {
	                System.out.println("Opção inválida. Tente novamente.");
	            }
	        }
			*/
}
