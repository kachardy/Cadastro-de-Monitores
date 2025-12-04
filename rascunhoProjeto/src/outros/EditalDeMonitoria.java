package outros;

import java.time.LocalDate;
import java.util.ArrayList;
import pessoas.Aluno;

public class EditalDeMonitoria {
    
    private long id;
    private String numeroEdital;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    
    private int maxInscricoesPorAluno; 
    private double pesoCRE;            
    private double pesoMedia;          
    
    private boolean resultadoCalculado = false;
    
    private ArrayList<Disciplina> todasAsDisciplinas = new ArrayList<Disciplina>();
    
    public EditalDeMonitoria() {
        this.id = System.currentTimeMillis();
        this.pesoCRE = 1; 
        this.pesoMedia = 0;
        this.maxInscricoesPorAluno = 1;
    }
    
    public EditalDeMonitoria(String numeroEdital, LocalDate inicio, LocalDate fim, int maxInsc, double pCRE, double pMedia) {
        this.id = System.currentTimeMillis();
        this.numeroEdital = numeroEdital;
        this.dataInicio = inicio;
        this.dataFim = fim;
        this.maxInscricoesPorAluno = maxInsc;
        this.pesoCRE = pCRE;
        this.pesoMedia = pMedia;
    }

    public void adicionarDisciplina(Disciplina d) {		
        this.todasAsDisciplinas.add(d);
    }
    
    public boolean validarPesos() {
        return Math.abs((pesoCRE + pesoMedia) - 1.0) < 0.001;
    }

    // Incrição do querido aluno
    public boolean inscrever(Aluno aluno, Disciplina disciplina, double cre, double media) {
        LocalDate hoje = LocalDate.now();
        
        // Verifica Prazo
        if (hoje.isBefore(dataInicio) || hoje.isAfter(dataFim)) {
            return false;
        }
        
        // Verifica se a disciplina pertence a este edital
        if (!todasAsDisciplinas.contains(disciplina)) {
            return false;
        }

        // Verifica Limite de Inscrições do Aluno neste Edital
        int cont = 0;
        for (Disciplina d : todasAsDisciplinas) {
            if (d.getAlunosInscritos().contains(aluno)) {
                cont++;
            }
        }
        
        if (cont >= maxInscricoesPorAluno) {
            return false;
        }

        // Realiza a Inscrição nas Listas Paralelas
        disciplina.adicionarAluno(aluno, cre, media);
        
        return true;
    }
    
    public EditalDeMonitoria clonar() {
        EditalDeMonitoria clone = new EditalDeMonitoria();
        
        clone.setDataInicio(this.dataInicio);
        clone.setDataFim(this.dataFim);
        clone.setMaxInscricoesPorAluno(this.maxInscricoesPorAluno);
        clone.setPesoCRE(this.pesoCRE);
        clone.setPesoMedia(this.pesoMedia);
        
        for (Disciplina d : this.todasAsDisciplinas) {
            Disciplina novaD = new Disciplina(d.getNome(), d.getVagasRemuneradas(), d.getVagasVoluntarias());
            clone.adicionarDisciplina(novaD);
        }
        
        return clone;
    }
    
    // Getters e Setters
    
    public long getId() {
        return id;
    }
    
    public String getNumeroEdital() {
        return numeroEdital;
    }

    public void setNumeroEdital(String numeroEdital) {
        this.numeroEdital = numeroEdital;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public int getMaxInscricoesPorAluno() {
        return maxInscricoesPorAluno;
    }

    public void setMaxInscricoesPorAluno(int maxInscricoesPorAluno) {
        this.maxInscricoesPorAluno = maxInscricoesPorAluno;
    }

    public double getPesoCRE() {
        return pesoCRE;
    }

    public void setPesoCRE(double pesoCRE) {
        this.pesoCRE = pesoCRE;
    }

    public double getPesoMedia() {
        return pesoMedia;
    }

    public void setPesoMedia(double pesoMedia) {
        this.pesoMedia = pesoMedia;
    }

    public boolean isResultadoCalculado() {
        return resultadoCalculado;
    }

    public void setResultadoCalculado(boolean resultadoCalculado) {
        this.resultadoCalculado = resultadoCalculado;
    }

    public ArrayList<Disciplina> getTodasAsDisciplinas() {
        return todasAsDisciplinas;
    }

    public void setTodasAsDisciplinas(ArrayList<Disciplina> todasAsDisciplinas) {
        this.todasAsDisciplinas = todasAsDisciplinas;
    }
    
    // Métodos ùteis

    public boolean jaAcabou() {
        return LocalDate.now().isAfter(dataFim);
    }
    
    public String percorrerDisciplinas() {
        String resultado = "";
        
        if (todasAsDisciplinas.isEmpty()) {
            return " Nenhuma disciplina cadastrada!";
        }
        
        for (Disciplina d : todasAsDisciplinas) {
            resultado += "\n - " + d.toString();
        }
        return resultado;
    }
    
    public Disciplina recuperarDisciplinaPeloNome(String nomeDisciplina) {
        for (Disciplina d : todasAsDisciplinas) {
            if (d.getNome().equalsIgnoreCase(nomeDisciplina)) {
                return d;
            }
        }
        return null;
    }
    
    public String toString() {
        String status = jaAcabou() ? "Fechado" : "Aberto";
        return "Edital " + numeroEdital + " (" + status + ")" +
               "\nPrazo: " + dataInicio + " até " + dataFim +
               "\nRegras: Max " + maxInscricoesPorAluno + " insc/aluno | Pesos: CRE(" + pesoCRE + ") Média(" + pesoMedia + ")" +
               "\nDisciplinas:" + percorrerDisciplinas();
    }
}