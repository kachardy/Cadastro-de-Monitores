package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;

import models.Disciplina;
import models.EditalDeMonitoria;
import views.builders.BotaoBuilder;
import views.factories.FabricaDeComponentes;
import views.tema.Tema;

public class TelaCadastroEdital extends TelaBase {

	private JTextField tfNumeroEdital;
	private JFormattedTextField tfDataInicio;
	private JFormattedTextField tfDataFim;
	private JSpinner spinnerMaxInsc;
	private JSpinner spinnerPesoCRE;
	private JSpinner spinnerPesoMedia;
	private JTextField tfNomeDisc;
	private JSpinner spinVagasRem;
	private JSpinner spinVagasVol;
	private JTextArea areaDisciplinas;
	private JButton btnAddDisc;
	private JButton btnSalvar;
	private JButton btnCancelar;

	public TelaCadastroEdital(EditalDeMonitoria editalBase) {
		super("Cadastro de Edital", 500, 700);

		// Se o editalBase não for null, preenche os campos automaticamente
		if (editalBase != null) {
			preencherDados(editalBase);
		}

		setVisible(true);
	}

	@Override
	protected void inicializarComponentes() {
		// 1. Layout Principal
		setLayout(new BorderLayout());

		// 2. Cabeçalho Padronizado via Template Method
		adicionarCabecalho("Cadastro do Edital", 500);

		// 3. Painel Central da Grelha Responsiva
		JPanel painelCentro = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 12, 6, 12);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Máscaras de Data
		MaskFormatter mascaraData = null;
		try {
			mascaraData = new MaskFormatter("##/##/####");
			mascaraData.setPlaceholderCharacter('_');
		} catch (Exception e) {
			e.printStackTrace();
		}

		// --- Instanciação com Fábrica e Configuração de Estilo ---
		JLabel labelNumero = FabricaDeComponentes.criarLabel("Número do Edital:");
		tfNumeroEdital = FabricaDeComponentes.criarTextField();
		tfNumeroEdital.setPreferredSize(new Dimension(310, 30));

		JLabel labelDataInicio = FabricaDeComponentes.criarLabel("Início Inscrições:");
		tfDataInicio = new JFormattedTextField(mascaraData);
		tfDataInicio.setFont(Tema.FONTE_PADRAO);
		tfDataInicio.setPreferredSize(new Dimension(100, 30));

		JLabel labelDataFim = FabricaDeComponentes.criarLabel("Fim Inscrições:");
		tfDataFim = new JFormattedTextField(mascaraData);
		tfDataFim.setFont(Tema.FONTE_PADRAO);
		tfDataFim.setPreferredSize(new Dimension(90, 30));

		JLabel labelMaxInsc = FabricaDeComponentes.criarLabel("Max. Inscrições por Aluno:");
		spinnerMaxInsc = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
		spinnerMaxInsc.setFont(Tema.FONTE_PADRAO);
		spinnerMaxInsc.setPreferredSize(new Dimension(50, 30));

		JLabel labelFormula = FabricaDeComponentes.criarLabel("--- Fórmula de Ranqueamento (Soma deve ser 1.0) ---");
		labelFormula.setFont(new Font("Arial", Font.BOLD, 12));
		labelFormula.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel labelPesoCRE = FabricaDeComponentes.criarLabel("Peso CRE:");
		spinnerPesoCRE = new JSpinner(new SpinnerNumberModel(0.5, 0.0, 1.0, 0.1));
		spinnerPesoCRE.setFont(Tema.FONTE_PADRAO);
		spinnerPesoCRE.setPreferredSize(new Dimension(60, 30));

		JLabel labelPesoMedia = FabricaDeComponentes.criarLabel("Peso Média:");
		spinnerPesoMedia = new JSpinner(new SpinnerNumberModel(0.5, 0.0, 1.0, 0.1));
		spinnerPesoMedia.setFont(Tema.FONTE_PADRAO);
		spinnerPesoMedia.setPreferredSize(new Dimension(60, 30));

		JSeparator separador = new JSeparator();

		JLabel labelDisc = FabricaDeComponentes.criarLabel("Adicionar Disciplina (Vagas):");
		labelDisc.setFont(new Font("Arial", Font.BOLD, 14));

		JLabel labelNomeDisc = FabricaDeComponentes.criarLabel("Nome:");
		tfNomeDisc = FabricaDeComponentes.criarTextField();
		tfNomeDisc.setPreferredSize(new Dimension(200, 30));

		JLabel labelVagasRem = FabricaDeComponentes.criarLabel("Remuneradas:");
		spinVagasRem = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
		spinVagasRem.setFont(Tema.FONTE_PADRAO);
		spinVagasRem.setPreferredSize(new Dimension(50, 30));

		JLabel labelVagasVol = FabricaDeComponentes.criarLabel("Voluntárias:");
		spinVagasVol = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
		spinVagasVol.setFont(Tema.FONTE_PADRAO);
		spinVagasVol.setPreferredSize(new Dimension(50, 30));

		// Botão Adicionar via Builder fluente
		btnAddDisc = new BotaoBuilder()
				.comTexto("+ Adicionar")
				.build();
		btnAddDisc.setPreferredSize(new Dimension(120, 65));

		areaDisciplinas = new JTextArea("Disciplinas adicionadas aparecerão aqui...");
		areaDisciplinas.setEditable(false);
		areaDisciplinas.setFont(Tema.FONTE_PADRAO);
		areaDisciplinas.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		JScrollPane scrollDisc = new JScrollPane(areaDisciplinas);
		scrollDisc.setPreferredSize(new Dimension(430, 100));

		// --- Montagem da Estrutura na Grelha ---

		// Linha 0: Número do Edital
		gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; painelCentro.add(labelNumero, gbc);
		gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 3; painelCentro.add(tfNumeroEdital, gbc);

		// Linha 1: Bloco de Datas
		gbc.gridwidth = 1;
		gbc.gridx = 0; gbc.gridy = 1; painelCentro.add(labelDataInicio, gbc);
		gbc.gridx = 1; gbc.gridy = 1; painelCentro.add(tfDataInicio, gbc);
		gbc.gridx = 2; gbc.gridy = 1; painelCentro.add(labelDataFim, gbc);
		gbc.gridx = 3; gbc.gridy = 1; painelCentro.add(tfDataFim, gbc);

		// Linha 2: Inscrições Máximas
		gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; painelCentro.add(labelMaxInsc, gbc);
		gbc.gridx = 2; gbc.gridy = 2; gbc.gridwidth = 2; painelCentro.add(spinnerMaxInsc, gbc);

		// Linha 3: Divisória/Título Fórmulas
		gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4; painelCentro.add(labelFormula, gbc);

		// Linha 4: Pesos (CRE e Média)
		gbc.gridwidth = 1;
		gbc.gridx = 0; gbc.gridy = 4; painelCentro.add(labelPesoCRE, gbc);
		gbc.gridx = 1; gbc.gridy = 4; painelCentro.add(spinnerPesoCRE, gbc);
		gbc.gridx = 2; gbc.gridy = 4; painelCentro.add(labelPesoMedia, gbc);
		gbc.gridx = 3; gbc.gridy = 4; painelCentro.add(spinnerPesoMedia, gbc);

		// Linha 5: Separador Visual
		gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 4; painelCentro.add(separador, gbc);

		// Linha 6: Título Disciplinas
		gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 4; painelCentro.add(labelDisc, gbc);

		// Linha 7: Painel Interno de Alinhamento para Disciplinas (Evita conflitos na grelha principal)
		JPanel painelCamposDisc = new JPanel(new GridBagLayout());
		painelCamposDisc.setOpaque(false);
		GridBagConstraints gbcDisc = new GridBagConstraints();
		gbcDisc.insets = new Insets(2, 2, 2, 2);
		gbcDisc.fill = GridBagConstraints.HORIZONTAL;

		gbcDisc.gridx = 0; gbcDisc.gridy = 0; painelCamposDisc.add(labelNomeDisc, gbcDisc);
		gbcDisc.gridx = 1; gbcDisc.gridy = 0; gbcDisc.gridwidth = 3; gbcDisc.weightx = 1.0; painelCamposDisc.add(tfNomeDisc, gbcDisc);

		gbcDisc.gridwidth = 1; gbcDisc.weightx = 0;
		gbcDisc.gridx = 0; gbcDisc.gridy = 1; painelCamposDisc.add(labelVagasRem, gbcDisc);
		gbcDisc.gridx = 1; gbcDisc.gridy = 1; painelCamposDisc.add(spinVagasRem, gbcDisc);
		gbcDisc.gridx = 2; gbcDisc.gridy = 1; painelCamposDisc.add(labelVagasVol, gbcDisc);
		gbcDisc.gridx = 3; gbcDisc.gridy = 1; painelCamposDisc.add(spinVagasVol, gbcDisc);

		// Adiciona os campos e o botão lado a lado na grelha principal
		gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 3; gbc.weightx = 1.0;
		painelCentro.add(painelCamposDisc, gbc);

		gbc.gridx = 3; gbc.gridy = 7; gbc.gridwidth = 1; gbc.weightx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		painelCentro.add(btnAddDisc, gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL; // Reset

		// Linha 8: Caixa de Texto com Rolamento
		gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 4; gbc.weightx = 1.0; gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		painelCentro.add(scrollDisc, gbc);

		// 4. Configuração dos botões de ação inferiores
		btnSalvar = new BotaoBuilder()
				.comTexto("Salvar Edital")
				.comCorFundo(new Color(200, 255, 200)) // Mantida a cor suave original para conformidade visual
				.build();
		btnSalvar.setPreferredSize(new Dimension(130, 40));

		btnCancelar = new BotaoBuilder()
				.comTexto("Cancelar")
				.build();
		btnCancelar.setPreferredSize(new Dimension(130, 40));

		JPanel painelBotoes = new JPanel();
		painelBotoes.add(btnSalvar);
		painelBotoes.add(btnCancelar);
		painelBotoes.setPreferredSize(new Dimension(500, 60));

		// 5. Acoplamento final na janela mãe
		add(painelCentro, BorderLayout.CENTER);
		add(painelBotoes, BorderLayout.SOUTH);
	}

	// --- Métodos de Interface permanecem 100% Intactos para os Controllers ---
	public String getNumeroEdital() { return tfNumeroEdital.getText(); }
	public String getDataInicio() { return tfDataInicio.getText(); }
	public String getDataFim() { return tfDataFim.getText(); }
	public int getMaxInscricoes() { return (int) spinnerMaxInsc.getValue(); }
	public double getPesoCRE() { return (double) spinnerPesoCRE.getValue(); }
	public double getPesoMedia() { return (double) spinnerPesoMedia.getValue(); }
	public String getNomeDisciplina() { return tfNomeDisc.getText(); }
	public int getVagasRem() { return (int) spinVagasRem.getValue(); }
	public int getVagasVol() { return (int) spinVagasVol.getValue(); }

	public void limparCamposDisciplina() {
		tfNomeDisc.setText("");
		spinVagasRem.setValue(1);
		spinVagasVol.setValue(0);
	}

	public void adicionarTextoDisciplina(String texto) {
		if (areaDisciplinas.getText().startsWith("Disciplinas")) {
			areaDisciplinas.setText("");
		}
		areaDisciplinas.append(texto + "\n");
	}

	public void preencherDados(EditalDeMonitoria edital) {
		if (edital == null) return;

		tfNumeroEdital.setText(edital.getNumeroEdital());

		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		if (edital.getDataInicio() != null)
			tfDataInicio.setText(edital.getDataInicio().format(fmt));

		if (edital.getDataFim() != null)
			tfDataFim.setText(edital.getDataFim().format(fmt));

		spinnerMaxInsc.setValue(edital.getMaxInscricoesPorAluno());
		spinnerPesoCRE.setValue(edital.getPesoCRE());
		spinnerPesoMedia.setValue(edital.getPesoMedia());

		areaDisciplinas.setText("");
		for (Disciplina d : edital.getTodasAsDisciplinas()) {
			adicionarTextoDisciplina(" - " + d.toString());
		}
	}

	public void adicionarAcaoSalvar(ActionListener acao) { btnSalvar.addActionListener(acao); }
	public void adicionarAcaoCancelar(ActionListener acao) { btnCancelar.addActionListener(acao); }
	public void adicionarAcaoAddDisciplina(ActionListener acao) { btnAddDisc.addActionListener(acao); }

}