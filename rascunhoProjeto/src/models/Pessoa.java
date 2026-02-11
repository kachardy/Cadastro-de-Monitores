package models;

public abstract class Pessoa {
	private String nome;
	private	String matricula;
	private String email;
	private String senha;
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getMatricula() {
		return matricula;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public Pessoa(String nome, String matricula, String email, String senha) {
		this.nome = nome;
		this.matricula = matricula;
		this.email = email;
		this.senha = senha;
	}

	public String toString() {
		return nome;
	}
}
