package models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "coordenador_tb")
public class Coordenador extends Pessoa{

	public Coordenador(String nome, String matricula, String email, String senha) {
		super(nome, matricula, email, senha);
	}

}
