package models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("COORDENADOR")
public class Coordenador extends Pessoa{
    public Coordenador () {
    }
	public Coordenador(String nome, String matricula, String email, String senha) {
		super(nome, matricula, email, senha);
	}

}
