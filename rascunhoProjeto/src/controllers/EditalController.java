package controllers	;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import models.*;
import utils.GeradorDeRelatorio;
import utils.Mensageiro;
import views.*;

public class EditalController {
    private Coordenador coord;
    private CentralDeInformacoes central;
    private Persistencia persistencia;

    public EditalController(Coordenador coord, CentralDeInformacoes central, Persistencia persistencia) {
        this.coord = coord;
        this.central = central;
        this.persistencia = persistencia;
    }

    public void exibirListagem() {
        TelaListagem tela = new TelaListagem();
        tela.preencherTabela(central.getTodosOsEditais());

        tela.adicionarAcaoDetalhar(e -> {
            Long id = tela.getIdEditalSelecionado();
            EditalDeMonitoria edital = central.recuperarEditalPeloId(id);
            if (edital != null) {
                tela.dispose();
                exibirDetalhes(edital);
            }
        });

        tela.adicionarAcaoVoltar(e -> {
            tela.dispose();
            new CoordenadorController(coord, central, persistencia).exibirMenuPrincipal();
        });
        tela.setVisible(true);
    }

    public void exibirDetalhes(EditalDeMonitoria edital) {
        TelaDetalheEditalCoordenador tela = new TelaDetalheEditalCoordenador(edital);

        tela.adicionarAcaoClonar(e -> {
            EditalDeMonitoria clone = edital.clonar();
            tela.dispose();
            exibirCadastro(clone);
        });
        
        tela.adicionarAcaoEditar(e -> {
        	tela.dispose();
        	exibirCadastro(edital);
        });
        
        tela.adicionarAcaoVerPerfil(e -> {
        	String matricula = tela.getMatriculaAlunoSelecionado();
        	Aluno aluno = central.recuperarAlunoPorMatricula(matricula);
        	if (aluno == null) {
        		JOptionPane.showMessageDialog(tela, "Selecione um aluno!");
        		return;
        	} else {
        		AlunoController alunoController = new AlunoController(aluno, central, persistencia);
        		alunoController.exibirPerfil(false, new CoordenadorController(coord, central, persistencia));
        		tela.dispose();
        	}
        });
        
        tela.adicionarAcaoEnviarEmail(e -> {
        	String matricula = tela.getMatriculaAlunoSelecionado();
        	Aluno aluno = central.recuperarAlunoPorMatricula(matricula);
        	if (aluno == null) {
        		JOptionPane.showMessageDialog(tela, "Selecione um aluno!");
        		return;
        	} else {
        		Mensageiro.enviarEmail(aluno.getEmail());
        	}
        });
        
        tela.adicionarAcaoEncerrar(e -> {
            int op = JOptionPane.showConfirmDialog(tela, "Deseja encerrar as inscrições deste edital?");
            if (op == JOptionPane.YES_OPTION) {
                // Define a data de fim para ontem
                edital.setDataFim(LocalDate.now().minusDays(1)); 
                
                persistencia.salvarCentral(central, "central.xml");
                JOptionPane.showMessageDialog(tela, "Inscrições encerradas com sucesso!");
                
                tela.dispose();
                exibirDetalhes(edital); // Recarrega a tela para atualizar o status visual
            }
        });
        
        tela.adicionarAcaoCalcular(e -> {
        	
        	if (edital.isResultadoFinal()) {
                // Chamando tela de resultado
                exibirResultadoFinal(edital);
                return;
            }
        	
            // Se o ranking ainda não foi gerado
            if (!edital.isResultadoCalculado()) {
                int op = JOptionPane.showConfirmDialog(tela, "Deseja gerar o ranking preliminar?");
                if (op == JOptionPane.YES_OPTION) {
                    edital.calcularRanking(); // Chama a lógica do edital
                    edital.setResultadoCalculado(true);
                    
                    persistencia.salvarCentral(central, "central.xml");
                    JOptionPane.showMessageDialog(tela, "Ranking calculado com sucesso!");
                    
                    tela.dispose();
                    exibirDetalhes(edital); // Atualiza a tela para mostrar a tabela ordenada
                }
            } 
            // Se já foi calculado, mas ainda não é o final
            else if (!edital.isResultadoFinal()) {
                int op = JOptionPane.showConfirmDialog(tela, "Deseja homologar o resultado final? Isso impedirá novas desistências.");
                if (op == JOptionPane.YES_OPTION) {
                    edital.setResultadoFinal(true);
                    
                    persistencia.salvarCentral(central, "central.xml");
                    JOptionPane.showMessageDialog(tela, "Edital finalizado com sucesso!");
                    
                    tela.dispose();
                    exibirDetalhes(edital);
                }
            }
        });
        
        tela.adicionarAcaoVoltar(e -> {
            tela.dispose();
            exibirListagem();
        });
        tela.setVisible(true);
    }

    public void exibirCadastro(EditalDeMonitoria editalBase) {
        TelaCadastroEdital telaEdital = new TelaCadastroEdital(editalBase);
        
        // Lista temporária para guardar as disciplinas antes de salvar
        ArrayList<Disciplina> disciplinasTemporarias = new ArrayList<>();
        
        // Se estiver editando ou clonando, popula a lista temporária com as disciplinas existentes
        if (editalBase != null) {
            for (Disciplina d : editalBase.getTodasAsDisciplinas()) {
                disciplinasTemporarias.add(new Disciplina(d.getNome(), d.getVagasRemuneradas(), d.getVagasVoluntarias()));
            }
        }
        
        // Ação de Adicionar Disciplina
        telaEdital.adicionarAcaoAddDisciplina(e -> {
            String nome = telaEdital.getNomeDisciplina();
            int vagasRem = telaEdital.getVagasRem();
            int vagasVol = telaEdital.getVagasVol();
            
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(telaEdital, "Digite o nome da disciplina.");
                return;
            }
            
            Disciplina d = new Disciplina(nome, vagasRem, vagasVol);
            disciplinasTemporarias.add(d);
            
            telaEdital.adicionarTextoDisciplina(" - " + nome + " (Rem: " + vagasRem + ", Vol: " + vagasVol + ")");
            telaEdital.limparCamposDisciplina();
        });
        
        // Ação de Salvar Edital
        telaEdital.adicionarAcaoSalvar(e -> {
            String numeroEdital = telaEdital.getNumeroEdital(); 
            String dataInicioStr = telaEdital.getDataInicio();
            String dataFimStr = telaEdital.getDataFim();
            int maxInsc = telaEdital.getMaxInscricoes();
            double pesoCRE = telaEdital.getPesoCRE();
            double pesoMedia = telaEdital.getPesoMedia();
            
            // Validações Básicas
            if (numeroEdital.isEmpty()) {
                JOptionPane.showMessageDialog(telaEdital, "Digite o número do edital!");
                return;
            }
            if (dataInicioStr.contains(" ") || dataFimStr.contains(" ")) {
                JOptionPane.showMessageDialog(telaEdital, "Preencha as datas corretamente!");
                return;
            }
            
            // Validação de Pesos
            if (Math.abs((pesoCRE + pesoMedia) - 1.0) > 0.001) {
                JOptionPane.showMessageDialog(telaEdital, "A soma dos pesos (CRE + Média) deve ser igual a 1.0!");
                return;
            }
            
            if (disciplinasTemporarias.isEmpty()) {
                JOptionPane.showMessageDialog(telaEdital, "Adicione pelo menos uma disciplina.");
                return;
            }
            
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate inicio = LocalDate.parse(dataInicioStr, formatter);
                LocalDate fim = LocalDate.parse(dataFimStr, formatter);
                
                if (fim.isBefore(inicio)) {
                    JOptionPane.showMessageDialog(telaEdital, "A data final não pode ser antes da inicial!");
                    return;
                }
                
                // Editar
                if (editalBase != null && central.getTodosOsEditais().contains(editalBase)) {
                    // Modo Edição
                    editalBase.setNumeroEdital(numeroEdital);
                    editalBase.setDataInicio(inicio);
                    editalBase.setDataFim(fim);
                    editalBase.setMaxInscricoesPorAluno(maxInsc);
                    editalBase.setPesoCRE(pesoCRE);
                    editalBase.setPesoMedia(pesoMedia);
                    editalBase.setTodasAsDisciplinas(disciplinasTemporarias);
                    JOptionPane.showMessageDialog(telaEdital, "Edital atualizado com sucesso!");
                } else {
                    // Modo Novo ou Clone
                    EditalDeMonitoria novoEdital = new EditalDeMonitoria(numeroEdital, inicio, fim, maxInsc, pesoCRE, pesoMedia);
                    novoEdital.setTodasAsDisciplinas(disciplinasTemporarias);
                    central.getTodosOsEditais().add(novoEdital);
                    JOptionPane.showMessageDialog(telaEdital, "Edital criado com sucesso!");
                }
                
                persistencia.salvarCentral(central, "central.xml");
                telaEdital.dispose();
                exibirListagem(); // Volta para a lista de editais
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(telaEdital, "Data inválida! Use o formato dd/mm/aaaa");
            }
        });
        
        telaEdital.adicionarAcaoCancelar(e -> {
            telaEdital.dispose();
            exibirListagem();
        });

        telaEdital.setVisible(true);
    }
    
    public void exibirResultadoFinal(EditalDeMonitoria edital) {
        try {
            TelaResultadoEdital telaResultado = new TelaResultadoEdital(edital);

            // Configura o botão de fechar
            telaResultado.adicionarAcaoFechar(e -> {
                telaResultado.dispose();
                exibirDetalhes(edital); 
            });

            // Configura a geração de PDF
            telaResultado.adicionarAcaoGerarPdf(e -> {
                GeradorDeRelatorio.gerarPdfResultado(edital);
                JOptionPane.showMessageDialog(telaResultado, "PDF gerado com sucesso!");
            });

            telaResultado.setVisible(true);
            telaResultado.setLocationRelativeTo(null); // Centraliza a tela
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao abrir tela de resultado: " + ex.getMessage());
        }
    }
    
}