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
	
	private ArrayList<Disciplina> todasAsDisciplinas = new ArrayList<Disciplina>();
	
	public EditalDeMonitoria() {
		this.id = System.currentTimeMillis();
		// Valores padrão para evitar erros matemáticos hehe
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
	
	// Validação de soma dos pesos
	public boolean validarPesos() {
		// Soma deve ser 1.0 
		return Math.abs((pesoCRE + pesoMedia) - 1.0) < 0.001;
	}

	public boolean inscrever(Aluno aluno, Disciplina disciplina) {
		LocalDate hoje = LocalDate.now();
		
		// Verifica Datas
		if (hoje.isBefore(dataInicio) || hoje.isAfter(dataFim)) {
			System.out.println("Fora do prazo de inscrição.");
			return false;
		}
		
		// Verifica Existência da Disciplina
		if (!todasAsDisciplinas.contains(disciplina)) {
			return false;
		}

		// Verifica Vagas
		if (disciplina.getAlunosInscritos().size() >= disciplina.getTotalVagas()) {
			System.out.println("Vagas Esgotadas");
			return false;
		}
		
		// Verifica se aluno já estourou o limite de inscrições neste edital
		int inscricoesDoAluno = 0;
		for(Disciplina d : todasAsDisciplinas) {
			if(d.getAlunosInscritos().contains(aluno)) {
				inscricoesDoAluno++;
			}
		}
		
		if (inscricoesDoAluno >= maxInscricoesPorAluno) {
			System.out.println("Aluno atingiu o limite de inscrições para este edital.");
			return false;
		}

		// Inscrição do aluno (se deu tudo certo né)
		disciplina.adicionarAluno(aluno);
		Mensageiro.enviarEmail(aluno.getEmail());
		return true;
	}
	
	// Getters e Setters
	
	public long getId() { 
		return id;
	}
	
	public String getNumeroEdital() { 
		return numeroEdital;
	}
	public void setNumeroEdital(String n) { 
		this.numeroEdital = n; 
	}

	public LocalDate getDataInicio() { 
		return dataInicio; 
	}
	
	public void setDataInicio(LocalDate d) { 
		this.dataInicio = d; 
	}

	public LocalDate getDataFim() { 
		return dataFim; 
	}
	
	public void setDataFim(LocalDate d) { 
		this.dataFim = d; 
	}

	public int getMaxInscricoesPorAluno() { 
		return maxInscricoesPorAluno; 
	}
	
	public void setMaxInscricoesPorAluno(int m) { 
		this.maxInscricoesPorAluno = m; 
	}

	public double getPesoCRE() { 
		return pesoCRE; 
	}
	
	public void setPesoCRE(double p) { 
		this.pesoCRE = p; 
	}

	public double getPesoMedia() { 
		return pesoMedia; 
	}
	
	public void setPesoMedia(double p) { 
		this.pesoMedia = p; 
	}

	public ArrayList<Disciplina> getTodasAsDisciplinas() { 
		return todasAsDisciplinas; 
	}
	
	public void setTodasAsDisciplinas(ArrayList<Disciplina> t) { 
		this.todasAsDisciplinas = t; 
	}
	
	// Métodos úteis

	public boolean jaAcabou() {
		return LocalDate.now().isAfter(dataFim);
	}
	
	public String percorrerDisciplinas() {
	    String resultado = "";
	    if (todasAsDisciplinas.isEmpty()) return " Nenhuma disciplina cadastrada!";
	    
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