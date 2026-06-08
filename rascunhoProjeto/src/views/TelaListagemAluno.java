package views;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import views.builders.BotaoBuilder;

public class TelaListagemAluno extends TelaListagem {

	private JButton botaoInscrever;
	private JButton botaoDesistir;
	private JButton botaoResultado;

	public TelaListagemAluno() {
		super();
		setTitle("Inscrição em Monitoria");

		// Usando o Builder
		botaoInscrever = new BotaoBuilder()
				.comTexto("Inscrever-se")
				.comCorFundo(new Color(100, 200, 255))
				.build();
		botaoInscrever.setPreferredSize(new Dimension(150, 40));

		botaoDesistir = new BotaoBuilder()
				.comTexto("Desistir")
				.comCorFundo(new Color(255, 220, 200))
				.build();
		botaoDesistir.setPreferredSize(new Dimension(140, 40));

		// O botão "Ver Resultado" tem estilo de Link, por isso usamos configuração customizada
		botaoResultado = new BotaoBuilder()
				.comTexto("Ver Resultado")
				.build();
		botaoResultado.setForeground(new Color(0, 100, 200));
		botaoResultado.setOpaque(false);
		botaoResultado.setContentAreaFilled(false);
		botaoResultado.setBorderPainted(false);
		botaoResultado.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// --- MAGIA DA ARQUITETURA ---
		// 1. Coloca o link de resultado no canto superior direito (onde ficava o teu Y=50)
		painelTopoSecundario.add(botaoResultado);

		// 2. Reorganiza os botões de baixo para ficarem na ordem perfeita: Detalhar, Inscrever, Desistir, Voltar
		painelBotoes.removeAll();
		painelBotoes.add(botaoDetalhar);
		painelBotoes.add(botaoInscrever);
		painelBotoes.add(botaoDesistir);
		painelBotoes.add(botaoVoltar);

		// Atualiza o painel para refletir a nova ordem
		painelBotoes.revalidate();
		painelBotoes.repaint();
	}

	public void adicionarAcaoInscrever(ActionListener acao) { botaoInscrever.addActionListener(acao); }
	public void adicionarAcaoDesistir(ActionListener acao) { botaoDesistir.addActionListener(acao); }
	public void adicionarAcaoResultado(ActionListener acao) { botaoResultado.addActionListener(acao); }


}