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
    private boolean resultadoFinal = false;
    
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
    
    public void calcularRanking() {
        // Ordena todas as disciplinas baseadas nos pesos deste edital
        for (Disciplina d : todasAsDisciplinas) {
            d.ordenarRanking(pesoCRE, pesoMedia);
        }
        this.resultadoCalculado = true;
        atribuirMonitorias();
    }
    
    // Recalcula o ranking mas não fecha o edital
    private void recalcularRankingSemFechar() {
        for (Disciplina d : todasAsDisciplinas) {
            d.ordenarRanking(pesoCRE, pesoMedia);
        }
    }

    public boolean desistirDoEdital(Aluno aluno) {
        if (resultadoFinal) return false; // Não pode desistir se o edital já fechou resultado final

        boolean estavaInscrito = false;

        for (Disciplina d : todasAsDisciplinas) {
            ArrayList<Aluno> alunos = d.getAlunosInscritos();
            for (Aluno a : alunos) {
                if (a.getMatricula().equals(aluno.getMatricula())) {
                    d.removerAluno(aluno);
                    estavaInscrito = true;
                    break;
                }
            }
        }

        if (estavaInscrito) {
           recalcularRankingSemFechar();
        }

        return estavaInscrito;
    }

    
    public boolean validarPesos() {
        return Math.abs((pesoCRE + pesoMedia) - 1.0) < 0.001;
    }
    
    public void atribuirMonitorias() {
        for (Disciplina d : this.getTodasAsDisciplinas()) {
            int vagas = d.getTotalVagas();
            ArrayList<Aluno> inscritos = d.getAlunosInscritos();

            for (int i = 0; i < vagas; i++) {
                if (i >= inscritos.size()) {
                    break;
                }

                Aluno selecionado = inscritos.get(i);

                String descricao = "Selecionado como monitor na disciplina "
                        + d.getNome() + " (Edital " + this.getId() + ")";

                selecionado.adicionarMonitoria(descricao);
            }
        }
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
            for (Aluno aInscrito : d.getAlunosInscritos()) {
                if (aInscrito.getMatricula().equals(aluno.getMatricula())) {
                    cont++;
                }
            }
        }
        
        if (cont >= maxInscricoesPorAluno) {
            return false;
        }

        // Realiza a Inscrição
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
    
    public boolean isResultadoFinal() { 
    	return resultadoFinal; 
    }
    
    public void setResultadoFinal(boolean resultadoFinal) { 
    	this.resultadoFinal = resultadoFinal; 
    }

    public ArrayList<Disciplina> getTodasAsDisciplinas() {
        return todasAsDisciplinas;
    }

    public void setTodasAsDisciplinas(ArrayList<Disciplina> todasAsDisciplinas) {
        this.todasAsDisciplinas = todasAsDisciplinas;
    }

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