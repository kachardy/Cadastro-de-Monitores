package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import models.EditalDeMonitoria;
import views.builders.BotaoBuilder;
import views.tema.Tema;

public class TelaListagem extends TelaBase {

	protected JTable tabelaEditais;
	protected DefaultTableModel modeloTabela;
	protected JButton botaoDetalhar;
	protected JButton botaoVoltar;

	// Paineis protegidos para permitir que a TelaListagemAluno adicione componentes
	protected JPanel painelBotoes;
	protected JPanel painelTopoSecundario;

	public TelaListagem() {
		super("Lista de Editais", 700, 500);
	}

	@Override
	public void inicializarComponentes() {
		setLayout(new BorderLayout());

		// 1. Cabeçalho Padronizado
		adicionarCabecalho("Editais Publicados", 700);

		// 2. Wrapper Central (Vai segurar o topo secundário e a tabela)
		JPanel painelCentral = new JPanel(new BorderLayout());
		painelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Margens laterais

		// 3. Painel Topo Secundário (Usado pela filha para colar o botão "Ver Resultado" à direita)
		painelTopoSecundario = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		painelTopoSecundario.setOpaque(false);
		painelCentral.add(painelTopoSecundario, BorderLayout.NORTH);

		// 4. Configuração Responsiva da Tabela
		String[] colunas = {"ID", "Início", "Fim", "Status"};
		modeloTabela = new DefaultTableModel(colunas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tabelaEditais = new JTable(modeloTabela);
		tabelaEditais.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabelaEditais.setFont(Tema.FONTE_PADRAO);
		tabelaEditais.setRowHeight(25); // Altura agradável para leitura
		tabelaEditais.getColumnModel().getColumn(0).setPreferredWidth(50);

		JScrollPane scroll = new JScrollPane(tabelaEditais);
		painelCentral.add(scroll, BorderLayout.CENTER);

		add(painelCentral, BorderLayout.CENTER);

		// 5. Painel de Botões Inferior (Centralizados lado a lado)
		painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // 20px de espaçamento entre eles

		botaoDetalhar = new BotaoBuilder()
				.comTexto("Ver Detalhes")
				.comCorFundo(new Color(200, 255, 200))
				.build();
		botaoDetalhar.setPreferredSize(new Dimension(150, 40));

		botaoVoltar = new BotaoBuilder()
				.comTexto("Voltar")
				.build();
		botaoVoltar.setPreferredSize(new Dimension(150, 40));

		painelBotoes.add(botaoDetalhar);
		painelBotoes.add(botaoVoltar);

		add(painelBotoes, BorderLayout.SOUTH);
	}

	// --- Métodos Funcionais Intactos ---
	public void preencherTabela(List<EditalDeMonitoria> listaEditais) {
		modeloTabela.setRowCount(0);
		if (listaEditais == null || listaEditais.isEmpty()) return;
		DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		for (EditalDeMonitoria edital : listaEditais) {
			String status = edital.jaAcabou() ? "Encerrado" : "Aberto";
			Object[] linha = { edital.getId(), edital.getDataInicio().format(formatador), edital.getDataFim().format(formatador), status };
			modeloTabela.addRow(linha);
		}
	}

	public Long getIdEditalSelecionado() {
		int linhaSelecionada = tabelaEditais.getSelectedRow();
		if (linhaSelecionada == -1) return null;
		return (Long) modeloTabela.getValueAt(linhaSelecionada, 0);
	}

	public void adicionarAcaoDetalhar(ActionListener acao) { botaoDetalhar.addActionListener(acao); }
	public void adicionarAcaoVoltar(ActionListener acao) { botaoVoltar.addActionListener(acao); }
}