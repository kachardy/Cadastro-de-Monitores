package controllers;

import java.util.List;

import javax.swing.JOptionPane;

import models.*;
import views.*;

import java.util.ArrayList;

public class CoordenadorController {
    private Coordenador coord;
    private CentralDeInformacoes central;
    private Persistencia persistencia;

    public CoordenadorController(Coordenador coord, CentralDeInformacoes central, Persistencia persistencia) {
        this.coord = coord;
        this.central = central;
        this.persistencia = persistencia;
    }

    public void exibirMenuPrincipal() {
        TelaPrincipalCoordenador tela = new TelaPrincipalCoordenador(coord);
        
        tela.adicionarAcaoCadastrarEdital(e -> {
            tela.dispose();
            new EditalController(coord, central, persistencia).exibirCadastro(null);
        });

        tela.adicionarAcaoListarEditais(e -> {
            tela.dispose();
            new EditalController(coord, central, persistencia).exibirListagem();
        });

        tela.adicionarAcaoListarAlunos(e -> {
            tela.dispose();
            exibirListagemAlunos();
        });

        tela.adicionarAcaoSair(e -> {
            tela.dispose();
            new AuthController(central, persistencia).iniciar();
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
                    if (a.getNome().toLowerCase().startsWith(filtro)) {
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
                // Busca o aluno na central pela matrícula
                Aluno alunoEncontrado = central.recuperarAlunoPorMatricula(mat);
                
                if (alunoEncontrado != null) {
                    tela.dispose();
                    // Abre o AlunoController em modo de leitura (true) 
                    new AlunoController(alunoEncontrado, central, persistencia).exibirPerfil(true, this);
                }
            } else {
                JOptionPane.showMessageDialog(tela, "Selecione um aluno na tabela primeiro.");
            }
        });

        // Lógica para Voltar ao Menu Principal do Coordenador
        tela.adicionarAcaoVoltar(e -> {
            tela.dispose();
            exibirMenuPrincipal();
        });

        tela.setVisible(true);
    }
}