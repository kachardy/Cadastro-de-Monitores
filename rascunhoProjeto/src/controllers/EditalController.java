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

    public EditalController(Coordenador coord, CentralDeInformacoes central) {
        this.coord = coord;
        this.central = central;
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
            new CoordenadorController(coord, central).exibirMenuPrincipal();
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
            if (aluno != null) {
                new AlunoController(aluno, central).exibirPerfil(true, new CoordenadorController(coord, central));
                tela.dispose();
            }
        });

        tela.adicionarAcaoEnviarEmail(e -> {
            String matricula = tela.getMatriculaAlunoSelecionado();
            Aluno aluno = central.recuperarAlunoPorMatricula(matricula);
            if (aluno != null) Mensageiro.enviarEmail(aluno.getEmail());
        });

        tela.adicionarAcaoEncerrar(e -> {
            int op = JOptionPane.showConfirmDialog(tela, "Encerrar inscrições?");
            if (op == JOptionPane.YES_OPTION) {
                edital.setDataFim(LocalDate.now().minusDays(1));
                JOptionPane.showMessageDialog(tela, "Encerrado!");
                tela.dispose();
                exibirDetalhes(edital);
            }
        });

        tela.adicionarAcaoCalcular(e -> {
            if (edital.isResultadoFinal()) {
                exibirResultadoFinal(edital);
                return;
            }
            if (!edital.isResultadoCalculado()) {
                edital.calcularResultadoFinal();
                JOptionPane.showMessageDialog(tela, "Ranking calculado!");
            } else {
                edital.setResultadoFinal(true);
                JOptionPane.showMessageDialog(tela, "Edital homologado!");
            }
            tela.dispose();
            exibirDetalhes(edital);
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
            if (!nome.isEmpty()) {
                disciplinasTemporarias.add(new Disciplina(nome, telaEdital.getVagasRem(), telaEdital.getVagasVol()));
                telaEdital.adicionarTextoDisciplina(" - " + nome);
                telaEdital.limparCamposDisciplina();
            }
        });

        telaEdital.adicionarAcaoSalvar(e -> {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate inicio = LocalDate.parse(telaEdital.getDataInicio(), formatter);
                LocalDate fim = LocalDate.parse(telaEdital.getDataFim(), formatter);

                if (editalBase != null && central.getTodosOsEditais().contains(editalBase)) {
                    editalBase.setNumeroEdital(telaEdital.getNumeroEdital());
                    editalBase.setDataInicio(inicio);
                    editalBase.setDataFim(fim);
                    editalBase.setTodasAsDisciplinas(disciplinasTemporarias);
                } else {
                    EditalDeMonitoria novo = new EditalDeMonitoria(System.currentTimeMillis(), telaEdital.getNumeroEdital(), inicio, fim, telaEdital.getMaxInscricoes(), telaEdital.getPesoCRE(), telaEdital.getPesoMedia());
                    novo.setTodasAsDisciplinas(disciplinasTemporarias);
                    central.adicionarEdital(novo);
                }
                telaEdital.dispose();
                exibirListagem();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(telaEdital, "Erro nos dados.");
            }
        });

        telaEdital.adicionarAcaoCancelar(e -> {
            telaEdital.dispose();
            exibirListagem();
        });
        telaEdital.setVisible(true);
    }

    public void exibirResultadoFinal(EditalDeMonitoria edital) {
        TelaResultadoEdital telaRes = new TelaResultadoEdital(edital);
        telaRes.adicionarAcaoFechar(e -> {
            telaRes.dispose();
            exibirDetalhes(edital);
        });
        telaRes.adicionarAcaoGerarPdf(e -> {
            GeradorDeRelatorio.gerarPdfResultado(edital);
            JOptionPane.showMessageDialog(telaRes, "PDF gerado!");
        });
        telaRes.setVisible(true);
    }
}