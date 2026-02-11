package models;

import java.util.ArrayList;

public class Aluno extends Pessoa{
	
	private ArrayList<String> historicoMonitorias = new ArrayList<>();
	
	public Aluno(String nome, String matricula, String email, String senha) {
		super(nome, matricula, email, senha);
	}

	public void adicionarMonitoria(String descricao) {
	    historicoMonitorias.add(descricao);
	}

	public ArrayList<String> getHistoricoMonitorias() {
	    return historicoMonitorias;
	}


}
