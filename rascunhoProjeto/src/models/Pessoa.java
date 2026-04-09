package models;

import javax.persistence.*;


@Entity
@Table(name = "pessoas_tb")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Column(unique = true)
    private String matricula;
    @Column(unique = true)
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pessoa() {
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
