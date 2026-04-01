package models;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "alunos_tb")
public class Aluno extends Pessoa {

    public Aluno() {
        super();
    }

    // Tabela auxiliar que guarda o histórico das monitorias
    @ElementCollection
    @CollectionTable(name = "aluno_historicos", joinColumns = @JoinColumn(name = "aluno_id"))
    @Column(name = "descricao_monitoria")
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
