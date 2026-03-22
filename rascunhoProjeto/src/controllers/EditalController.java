package controllers;

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

    // MÉTODO QUE ESTAVA EM VERMELHO
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
                edital.setDataFim(LocalDate.now().minusDays(1));
                persistencia.salvarCentral(central, "central.xml");
                JOptionPane.showMessageDialog(tela, "Inscrições encerradas com sucesso!");
                tela.dispose();
                exibirDetalhes(edital);
            }
        });

        tela.adicionarAcaoCalcular(e -> {
            if (edital.isResultadoFinal()) {
                exibirResultadoFinal(edital); // ESTE MÉTODO ESTÁ DEFINIDO ABAIXO
                return;
            }

            if (!edital.isResultadoCalculado()) {
                int op = JOptionPane.showConfirmDialog(tela, "Deseja gerar o ranking preliminar?");
                if (op == JOptionPane.YES_OPTION) {
                    edital.calcularResultadoFinal();
                    persistencia.salvarCentral(central, "central.xml");
                    JOptionPane.showMessageDialog(tela, "Ranking calculado!");
                    tela.dispose();
                    exibirDetalhes(edital);
                }
            } else {
                int op = JOptionPane.showConfirmDialog(tela, "Deseja homologar o resultado final?");
                if (op == JOptionPane.YES_OPTION) {
                    edital.setResultadoFinal(true);
                    persistencia.salvarCentral(central, "central.xml");
                    JOptionPane.showMessageDialog(tela, "Edital finalizado!");
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
        ArrayList<Disciplina> disciplinasTemporarias = new ArrayList<>();

        if (editalBase != null) {
            for (Disciplina d : editalBase.getTodasAsDisciplinas()) {
                disciplinasTemporarias.add(new Disciplina(d.getNome(), d.getVagasRemuneradas(), d.getVagasVoluntarias()));
            }
        }

        telaEdital.adicionarAcaoAddDisciplina(e -> {
            String nome = telaEdital.getNomeDisciplina();
            if (nome.isEmpty()) return;
            Disciplina d = new Disciplina(nome, telaEdital.getVagasRem(), telaEdital.getVagasVol());
            disciplinasTemporarias.add(d);
            telaEdital.adicionarTextoDisciplina(" - " + nome);
            telaEdital.limparCamposDisciplina();
        });

        telaEdital.adicionarAcaoSalvar(e -> {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate inicio = LocalDate.parse(telaEdital.getDataInicio(), formatter);
                LocalDate fim = LocalDate.parse(telaEdital.getDataFim(), formatter);

                if (editalBase != null && central.getTodosOsEditais().contains(editalBase)) {
                    // MODO EDIÇÃO
                    editalBase.setNumeroEdital(telaEdital.getNumeroEdital());
                    editalBase.setDataInicio(inicio);
                    editalBase.setDataFim(fim);
                    editalBase.setMaxInscricoesPorAluno(telaEdital.getMaxInscricoes());
                    editalBase.setPesoCRE(telaEdital.getPesoCRE());
                    editalBase.setPesoMedia(telaEdital.getPesoMedia());
                    editalBase.setTodasAsDisciplinas(disciplinasTemporarias);
                } else {
                    // MODO NOVO / CLONE
                    EditalDeMonitoria novoEdital = new EditalDeMonitoria(
                            System.currentTimeMillis(),
                            telaEdital.getNumeroEdital(),
                            inicio, fim,
                            telaEdital.getMaxInscricoes(),
                            telaEdital.getPesoCRE(),
                            telaEdital.getPesoMedia()
                    );
                    novoEdital.setTodasAsDisciplinas(disciplinasTemporarias);
                    central.getTodosOsEditais().add(novoEdital);
                }

                persistencia.salvarCentral(central, "central.xml");
                telaEdital.dispose();
                exibirListagem();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(telaEdital, "Erro nos dados. Verifique datas e pesos!");
            }
        });

        telaEdital.adicionarAcaoCancelar(e -> {
            telaEdital.dispose();
            exibirListagem();
        });
        telaEdital.setVisible(true);
    }

    // MÉTODO QUE ESTAVA EM VERMELHO
    public void exibirResultadoFinal(EditalDeMonitoria edital) {
        TelaResultadoEdital telaResultado = new TelaResultadoEdital(edital);
        telaResultado.adicionarAcaoFechar(e -> {
            telaResultado.dispose();
            exibirDetalhes(edital);
        });
        telaResultado.adicionarAcaoGerarPdf(e -> {
            GeradorDeRelatorio.gerarPdfResultado(edital);
            JOptionPane.showMessageDialog(telaResultado, "PDF gerado!");
        });
        telaResultado.setVisible(true);
    }
}