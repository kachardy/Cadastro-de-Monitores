package controllers;

import java.util.List;
import javax.swing.JOptionPane;
import models.*;
import views.*;
import java.util.ArrayList;

public class CoordenadorController {
    private Coordenador coord;
    private CentralDeInformacoes central;
    // REMOVIDO: private Persistencia persistencia;

    // Construtor atualizado: removido o parâmetro Persistencia
    public CoordenadorController(Coordenador coord, CentralDeInformacoes central) {
        this.coord = coord;
        this.central = central;
    }

    public void exibirMenuPrincipal() {
        TelaPrincipalCoordenador tela = new TelaPrincipalCoordenador(coord);

        tela.adicionarAcaoCadastrarEdital(e -> {
            tela.dispose();
            // Removido 'persistencia' da chamada
            new EditalController(coord, central).exibirCadastro(null);
        });

        tela.adicionarAcaoListarEditais(e -> {
            tela.dispose();
            // Removido 'persistencia' da chamada
            new EditalController(coord, central).exibirListagem();
        });

        tela.adicionarAcaoListarAlunos(e -> {
            tela.dispose();
            exibirListagemAlunos();
        });

        tela.adicionarAcaoSair(e -> {
            tela.dispose();
            // Removido 'persistencia' da chamada
            new AuthController(central).iniciar();
        });

        tela.setVisible(true);
    }

    private void exibirListagemAlunos() {
        TelaListaAlunos tela = new TelaListaAlunos();
        tela.preencherTabela(central.getTodosOsAlunos());

        // Lógica de Busca/Filtro
        tela.adicionarAcaoBuscar(e -> {
            String filtro = tela.getTextoFiltro().toLowerCase();

            if (filtro.isEmpty()) {
                tela.preencherTabela(central.getTodosOsAlunos());
            } else {
                List<Aluno> filtrados = new ArrayList<>();
                for (Aluno a : central.getTodosOsAlunos()) {
                    if (a.getNome().toLowerCase().contains(filtro)) { // Troquei para contains para ser mais flexível
                        filtrados.add(a);
                    }
                }
                tela.preencherTabela(filtrados);
            }
        });

        // Lógica para Ver/Editar Perfil do Aluno selecionado
        tela.adicionarAcaoPerfil(e -> {
            String mat = tela.getMatriculaAlunoSelecionado();

            if (mat != null) {
                Aluno alunoEncontrado = central.recuperarAlunoPorMatricula(mat);

                if (alunoEncontrado != null) {
                    tela.dispose();
                    // Removido 'persistencia' da chamada
                    new AlunoController(alunoEncontrado, central).exibirPerfil(true, this);
                }
            } else {
                JOptionPane.showMessageDialog(tela, "Selecione um aluno na tabela primeiro.");
            }
        });

        tela.adicionarAcaoVoltar(e -> {
            tela.dispose();
            exibirMenuPrincipal();
        });

        tela.setVisible(true);
    }
}