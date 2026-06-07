package models;

import models.estados.FaseDoEdital;
import services.GerenciadorDeInscricoes;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "editais_tb")
public class EditalDeMonitoria implements Serializable {

    // Atributos de Identificação e Configuração
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String numeroEdital;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private int maxInscricoesPorAluno;
    private double pesoCRE;
    private double pesoMedia;

    @Enumerated(EnumType.STRING)
    private FaseDoEdital fase = FaseDoEdital.ABERTO;

    // Adicionado cascade pra quando salvar o edital, salvar o catálogo de disciplinas junto.
    // O edital_id vai lá pra tabela de disciplinas, garantindo a chave estrangeira.
    // Adicionamos orphanRemoval = true para garantir que as disciplinas removidas do edital
    // sejam apagadas do banco de dados definitivamente.
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "edital_id")
    private List<Disciplina> todasAsDisciplinas = new ArrayList<>();

    // Nossa lista oficial de inscrições. É isso aqui que o JPA vai olhar pra persistir no banco.
    // Adicionamos orphanRemoval = true para garantir a eliminação real das inscrições
    // quando um aluno desiste do processo seletivo.
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "edital_id")
    private List<Inscricao> inscricoesRealizadas = new ArrayList<>();

    // Coloquei @Transient pra avisar o Hibernate pra ignorar isso na hora de criar as tabelas.
    // O gerenciador agora não guarda mais estado (listas), funciona só como um motor de cálculo.
    @Transient
    private GerenciadorDeInscricoes gerenciador = new GerenciadorDeInscricoes();

    public EditalDeMonitoria(long id, String numeroEdital, LocalDate dataInicio, LocalDate dataFim, int maxInscricoes, double pesoCRE, double pesoMedia) {
        this.id = id;
        this.numeroEdital = numeroEdital;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.maxInscricoesPorAluno = maxInscricoes;
        this.pesoCRE = pesoCRE;
        this.pesoMedia = pesoMedia;
    }

    public EditalDeMonitoria() {
    }

    // --- LÓGICA DE NEGÓCIO ---

    /**
     * Realiza uma cópia profunda (Deep Copy) do edital para um novo período.
     * Mantém as disciplinas (Catálogo), mas zera as inscrições (Estado).
     */
    public EditalDeMonitoria clonar() {
        EditalDeMonitoria novoEdital = new EditalDeMonitoria(System.currentTimeMillis(), // Novo ID temporário
                "Cópia de " + this.numeroEdital, this.dataInicio, this.dataFim, this.maxInscricoesPorAluno, this.pesoCRE, this.pesoMedia);

        // Clona as disciplinas limpando os vínculos antigos pra não dar conflito no banco
        for (Disciplina d : this.todasAsDisciplinas) {
            novoEdital.adicionarDisciplina(new Disciplina(d.getNome(), d.getVagasRemuneradas(), d.getVagasVoluntarias()));
        }
        return novoEdital;
    }

    // Registra a inscrição na nossa lista oficial (que vai pro banco)
    public boolean inscrever(Aluno aluno, Disciplina disc, double cre, double media) {
        return this.fase.inscrever(this, aluno, disc, cre, media);
    }

    // Percorre o catálogo de disciplinas e solicita ao Gerenciador que ordene cada ranking.
    public void calcularResultadoFinal() {
        this.fase.calcularResultado(this);
    }

    // Remove o aluno do processo seletivo
    public boolean desistirDoEdital(Aluno aluno) {
        return this.fase.desistir(this, aluno);
    }


    public boolean jaAcabou() {
        return LocalDate.now().isAfter(dataFim);
    }

    // Getter e Setters

    public long getId() {
        return id;
    }

    public String getNumeroEdital() {
        return numeroEdital;
    }

    public void setNumeroEdital(String num) {
        this.numeroEdital = num;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate inicio) {
        this.dataInicio = inicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate fim) {
        this.dataFim = fim;
    }

    public int getMaxInscricoesPorAluno() {
        return maxInscricoesPorAluno;
    }

    public double getPesoCRE() {
        return pesoCRE;
    }

    public double getPesoMedia() {
        return pesoMedia;
    }

    public FaseDoEdital getFase() { return fase; }

    public void setFase(FaseDoEdital fase) { this.fase = fase; }

    public List<Disciplina> getTodasAsDisciplinas() {
        return todasAsDisciplinas;
    }

    public void setTodasAsDisciplinas(ArrayList<Disciplina> lista) {
        this.todasAsDisciplinas = lista;
    }

    public void adicionarDisciplina(Disciplina d) {
        this.todasAsDisciplinas.add(d);
    }

    public List<Inscricao> getInscricoesRealizadas() {
        return inscricoesRealizadas;
    }

    public GerenciadorDeInscricoes getGerenciador() {
        return gerenciador;
    }
}