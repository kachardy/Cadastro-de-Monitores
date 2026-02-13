package controllers	;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import models.*;
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

        tela.adicionarAcaoCalcular(e -> {
            if (!edital.isResultadoCalculado()) {
                edital.calcularRanking();
                persistencia.salvarCentral(central, "central.xml");
                JOptionPane.showMessageDialog(tela, "Ranking Preliminar Gerado!");
            } else if (!edital.isResultadoFinal()) {
                edital.setResultadoFinal(true);
                persistencia.salvarCentral(central, "central.xml");
                JOptionPane.showMessageDialog(tela, "Resultado Finalizado!");
            }
            tela.dispose();
            exibirDetalhes(edital); // Refresh
        });

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
        		tela.dispose();
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
    
}