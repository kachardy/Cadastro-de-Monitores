package views;

public class TelaCadastroCoordenador extends TelaPadraoCadastro {

	public TelaCadastroCoordenador() {
		super(); // Chama o construtor da TelaPadraoCadastro

		// Usando o método herdado da TelaBase com a largura correta (500)
		adicionarCabecalho("Cadastro de Coordenador", 500);
	}
}