package controllers;

import java.util.List;
import javax.swing.JOptionPane;
import models.*;
import views.*;
import java.util.ArrayList;

public class CoordenadorController {
    private Coordenador coord;
    private CentralDeInformacoes central;

    public CoordenadorController(Coordenador coord, CentralDeInformacoes central) {
        this.coord = coord;
        this.central = central;
    }

    public void exibirMenuPrincipal() {
        TelaPrincipalCoordenador tela = new TelaPrincipalCoordenador(coord);

        tela.adicionarAcaoCadastrarEdital(e -> {
            tela.dispose();
            new EditalController(coord, central).exibirCadastro(null);
        });

        tela.adicionarAcaoListarEditais(e -> {
            tela.dispose();
            new EditalController(coord, central).exibirListagem();
        });

        tela.adicionarAcaoListarAlunos(e -> {
            tela.dispose();
            exibirListagemAlunos();
        });

        tela.adicionarAcaoSair(e -> {
            tela.dispose();
            new AuthController(central).iniciar();
        });

        tela.setVisible(true);
    }

    private void exibirListagemAlunos() {
        TelaListaAlunos tela = new TelaListaAlunos();
        // Começo a tela listando todo mundo normalmente
        tela.preencherTabela(central.getTodosOsAlunos());

        // Lógica de Busca/Filtro otimizada para o Banco de Dados
        tela.adicionarAcaoBuscar(e -> {
            String filtro = tela.getTextoFiltro();

            if (filtro.isEmpty()) {
                // Se o campo estiver vazio, mostro a lista completa
                tela.preencherTabela(central.getTodosOsAlunos());
            } else {
                // Agora eu não faço mais o loop 'for' aqui no Controller.
                // Eu peço para a Central (que pede para o AlunoDao) buscar direto no SQLite
                // usando o comando LIKE, o que é muito mais performático.
                List<Aluno> filtrados = central.buscarAlunosPorNome(filtro);
                tela.preencherTabela(filtrados);
            }
        });

        // Lógica para Ver Perfil do Aluno selecionado
        tela.adicionarAcaoPerfil(e -> {
            String mat = tela.getMatriculaAlunoSelecionado();

            if (mat != null) {
                Aluno alunoEncontrado = central.recuperarAlunoPorMatricula(mat);

                if (alunoEncontrado != null) {
                    tela.dispose();
                    // Abro o perfil do aluno em modo leitura para o coordenador visualizar
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